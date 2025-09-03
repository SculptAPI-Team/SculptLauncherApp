package com.microsoft.xbox.idp.util;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.microsoft.xbox.idp.toolkit.BitmapLoader;

public class BitmapLoaderCache implements BitmapLoader.Cache {
    private final LruCache<Object, Bitmap> cache;

    public BitmapLoaderCache(int i) {
        this.cache = new LruCache<>(i);
    }

    @Override // com.microsoft.xbox.idp.toolkit.BitmapLoader.Cache
    public Bitmap get(Object obj) {
        return this.cache.get(obj);
    }

    @Override // com.microsoft.xbox.idp.toolkit.BitmapLoader.Cache
    public Bitmap put(Object obj, Bitmap bitmap) {
        return this.cache.put(obj, bitmap);
    }

    @Override // com.microsoft.xbox.idp.toolkit.BitmapLoader.Cache
    public Bitmap remove(Object obj) {
        return this.cache.remove(obj);
    }

    @Override // com.microsoft.xbox.idp.toolkit.BitmapLoader.Cache
    public void clear() {
        this.cache.evictAll();
    }
}
