#ifndef _LIB_H_
#define _LIB_H_

#include <libARSAL/ARSAL.h>
#include <libARController/ARController.h>
#include <libARDiscovery/ARDiscovery.h>

ARDISCOVERY_Device_t* createDiscoveryDevice(eARDISCOVERY_PRODUCT product, const char *name, const char *ip, int port);
// void deleteDeviceController(ARCONTROLLER_Device_t *deviceController);
eARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE getFlyingState(ARCONTROLLER_Device_t *deviceController);
void takeOff(ARCONTROLLER_Device_t *deviceController);
void land(ARCONTROLLER_Device_t *deviceController);

#endif