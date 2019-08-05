#include <jni.h>
#include <string.h>

jstring Java_ga_fliptech_imageeditor_MainActivity_StringFromJNI(JNIEnv* env, jobject thiz) {
	return (*env)->NewStringUTF(env, "Hello World, Hello JNI");
}
