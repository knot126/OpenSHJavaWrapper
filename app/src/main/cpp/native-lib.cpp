#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_mediocre_smashhit_MainActivity_stringFromJNI(JNIEnv* env, jobject obj) {
    return env->NewStringUTF("You need to replace this file with libsmashhit.so!");
}