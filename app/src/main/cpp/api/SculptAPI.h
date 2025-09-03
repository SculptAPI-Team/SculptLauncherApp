//
// Created by 微晞鸢徊 on 2025/9/3.
//

#ifndef SCULPTLAUNCHER_SCULPTAPI_H
#define SCULPTLAUNCHER_SCULPTAPI_H

#include <jni.h>
#include <cxxabi.h>
#include "../header/app.hpp"

namespace SculptAPI {
//    void nativeSetDataDirectory(JNIEnv*env,jobject thiz,jstring directory) {
//        *mAddrAndroidAppDataPath = toString(env,directory);
//    }
    jstring nativeDemangle(JNIEnv *env, jobject thiz, jstring str) {
        char const *symbol_name = toString(env, str).c_str();
        if (symbol_name) {
            char const *ret = abi::__cxa_demangle(symbol_name, nullptr, nullptr, nullptr);
            return env->NewStringUTF(ret);
        }
        return env->NewStringUTF("");
    }
}

#endif //SCULPTLAUNCHER_SCULPTAPI_H
