#include <jni.h>
#include <dlfcn.h>

//#include <game-activity/GameActivity.cpp>
//#include <game-text-input/gametextinput.cpp>
//#include <game-activity/native_app_glue/android_native_app_glue.c>
#include <game-activity/native_app_glue/android_native_app_glue.h>

void (*mMainFunc)(struct android_app*) = nullptr;

extern "C" {
    void android_main(struct android_app* state) {
        mMainFunc(state);
    }
}

//void android_main(struct android_app* app) {
//    mMainFunc(app);
//}

extern "C" JNIEXPORT void JNICALL Java_org_thelauncher_sculptlauncher_backend_launcher_util_LibraryLoader_nativeOnLauncherLoaded(
        JNIEnv* env, jobject thiz, jstring libPath) {
    const char * mNativeLibPath = env->GetStringUTFChars(libPath, 0);
    void* image = dlopen(mNativeLibPath, RTLD_LAZY);
    mMainFunc = (void(*)(struct android_app*)) dlsym(image, "android_main");
    dlclose(image);
    env->ReleaseStringUTFChars(libPath, mNativeLibPath);
}