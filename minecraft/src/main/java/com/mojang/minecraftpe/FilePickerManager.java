package com.mojang.minecraftpe;

import android.content.Intent;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/FilePickerManager.class */
public class FilePickerManager implements ActivityListener {
    static final int PICK_DIRECTORY_REQUEST_CODE = 246242755;
    FilePickerManagerHandler mHandler;

    public FilePickerManager(FilePickerManagerHandler filePickerManagerHandler) {
        this.mHandler = filePickerManagerHandler;
    }

    private static native void nativeDirectoryPickResult(String str, String str2);

    @Override // com.mojang.minecraftpe.ActivityListener
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == PICK_DIRECTORY_REQUEST_CODE) {
            if (i2 == -1) {
                nativeDirectoryPickResult(intent.getData().toString(), "");
            } else {
                nativeDirectoryPickResult("", "No directory selected");
            }
        }
    }

    @Override // com.mojang.minecraftpe.ActivityListener
    public void onDestroy() {
    }

    @Override // com.mojang.minecraftpe.ActivityListener
    public void onResume() {
    }

    @Override // com.mojang.minecraftpe.ActivityListener
    public void onStop() {
    }

    public void pickDirectory(String str, String str2) {
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
        if (str != null && !str.isEmpty()) {
            intent.putExtra("android.provider.extra.PROMPT", str);
        }
        if (str2 != null && !str2.isEmpty()) {
            intent.putExtra("android.provider.extra.INITIAL_URI", str2);
        }
        this.mHandler.startPickerActivity(intent, PICK_DIRECTORY_REQUEST_CODE);
    }
}
