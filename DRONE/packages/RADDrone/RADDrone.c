/*
  Copyright (C) 2014 Parrot SA

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions
  are met:
  * Redistributions of source code must retain the above copyright
  notice, this list of conditions and the following disclaimer.
  * Redistributions in binary form must reproduce the above copyright
  notice, this list of conditions and the following disclaimer in
  the documentation and/or other materials provided with the
  distribution.
  * Neither the name of Parrot nor the names
  of its contributors may be used to endorse or promote products
  derived from this software without specific prior written
  permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
  FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
  COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
  OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
  AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
  SUCH DAMAGE.
*/
/**
 * @file BebopSample.c
 * @brief This file contains sources about basic arsdk example sending commands to a bebop drone to pilot it,
 * receive its battery level and display the video stream.
 * @date 15/01/2015
 */

/*****************************************
 *
 *             include file :
 *
 *****************************************/

#include <stdlib.h>
#include <fcntl.h>
#include <curses.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <errno.h>
#include <pthread.h>

#include <libARSAL/ARSAL.h>
#include <libARController/ARController.h>
#include <libARDiscovery/ARDiscovery.h>

#include "RADDrone.h"
#include "ihm.h"

/*****************************************
 *
 *             define :
 *
 *****************************************/
#define TAG "RADDrone"

#define ERROR_STR_LENGTH 2048

#define BEBOP_IP_ADDRESS "192.168.42.1"
#define BEBOP_DISCOVERY_PORT 44444

/*****************************************
 *
 *             private header:
 *
 ****************************************/

void writeStrToLog(char* str);
void IHM_PrintMessage_H(IHM_t *ihm, char* str);
void IHM_PrintError_H(IHM_t *ihm, char* str);
eARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE getFlyingState(ARCONTROLLER_Device_t *deviceController);
void waitForLanding(ARCONTROLLER_Device_t *deviceController);

/*****************************************
 *
 *             implementation :
 *
 *****************************************/

//TODO nouveau code
pthread_t tidAutoPilot = 0;

int fLogFile = -1;

//static char fifo_dir[] = FIFO_DIR_PATTERN;
//static char fifo_name[128] = "";

int gIHMRun = 1;
char gErrorStr[ERROR_STR_LENGTH];
char msg[512];
IHM_t *ihm = NULL;

ARSAL_Sem_t stateSem;
int isBebop2 = 0;


static void signal_handler(int signal)
{
    gIHMRun = 0;
}

int main (int argc, char *argv[])
{
    // logs
    fLogFile = open("RADDrone.log", O_WRONLY|O_CREAT|O_TRUNC, 0600);
    // local declarations
    int failed = 0;
    ARDISCOVERY_Device_t *device = NULL;
    ARCONTROLLER_Device_t *deviceController = NULL;
    eARCONTROLLER_ERROR error = ARCONTROLLER_OK;
    eARCONTROLLER_DEVICE_STATE deviceState = ARCONTROLLER_DEVICE_STATE_MAX;

    /* Set signal handlers */
    struct sigaction sig_action = {
        .sa_handler = signal_handler,
    };

    int ret = sigaction(SIGINT, &sig_action, NULL);
    if (ret < 0)
    {
        fprintf(stderr, "Unable to set SIGINT handler : %d(%s)", errno, strerror(errno));
        return 1;
    }
    ret = sigaction(SIGPIPE, &sig_action, NULL);
    if (ret < 0)
    {
        fprintf(stderr, "Unable to set SIGPIPE handler : %d(%s)", errno, strerror(errno));
        return 1;
    }

    ARSAL_Sem_Init (&(stateSem), 0, 0);

    fprintf(stdout, "Select your Bebop : Bebop (1) ; Bebop2 (2)");
    char answer = '1';
    scanf(" %c", &answer);
    if (answer == '2')
    {
        isBebop2 = 1;
    }

    if(isBebop2)
    {
        fprintf(stdout, "-- Bebop 2 Sample --");
    }
    else
    {
        fprintf(stdout, "-- Bebop Sample --");
    }

    ihm = IHM_New (&onInputEvent);
    if (ihm != NULL)
    {
        gErrorStr[0] = '\0';

        ARSAL_Print_SetCallback (customPrintCallback); //use a custom callback to print, for not disturb ncurses IHM

        if(isBebop2)
        {
            IHM_PrintHeader (ihm, "-- Bebop 2 Sample --");
        }
        else
        {
            IHM_PrintHeader (ihm, "-- Bebop Sample --");
        }
    }
    else
    {
        fprintf (stderr, "Creation of IHM failed.");
        failed = 1;
    }
    
    // create a discovery device
    if (!failed)
    {
        sprintf(msg, "- init discovey device ... ");
        char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
        IHM_PrintMessage_H(ihm, msgTemp);
        eARDISCOVERY_ERROR errorDiscovery = ARDISCOVERY_OK;

        device = ARDISCOVERY_Device_New (&errorDiscovery);

        if (errorDiscovery == ARDISCOVERY_OK)
        {
            sprintf(msg, "    - ARDISCOVERY_Device_InitWifi ...");
            char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
            IHM_PrintMessage_H(ihm, msgTemp);
            // create a Bebop drone discovery device (ARDISCOVERY_PRODUCT_ARDRONE)

            if(isBebop2)
            {
                errorDiscovery = ARDISCOVERY_Device_InitWifi (device, ARDISCOVERY_PRODUCT_BEBOP_2, "bebop2", BEBOP_IP_ADDRESS, BEBOP_DISCOVERY_PORT);
            }
            else
            {
                errorDiscovery = ARDISCOVERY_Device_InitWifi (device, ARDISCOVERY_PRODUCT_ARDRONE, "bebop", BEBOP_IP_ADDRESS, BEBOP_DISCOVERY_PORT);
            }

            if (errorDiscovery != ARDISCOVERY_OK)
            {
                failed = 1;
                sprintf(msg, "Discovery error :%s", ARDISCOVERY_Error_ToString(errorDiscovery));
                char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
                IHM_PrintError_H(ihm, msgTemp);
            }
        }
        else
        {
            sprintf(msg, "Discovery error :%s", ARDISCOVERY_Error_ToString(errorDiscovery));
            char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
            IHM_PrintError_H(ihm, msgTemp);
            failed = 1;
        }
    }

    // create a device controller
    if (!failed)
    {
        deviceController = ARCONTROLLER_Device_New (device, &error);

        if (error != ARCONTROLLER_OK)
        {
            sprintf(msg, "Creation of deviceController failed.");
            char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
            IHM_PrintError_H(ihm, msgTemp);
            failed = 1;
        }
        else
        {
            IHM_setCustomData(ihm, deviceController);
        }
    }
    
    if (!failed)
    {
        sprintf(msg, "- delete discovey device ... ");
        char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
        IHM_PrintMessage_H(ihm, msgTemp);
        ARDISCOVERY_Device_Delete (&device);
    }

    // add the state change callback to be informed when the device controller starts, stops...
    if (!failed)
    {
        error = ARCONTROLLER_Device_AddStateChangedCallback (deviceController, stateChanged, deviceController);

        if (error != ARCONTROLLER_OK)
        {
            sprintf(msg, "add State callback failed.");
            char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
            IHM_PrintError_H(ihm, msgTemp);
            failed = 1;
        }
    }

    // add the command received callback to be informed when a command has been received from the device
    if (!failed)
    {
        error = ARCONTROLLER_Device_AddCommandReceivedCallback (deviceController, commandReceived, deviceController);

        if (error != ARCONTROLLER_OK)
        {
            sprintf(msg, "add callback failed.");
            char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
            IHM_PrintError_H(ihm, msgTemp);
            failed = 1;
        }
    }

    if (!failed)
    {
        IHM_PrintInfo(ihm, "Connecting ...");
        error = ARCONTROLLER_Device_Start (deviceController);

        if (error != ARCONTROLLER_OK)
        {
            failed = 1;
            sprintf(msg, "- error :%s", ARCONTROLLER_Error_ToString(error));
            char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
            IHM_PrintError_H(ihm, msgTemp);
        }
    }
    
    if (!failed)
    {
        // wait state update update
        ARSAL_Sem_Wait (&(stateSem));

        deviceState = ARCONTROLLER_Device_GetState (deviceController, &error);

        if ((error != ARCONTROLLER_OK) || (deviceState != ARCONTROLLER_DEVICE_STATE_RUNNING))
        {
            failed = 1;
            sprintf(msg, "- deviceState :%d", deviceState);
            char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
            IHM_PrintError_H(ihm, msgTemp);
            sprintf(msg, "- error :%s", ARCONTROLLER_Error_ToString(error));
            char *msgTemp2 = malloc(strlen(msg)+1); strcpy(msgTemp2, msg);
            IHM_PrintError_H(ihm, msgTemp2);
        }
    }

    if (!failed)
    {
        //TODO nouveau code
        signal(SIGUSR1, sigusrHandler);
        if(!failed) {
            if(pthread_create(&tidAutoPilot, NULL, customPiloting, deviceController) == -1) {
                sprintf(msg, "AutoPilot thread creation failed.");
                char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
                IHM_PrintError_H(ihm, msgTemp);
                failed = 1;
            }
        }

        IHM_PrintInfo(ihm, "Running ... ('t' to takeoff ; Spacebar to land ; 'e' for emergency ; Arrow keys and ('r','f','d','g') to move ; 'q' to quit)");
        
        while (gIHMRun)
        {
            usleep(50);
        }
    } else {
        sprintf(msg, "Error occured, see logs, program will exit in two seconds");
        char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
        IHM_PrintError_H(ihm, msgTemp);
        sleep(2);
    }
    
    IHM_Delete (&ihm);

    // we are here because of a disconnection or user has quit IHM, so safely delete everything
    if (deviceController != NULL)
    {


        deviceState = ARCONTROLLER_Device_GetState (deviceController, &error);
        if ((error == ARCONTROLLER_OK) && (deviceState != ARCONTROLLER_DEVICE_STATE_STOPPED))
        {
            IHM_PrintInfo(ihm, "Disconnecting ...");
            fprintf(stdout, "Disconnecting ...");

            error = ARCONTROLLER_Device_Stop (deviceController);

            if (error == ARCONTROLLER_OK)
            {
                // wait state update update
                ARSAL_Sem_Wait (&(stateSem));
            }
        }

        IHM_PrintInfo(ihm, "");
        fprintf(stdout, "ARCONTROLLER_Device_Delete ...");
        ARCONTROLLER_Device_Delete (&deviceController);
    }

    ARSAL_Sem_Destroy (&(stateSem));
    fprintf(stdout, "-- END --");
    close(fLogFile);

    return EXIT_SUCCESS;
}

/*****************************************
 *
 *             private implementation:
 *
 ****************************************/

void writeStrToLog(char* str) {
    if(fLogFile != -1) {
        write(fLogFile, str, strlen(str));
    } 
}

void IHM_PrintMessage_H(IHM_t *ihm, char* str) {
    writeStrToLog(str);
    writeStrToLog("\n");
    IHM_PrintMessage(ihm, str);
}

void IHM_PrintError_H(IHM_t *ihm, char* str) {
    writeStrToLog("ERROR : ");
    writeStrToLog(str);
    writeStrToLog("\n");
    IHM_PrintError(ihm, str);
}

// called when the state of the device controller has changed
void stateChanged (eARCONTROLLER_DEVICE_STATE newState, eARCONTROLLER_ERROR error, void *customData)
{
    sprintf(msg, "    - stateChanged newState: %d .....", newState);
    char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
    IHM_PrintInfo(ihm, msgTemp);

    switch (newState)
    {
    case ARCONTROLLER_DEVICE_STATE_STOPPED:
        ARSAL_Sem_Post (&(stateSem));
        //stop
        gIHMRun = 0;

        break;

    case ARCONTROLLER_DEVICE_STATE_RUNNING:
        ARSAL_Sem_Post (&(stateSem));
        break;

    default:
        break;
    }
}

static void cmdBatteryStateChangedRcv(ARCONTROLLER_Device_t *deviceController, ARCONTROLLER_DICTIONARY_ELEMENT_t *elementDictionary)
{
    ARCONTROLLER_DICTIONARY_ARG_t *arg = NULL;
    ARCONTROLLER_DICTIONARY_ELEMENT_t *singleElement = NULL;

    if (elementDictionary == NULL) {
        sprintf(msg, "elements is NULL");
        char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
        IHM_PrintError_H(ihm, msgTemp);
        return;
    }

    // get the command received in the device controller
    HASH_FIND_STR (elementDictionary, ARCONTROLLER_DICTIONARY_SINGLE_KEY, singleElement);

    if (singleElement == NULL) {
        sprintf(msg, "singleElement is NULL");
        char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
        IHM_PrintError_H(ihm, msgTemp);
        return;
    }

    // get the value
    HASH_FIND_STR (singleElement->arguments, ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_BATTERYSTATECHANGED_PERCENT, arg);

    if (arg == NULL) {
        sprintf(msg, "arg is NULL");
        char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
        IHM_PrintError_H(ihm, msgTemp);
        return;
    }

    // update UI
    batteryStateChanged(arg->value.U8);
}

static void cmdSensorStateListChangedRcv(ARCONTROLLER_Device_t *deviceController, ARCONTROLLER_DICTIONARY_ELEMENT_t *elementDictionary)
{
    ARCONTROLLER_DICTIONARY_ARG_t *arg = NULL;
    ARCONTROLLER_DICTIONARY_ELEMENT_t *dictElement = NULL;
    ARCONTROLLER_DICTIONARY_ELEMENT_t *dictTmp = NULL;

    eARCOMMANDS_COMMON_COMMONSTATE_SENSORSSTATESLISTCHANGED_SENSORNAME sensorName = ARCOMMANDS_COMMON_COMMONSTATE_SENSORSSTATESLISTCHANGED_SENSORNAME_MAX;
    int sensorState = 0;

    if (elementDictionary == NULL) {
        sprintf(msg, "elements is NULL");
        char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
        IHM_PrintError_H(ihm, msgTemp);
        return;
    }

    // get the command received in the device controller
    HASH_ITER(hh, elementDictionary, dictElement, dictTmp) {
        // get the Name
        HASH_FIND_STR (dictElement->arguments, ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_SENSORSSTATESLISTCHANGED_SENSORNAME, arg);
        if (arg != NULL) {
            sprintf(msg, "arg sensorName is NULL");
            char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
            IHM_PrintError_H(ihm, msgTemp);
            continue;
        }

        sensorName = arg->value.I32;

        // get the state
        HASH_FIND_STR (dictElement->arguments, ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_SENSORSSTATESLISTCHANGED_SENSORSTATE, arg);
        if (arg == NULL) {
            sprintf(msg, "arg sensorState is NULL");
            char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
            IHM_PrintError_H(ihm, msgTemp);
            continue;
        }

        sensorState = arg->value.U8;
        sprintf(msg, "sensorName %d ; sensorState: %d", sensorName, sensorState);
        char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
        IHM_PrintMessage_H(ihm, msgTemp);
    }
}

// called when a command has been received from the drone
void commandReceived (eARCONTROLLER_DICTIONARY_KEY commandKey, ARCONTROLLER_DICTIONARY_ELEMENT_t *elementDictionary, void *customData)
{
    ARCONTROLLER_Device_t *deviceController = customData;

    if (deviceController == NULL)
        return;

    // if the command received is a battery state changed
    switch(commandKey) {
    case ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_BATTERYSTATECHANGED:
        cmdBatteryStateChangedRcv(deviceController, elementDictionary);
        break;
    case ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_SENSORSSTATESLISTCHANGED:
        cmdSensorStateListChangedRcv(deviceController, elementDictionary);
        break;
    default:
        break;
    }
}

void batteryStateChanged (uint8_t percent)
{
    // callback of changing of battery level

    if (ihm != NULL)
    {
        IHM_PrintBattery (ihm, percent);
    }
}

// IHM callbacks:

void onInputEvent (eIHM_INPUT_EVENT event, void *customData)
{
    // Manage IHM input events
    ARCONTROLLER_Device_t *deviceController = (ARCONTROLLER_Device_t *)customData;
    eARCONTROLLER_ERROR error = ARCONTROLLER_OK;
    //TODO nouveau code
    if(event != IHM_INPUT_EVENT_NONE && tidAutoPilot != 0) {
        pthread_kill(tidAutoPilot, SIGUSR1);
        tidAutoPilot = 0;
        sprintf(msg, "Emergency landing call, WAIT");
        char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
        IHM_PrintMessage_H(ihm, msgTemp);
        waitForLanding(deviceController);
        sprintf(msg, "Landed, you can control the device or quit");
        char *msgTemp2 = malloc(strlen(msg)+1); strcpy(msgTemp2, msg);
        IHM_PrintMessage_H(ihm, msgTemp2);
    }
    switch (event)
    {
    case IHM_INPUT_EVENT_EXIT:
        IHM_PrintInfo(ihm, "IHM_INPUT_EVENT_EXIT ...");
        gIHMRun = 0;
        break;
    case IHM_INPUT_EVENT_EMERGENCY:
        if(deviceController != NULL)
        {
            // send a Emergency command to the drone
            error = deviceController->aRDrone3->sendPilotingEmergency(deviceController->aRDrone3);
        }
        break;
    case IHM_INPUT_EVENT_LAND:
        if(deviceController != NULL)
        {
            // send a takeoff command to the drone
            error = deviceController->aRDrone3->sendPilotingLanding(deviceController->aRDrone3);
        }
        break;
    case IHM_INPUT_EVENT_TAKEOFF:
        if(deviceController != NULL)
        {
            // send a landing command to the drone
            error = deviceController->aRDrone3->sendPilotingTakeOff(deviceController->aRDrone3);
        }
        break;
    case IHM_INPUT_EVENT_UP:
        if(deviceController != NULL)
        {
            // set the flag and speed value of the piloting command
            error = deviceController->aRDrone3->setPilotingPCMDGaz(deviceController->aRDrone3, 50);
        }
        break;
    case IHM_INPUT_EVENT_DOWN:
        if(deviceController != NULL)
        {
            error = deviceController->aRDrone3->setPilotingPCMDGaz(deviceController->aRDrone3, -50);
        }
        break;
    case IHM_INPUT_EVENT_RIGHT:
        if(deviceController != NULL)
        {
            error = deviceController->aRDrone3->setPilotingPCMDYaw(deviceController->aRDrone3, 50);
        }
        break;
    case IHM_INPUT_EVENT_LEFT:
        if(deviceController != NULL)
        {
            error = deviceController->aRDrone3->setPilotingPCMDYaw(deviceController->aRDrone3, -50);
        }
        break;
    case IHM_INPUT_EVENT_FORWARD:
        if(deviceController != NULL)
        {
            error = deviceController->aRDrone3->setPilotingPCMDPitch(deviceController->aRDrone3, 50);
            error = deviceController->aRDrone3->setPilotingPCMDFlag(deviceController->aRDrone3, 1);
        }
        break;
    case IHM_INPUT_EVENT_BACK:
        if(deviceController != NULL)
        {
            error = deviceController->aRDrone3->setPilotingPCMDPitch(deviceController->aRDrone3, -50);
            error = deviceController->aRDrone3->setPilotingPCMDFlag(deviceController->aRDrone3, 1);
        }
        break;
    case IHM_INPUT_EVENT_ROLL_LEFT:
        if(deviceController != NULL)
        {
            error = deviceController->aRDrone3->setPilotingPCMDRoll(deviceController->aRDrone3, -50);
            error = deviceController->aRDrone3->setPilotingPCMDFlag(deviceController->aRDrone3, 1);
        }
        break;
    case IHM_INPUT_EVENT_ROLL_RIGHT:
        if(deviceController != NULL)
        {
            error = deviceController->aRDrone3->setPilotingPCMDRoll(deviceController->aRDrone3, 50);
            error = deviceController->aRDrone3->setPilotingPCMDFlag(deviceController->aRDrone3, 1);
        }
        break;
    case IHM_INPUT_EVENT_NONE:
        if(deviceController != NULL)
        {
            IHM_PrintInfo(ihm, "IHM_INPUT_NONE ...");
            error = deviceController->aRDrone3->setPilotingPCMD(deviceController->aRDrone3, 0, 0, 0, 0, 0, 0);
        }
        break;
    default:
        break;
    }

    // This should be improved, here it just displays that one error occured
    if (error != ARCONTROLLER_OK)
    {
        sprintf(msg, "Error sending an event : %d", error);
        char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
        IHM_PrintError_H(ihm, msgTemp);
    }
}

int customPrintCallback (eARSAL_PRINT_LEVEL level, const char *tag, const char *format, va_list va)
{
    // Custom callback used when ncurses is runing for not disturb the IHM
    if ((level == ARSAL_PRINT_ERROR) && (strcmp(TAG, tag) == 0))
    {
        // Save the last Error
        vsnprintf(gErrorStr, (ERROR_STR_LENGTH - 1), format, va);
        gErrorStr[ERROR_STR_LENGTH - 1] = '\0';
    }

    return 1;
}            

//NOUVEAU CODE TODO

void waitForLanding(ARCONTROLLER_Device_t *deviceController) {
    while(getFlyingState(deviceController) != ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED) usleep(50);
}

eARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE getFlyingState(ARCONTROLLER_Device_t *deviceController)
{
    eARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE flyingState = ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_MAX;
    eARCONTROLLER_ERROR error;
    ARCONTROLLER_DICTIONARY_ELEMENT_t *elementDictionary = ARCONTROLLER_ARDrone3_GetCommandElements(deviceController->aRDrone3, ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED, &error);
    if (error == ARCONTROLLER_OK && elementDictionary != NULL)
    {
        ARCONTROLLER_DICTIONARY_ARG_t *arg = NULL;
        ARCONTROLLER_DICTIONARY_ELEMENT_t *element = NULL;
        HASH_FIND_STR (elementDictionary, ARCONTROLLER_DICTIONARY_SINGLE_KEY, element);
        if (element != NULL)
        {
            // Get the value
            HASH_FIND_STR(element->arguments, ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE, arg);
            if (arg != NULL)
            {
                // Enums are stored as I32
                flyingState = arg->value.I32;
            }
        }
    }
    return flyingState;
}

void takeOff(ARCONTROLLER_Device_t *deviceController)
{
    if (deviceController == NULL)
    {
        return;
    }
    if (getFlyingState(deviceController) == ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED)
    {
        deviceController->aRDrone3->sendPilotingTakeOff(deviceController->aRDrone3);
    }
}

eARCONTROLLER_ERROR land(ARCONTROLLER_Device_t *deviceController)
{
    if (deviceController == NULL)
    {
        return ARCONTROLLER_ERROR;
    }
    eARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE flyingState = getFlyingState(deviceController);
    if (flyingState == ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_FLYING || flyingState == ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING)
    {
        return deviceController->aRDrone3->sendPilotingLanding(deviceController->aRDrone3);
    }
}

ARCONTROLLER_Device_t *deviceController;

void sigusrHandler(int num) {
    sprintf(msg, "Catch sig %d %li\nStop autopilot\n", num, pthread_self());
    char *msgTemp = malloc(strlen(msg)+1); strcpy(msgTemp, msg);
    IHM_PrintMessage_H(ihm, msgTemp);
    
    if(land(deviceController) != ARCONTROLLER_OK) {
        deviceController->aRDrone3->sendPilotingEmergency(deviceController->aRDrone3);
    }
}

void *customPiloting (void *customData) {
    sleep(5);
    deviceController = (ARCONTROLLER_Device_t *)customData;
    eARCONTROLLER_ERROR error = ARCONTROLLER_OK;
    
    takeOff(deviceController);

    sleep(3);

    error = deviceController->aRDrone3->setPilotingPCMDPitch(deviceController->aRDrone3, 50);
    error = deviceController->aRDrone3->setPilotingPCMDFlag(deviceController->aRDrone3, 1);
    /*sleep(2);
    error = deviceController->aRDrone3->setPilotingPCMDPitch(deviceController->aRDrone3, 0);
    error = deviceController->aRDrone3->setPilotingPCMDFlag(deviceController->aRDrone3, 1);
    sleep(3);
    error = deviceController->aRDrone3->setPilotingPCMDPitch(deviceController->aRDrone3, -50);
    error = deviceController->aRDrone3->setPilotingPCMDFlag(deviceController->aRDrone3, 1);
    sleep(2);

    deviceController->aRDrone3->setPilotingPCMDFlag(deviceController->aRDrone3, 0);
    deviceController->aRDrone3->setPilotingPCMDPitch(deviceController->aRDrone3, 0);
*/
      // sleep(1);
      //Make the drone rotate to the right (50% of its max rotation speed)
      // deviceController->aRDrone3->setPilotingPCMDYaw(deviceController->aRDrone3, 25);


      sleep(10);

      if(land(deviceController) != ARCONTROLLER_OK) {
        deviceController->aRDrone3->sendPilotingEmergency(deviceController->aRDrone3);
      }

}
