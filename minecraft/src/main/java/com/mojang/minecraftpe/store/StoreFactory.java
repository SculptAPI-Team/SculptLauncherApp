package com.mojang.minecraftpe.store;

import com.mojang.minecraftpe.MainActivity;
import com.mojang.minecraftpe.store.amazonappstore.AmazonAppStore;
import com.mojang.minecraftpe.store.googleplay.GooglePlayStore;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class StoreFactory {
    static Store createAmazonAppStore(StoreListener storeListener) {
        return new AmazonAppStore(MainActivity.mInstance, storeListener);
    }

    static Store createGooglePlayStore(String str, StoreListener storeListener) {
        return new GooglePlayStore(MainActivity.mInstance, str, storeListener);
    }
}