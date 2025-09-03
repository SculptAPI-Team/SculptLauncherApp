package com.mojang.minecraftpe.input;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Handler;

import org.jetbrains.annotations.NotNull;

@TargetApi(16)
public class JellyBeanDeviceManager extends InputDeviceManager implements InputManager.InputDeviceListener {
    private final InputManager inputManager;
    native void onInputDeviceAddedNative(int i);
    native void onInputDeviceChangedNative(int i);
    native void onInputDeviceRemovedNative(int i);
    native void setControllerDetailsNative(int i, boolean z, boolean z2);

    native void setDoubleTriggersSupportedNative(boolean z);

    native void setFoundDualsenseControllerNative(boolean z);

    native void setFoundPlaystationControllerNative(boolean z);

    native void setFoundXboxControllerNative(boolean z);

    @SuppressLint("WrongConstant")
    JellyBeanDeviceManager(@NotNull Context ctx) {
        inputManager = (InputManager) ctx.getSystemService("input");
    }


    public void register() {
//        int[] ids = inputManager.getInputDeviceIds();
//        inputManager.registerInputDeviceListener(this, (Handler) null);
//        setDoubleTriggersSupportedNative(InputCharacteristics.allControllersHaveDoubleTriggers());
//        for (int i = 0; i < ids.length; i++) {
//            setCreteControllerNative(ids[i], InputCharacteristics.isCreteController(ids[i]));
//        }
        int[] inputDeviceIds = this.inputManager.getInputDeviceIds();
        this.inputManager.registerInputDeviceListener(this, null);
        setDoubleTriggersSupportedNative(InputCharacteristics.allControllersHaveDoubleTriggers());
        for (int i = 0; i < inputDeviceIds.length; i++) {
            int i2 = inputDeviceIds[i];
            setControllerDetailsNative(i2, InputCharacteristics.isCreteController(i2), InputCharacteristics.supportsAnalogTriggers(inputDeviceIds[i]));
        }
        checkForXboxAndPlaystationController();
    }

    public void unregister() {
        inputManager.unregisterInputDeviceListener(this);
    }

    public void onInputDeviceAdded(int deviceId) {
        onInputDeviceAddedNative(deviceId);
        setDoubleTriggersSupportedNative(InputCharacteristics.allControllersHaveDoubleTriggers());
        //setCreteControllerNative(deviceId, InputCharacteristics.isCreteController(deviceId));
    }

    public void onInputDeviceChanged(int deviceId) {
        onInputDeviceChangedNative(deviceId);
        setDoubleTriggersSupportedNative(InputCharacteristics.allControllersHaveDoubleTriggers());
        //setCreteControllerNative(deviceId, InputCharacteristics.isCreteController(deviceId));
    }

    public void onInputDeviceRemoved(int deviceId) {
        onInputDeviceRemovedNative(deviceId);
        setDoubleTriggersSupportedNative(InputCharacteristics.allControllersHaveDoubleTriggers());
        //setCreteControllerNative(deviceId, InputCharacteristics.isCreteController(deviceId));
    }

    public void checkForXboxAndPlaystationController() {
        boolean z;
        boolean z2;
        boolean z3;
        int[] inputDeviceIds = this.inputManager.getInputDeviceIds();
        int length = inputDeviceIds.length;
        int i = 0;
        boolean zIsXboxController = false;
        boolean zIsPlaystationController = false;
        boolean zIsDualsenseController = false;
        while (true) {
            z = zIsXboxController;
            z2 = zIsPlaystationController;
            z3 = zIsDualsenseController;
            if (i >= length) {
                break;
            }
            int i2 = inputDeviceIds[i];
            zIsXboxController |= InputCharacteristics.isXboxController(i2);
            zIsPlaystationController |= InputCharacteristics.isPlaystationController(i2);
            zIsDualsenseController |= InputCharacteristics.isDualsenseController(i2);
            if (zIsXboxController && zIsPlaystationController) {
                z = zIsXboxController;
                z2 = zIsPlaystationController;
                z3 = zIsDualsenseController;
                break;
            }
            i++;
        }
        setFoundXboxControllerNative(z);
        setFoundPlaystationControllerNative(z2);
        setFoundDualsenseControllerNative(z3);
    }
}