package com.mojang.minecraftpe.python;

import android.content.res.AssetManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/python/PythonPackageLoader.class */
public class PythonPackageLoader {
    private AssetManager assetManager;
    private File destination;

    /* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/python/PythonPackageLoader$CreateDirectory.class */
    enum CreateDirectory {
        Created,
        Exists
    }

    public PythonPackageLoader(AssetManager assetManager, File file) {
        this.assetManager = assetManager;
        this.destination = new File(file + "/python");
    }

    private final void copy(InputStream inputStream, File file) throws Throwable {
        File parentFile = file.getParentFile();
        parentFile.getClass();
        createDirectory(parentFile);
        Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Log.i("MCPE", "Created " + file.getAbsolutePath());
    }

    private CreateDirectory createDirectory(File file) throws Throwable {
        if (file.exists()) {
            return CreateDirectory.Exists;
        }
        if (file.mkdirs()) {
            return CreateDirectory.Created;
        }
        throw new IOException("Failed to mkdir " + file.getAbsolutePath());
    }

    private void delete(File file) {
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                delete(file2);
            }
        }
        file.delete();
    }

    private boolean shouldUnpack() throws Throwable {
        File file = new File(this.destination.getAbsolutePath() + "/python-tracker.txt");
        String[] list = this.assetManager.list("python-tracker.txt");
        if (!file.exists() || list == null || list.length <= 0) {
            return true;
        }
        return !new BufferedReader(new FileReader(file)).readLine().equals(new BufferedReader(new InputStreamReader(this.assetManager.open(list[0]), StandardCharsets.UTF_8)).readLine());
    }

    private void traverse(File file) {
        if (file.exists()) {
            for (File file2 : file.listFiles()) {
                if (file2.isDirectory()) {
                    traverse(file2);
                } else {
                    Log.i("MCPE", "Python stdLib file: '" + file2.getAbsolutePath() + "'");
                }
            }
        }
    }

    private final void unpackAssetPath(String str, int i, File file) throws Throwable {
        String[] list = this.assetManager.list(String.valueOf(str));
        if (list == null) {
            throw new IOException("Unable to list assets at path " + str + '/');
        }
        if (list.length == 0) {
            copy(this.assetManager.open(str), new File(file.getAbsolutePath() + '/' + str.substring(i)));
            return;
        }
        for (String str2 : list) {
            unpackAssetPath(str + '/' + str2, i, file);
        }
    }

    private void unpackAssetPrefix(String str, File file) throws Throwable {
        Log.i("MCPE", "Clearing out path " + file.getAbsolutePath());
        delete(file);
        String[] list = this.assetManager.list(str);
        if (list == null || list.length == 0) {
            throw new IOException("No assets at prefix " + str);
        }
        for (String str2 : list) {
            unpackAssetPath(str + '/' + str2, str.length(), file);
        }
    }

    public void unpack() {
        try {
            if (createDirectory(this.destination) == CreateDirectory.Exists && !shouldUnpack()) {
                Log.i("PythonPackageLoader", "Nothing to do here. File versions checkout out. returning.");
            } else {
                Log.i("PythonPackageLoader", "Attempting to copy over python files to data.");
                unpackAssetPrefix("assets/python", this.destination);
            }
        } catch (Throwable th) {
            Log.e("PythonPackageLoader", th.toString());
        }
    }
}
