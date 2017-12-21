# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/cylee/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class cn.csnbgsh.herbarium.getName to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

-keep public class com.baidu.crabsdk.**
-keepclassmembers public class com.baidu.crabsdk.*{
    *;
}
#Native Crash收集请加入如下配置
-keepclassmembers public class com.baidu.crabsdk.sender.NativeCrashHandler{
    *;
}
