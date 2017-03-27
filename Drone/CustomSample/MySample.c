/*
 * include
 */
#include <stdlib.h>
#include <curses.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <errno.h>

#include <libARSAL/ARSAL.h>
#include <libARController/ARController.h>
#include <libARDiscovery/ARDiscovery.h>

#include "MySample.h"
#include "ihm.h"
#include "lib.h"

/*
 * define
 */
#define TAG "MySample"

#define ERROR_STR_LENGTH 2048

#define BEBOP_IP_ADDRESS "192.168.42.1"
#define BEBOP_DISCOVERY_PORT 44444

#define DISPLAY_WITH_MPLAYER 1

#define FIFO_DIR_PATTERN "/tmp/arsdk_XXXXXX"
#define FIFO_NAME "arsdk_fifo"

#define IHM

/*
 * implementation
 */

static char fifo_dir[] = FIFO_DIR_PATTERN;
static char fifo_name[128] = "";

int gIHMRun = 1;
char gErrorStr[ERROR_STR_LENGTH];
IHM_t *ihm = NULL;

FILE *videoOut = NULL;
int frameNb = 0;
ARSAL_Sem_t stateSem;
int isBebop2 = 0;

int main(int argc, char **argv) {
  // local declarations
  int failed = 0;
  ARDISCOVERY_Device_t *device = NULL;
  ARCONTROLLER_Device_t *deviceController = NULL;
  eARCONTROLLER_ERROR error = ARCONTROLLER_OK;
  eARCONTROLLER_DEVICE_STATE deviceState = ARCONTROLLER_DEVICE_STATE_MAX;
  pid_t child = 0;

  printf("Discovering bebop2 drone...\n");
  device = createDiscoveryDevice(ARDISCOVERY_PRODUCT_BEBOP_2, "bebop2", BEBOP_IP_ADDRESS, BEBOP_DISCOVERY_PORT);
  // just in case
  if (device == NULL) {
    printf("Not found!\n");
    return EXIT_FAILURE;
  }

  // device controller
  deviceController = ARCONTROLLER_Device_New (device, &error);

  error = ARCONTROLLER_Device_Start(deviceController);

  printf("Press <Enter> when you are ready !\n");
  getchar();
  printf("Starting...\n");

  // Start
  takeOff(deviceController);

  sleep(3);

  //Make the drone moves forward (50% of its max angle)
  deviceController->aRDrone3->setPilotingPCMDFlag(deviceController->aRDrone3, 1);
  deviceController->aRDrone3->setPilotingPCMDPitch(deviceController->aRDrone3, 50);

  sleep(3);
  //Make the drone rotate to the right (50% of its max rotation speed)
  deviceController->aRDrone3->setPilotingPCMDYaw(deviceController->aRDrone3, 50);

  sleep(3);

  land(deviceController);

  printf("- END -");
  // deleteDeviceController(deviceController);

  return EXIT_SUCCESS;
}