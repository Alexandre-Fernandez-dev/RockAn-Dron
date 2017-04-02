ifeq ("$(TARGET_OS_FLAVOUR)","native")

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_CATEGORY_PATH := RADDrone
LOCAL_MODULE := RADDrone
LOCAL_DESCRIPTION := RADDrone Test

LOCAL_LIBRARIES := \
	libARSAL \
	libARController \
	libARDataTransfer \
	libARUtils \
	libARCommands \
	libARNetwork \
	libARNetworkAL \
	libARDiscovery \
	libARStream \
	libARStream2 \
	ncurses

LOCAL_SRC_FILES := \
	$(call all-c-files-under,.)

include $(BUILD_EXECUTABLE)

endif
