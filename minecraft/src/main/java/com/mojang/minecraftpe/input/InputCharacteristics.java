package com.mojang.minecraftpe.input;

import android.view.InputDevice;
import java.io.File;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/input/InputCharacteristics.class */
public class InputCharacteristics {
    private static final int DUALSENSE_DEVICE_ID = 3302;
    private static final int SONY_VENDOR_ID = 1356;

    public static boolean allControllersHaveDoubleTriggers() {
        boolean z;
        boolean z2;
        int[] deviceIds = InputDevice.getDeviceIds();
        int i = 0;
        boolean z3 = false;
        while (true) {
            boolean z4 = z3;
            z = z4;
            if (i >= deviceIds.length) {
                break;
            }
            InputDevice device = InputDevice.getDevice(deviceIds[i]);
            boolean z5 = z4;
            if (device != null) {
                z5 = z4;
                if (device.isVirtual()) {
                    continue;
                } else {
                    z5 = z4;
                    if (device.getControllerNumber() > 0) {
                        z5 = z4;
                        if ((device.getSources() & 1025) != 0) {
                            boolean[] zArrHasKeys = device.hasKeys(102, 103, 104, 105);
                            boolean z6 = zArrHasKeys.length == 4;
                            int i2 = 0;
                            while (true) {
                                z2 = z6;
                                if (i2 >= zArrHasKeys.length) {
                                    break;
                                }
                                if (!zArrHasKeys[i2]) {
                                    z2 = false;
                                    break;
                                }
                                i2++;
                            }
                            boolean zSupportsAnalogTriggers = z2;
                            if (!z2) {
                                zSupportsAnalogTriggers = z2;
                                if (zArrHasKeys[0]) {
                                    zSupportsAnalogTriggers = z2;
                                    if (zArrHasKeys[1]) {
                                        zSupportsAnalogTriggers = supportsAnalogTriggers(device);
                                    }
                                }
                            }
                            boolean z7 = (zSupportsAnalogTriggers && device.getName().contains("EI-GP20")) ? false : zSupportsAnalogTriggers;
                            z5 = z7;
                            if (!z7) {
                                z = z7;
                                break;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
            }
            i++;
            z3 = z5;
        }
        return z;
    }

    public static boolean isCreteController(int i) {
        InputDevice device = InputDevice.getDevice(i);
        if (device == null || device.isVirtual() || device.getControllerNumber() <= 0 || (device.getSources() & 1025) == 0) {
            return false;
        }
        if (!(device.getProductId() == 736) || !(device.getVendorId() == 1118)) {
            return false;
        }
        for (int i2 = 0; i2 < 2; i2++) {
            if (new File(new String[]{"/system/usr/keylayout/Vendor_045e_Product_02e0.kl", "/data/system/devices/keylayout/Vendor_045e_Product_02e0.kl"}[i2]).exists()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDualsenseController(int i) {
        InputDevice device = InputDevice.getDevice(i);
        return device != null && (device.getSources() & 1025) == 1025 && device.getVendorId() == SONY_VENDOR_ID && device.getProductId() == DUALSENSE_DEVICE_ID;
    }

    public static boolean isPlaystationController(int i) {
        InputDevice device = InputDevice.getDevice(i);
        return device != null && (device.getSources() & 1025) == 1025 && device.getVendorId() == SONY_VENDOR_ID;
    }

    public static boolean isXboxController(int i) {
        InputDevice device = InputDevice.getDevice(i);
        return device != null && (device.getSources() & 1025) == 1025 && device.getVendorId() == 1118;
    }

    public static boolean supportsAnalogTriggers(int i) {
        InputDevice device = InputDevice.getDevice(i);
        if (device != null) {
            return supportsAnalogTriggers(device);
        }
        return false;
    }

    private static boolean supportsAnalogTriggers(InputDevice inputDevice) {
        boolean z = (inputDevice.getMotionRange(17) == null && inputDevice.getMotionRange(23) == null) ? false : true;
        boolean z2 = (inputDevice.getMotionRange(18) == null && inputDevice.getMotionRange(22) == null) ? false : true;
        boolean z3 = false;
        if (z) {
            z3 = false;
            if (z2) {
                z3 = true;
            }
        }
        return z3;
    }
}
