//
// Created by 微晞鸢徊 on 2025/9/2.
//

#ifndef SCULPTLAUNCHER_APP_HPP
#define SCULPTLAUNCHER_APP_HPP
#include "string"
#include "jni.h"

std::string toString(JNIEnv* env, jstring j_str) {
    //DO NOT RELEASE.
    const char * c_str = env->GetStringUTFChars(j_str, nullptr);
    std::string cpp_str = c_str;
    return cpp_str;
}

#endif //SCULPTLAUNCHER_APP_HPP
