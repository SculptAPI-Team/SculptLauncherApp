//
// Created by 微晞鸢徊 on 2025/9/3.
//
#include "jni.h"
#include "../api/SculptAPI.h"
#include <string>
#include <dlfcn.h>

JavaVM *mJvm = nullptr;
//std::string* mAddrAndroidAppDataPath = nullptr;
std::string mMCPENativeLibPath;

extern "C" JNIEXPORT jboolean JNICALL
Java_org_sculptlauncher_app_backend_launcher_util_NativeUtil_nativeRegisterNatives(JNIEnv *env,
                                                                                   jobject thiz,
                                                                                   jclass cls) {
    JNINativeMethod methods[] = {
            //{"nativeSetDataDirectory", "(Ljava/lang/String;)V", (void *)&NModAPI::nativeSetDataDirectory},
            {"nativeDemangle", "(Ljava/lang/String;)Ljava/lang/String;",
             (void *) &SculptAPI::nativeDemangle}
    };

    if (env->RegisterNatives(cls, methods, sizeof(methods) / sizeof(JNINativeMethod)) < 0)
        return JNI_FALSE;
    return JNI_TRUE;
}

extern "C" JNIEXPORT void JNICALL
Java_org_sculptlauncher_app_backend_launcher_util_LibraryLoader_nativeOnNModAPILoaded(JNIEnv *env,
                                                                                      jobject thiz,
                                                                                      jstring libPath) {
    const char *mNativeLibPath = toString(env, libPath).c_str();
    mMCPENativeLibPath = mNativeLibPath;
    void *imageMCPE = (void *) dlopen(mNativeLibPath, RTLD_LAZY);
    //mAddrAndroidAppDataPath = ((std::string*)dlsym(imageMCPE,"_ZN19AppPlatform_android20ANDROID_APPDATA_PATHE"));
    dlclose(imageMCPE);
}

JNIEXPORT jint JNI_OnLoad(JavaVM*vm,void*) {
    mJvm=vm;
    return JNI_VERSION_1_6;
}