#include <dlfcn.h>
#include "game-activity/native_app_glue/android_native_app_glue.h"
#include "header/app.hpp"


void (*mMainFunc)(struct android_app *) = nullptr;

extern "C" {
void android_main(struct android_app *state) {
    mMainFunc(state);
}
}

extern "C" JNIEXPORT void JNICALL
Java_org_sculptlauncher_app_backend_launcher_util_LibraryLoader_nativeOnLauncherLoaded(
        JNIEnv *env, jobject thiz, jstring libPath) {
    const char *mNativeLibPath = env->GetStringUTFChars(libPath, nullptr);
    void *image = dlopen(mNativeLibPath, RTLD_LAZY);
    mMainFunc = (void (*)(struct android_app *)) dlsym(image, "android_main");
    dlclose(image);
    env->ReleaseStringUTFChars(libPath, mNativeLibPath);
}