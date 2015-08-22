# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/oguzemreozcan/Library/Developer/Xamarin/android-sdk-macosx/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:
#-injars      bin/classes
#-injars      libs
#-outjars     bin/classes-processed.jar
#-libraryjars libs/android-support-v4.jar
#-libraryjars /usr/local/java/android-sdk/platforms/android-9/android.jar
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#
-libraryjars libs

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-optimizationpasses 5
-verbose
-dontpreverify
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontusemixedcaseclassnames
#-repackageclasses ''
-allowaccessmodification
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-allowaccessmodification
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-repackageclasses ''

-dontwarn android.support.**
-dontwarn com.makeramen.**
-dontwarn java.nio.file.**
-dontwarn org.codehaus.mojo.**
-dontwarn com.jeremyfeinstein.**
-dontwarn com.makeramen.**
-dontwarn android.support.v7.**
-dontwarn org.joda.**
-dontwarn com.squareup.okhttp.**
-dontwarn com.makeramen.roundedimageview.RoundedImageView

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keep class com.makeramen.** {*;}
-keep interface com.makeramen.** { *; }
-keep class com.makeramen.roundedimageview.RoundedImageView
-keep interface com.jeremyfeinstein.** {*;}
-keep class com.jeremyfeinstein.** {*;}
-keep class com.crashlytics.** { *; }
-keep interface com.nostra13.**{*;}
-keep class com.nostra13.**{*;}
-keep interface com.squareup.okhttp.**{*;}
-keep class com.squareup.okhttp.**{*;}
-keep class eu.janmuller.android.simplecropimage.**{*;}
-keep interface eu.janmuller.android.simplecropimage.**{*;}
-keep class com.birin.gridlistviewadapters.**{*;}
-keep interface com.birin.gridlistviewadapters.**{*;}
-keep class android.app.**{*;}
-keep class android.transition.**{*;}
-keep public class com.mallardduckapps.fashiontalks.FashionTalksApp

#-keep class com.mallardduckapps.fashiontalks.GalleriesFragment
#-keep public class com.mallardduckapps.fashiontalks.LoginActivity
#-keep class * extends com.mallardduckapps.fashiontalks.BaseActivity

-keepclasseswithmembernames class android.support.v4.app.Fragment {
    android.support.v4.app.FragmentManagerImpl mChildFragmentManager;
}
-keep class * extends android.support.v4.app.Fragment
-keep class * extends android.app.Activity
-keep class * extends android.support.v7.app.ActionBarActivity
-keep class * extends java.lang.Throwable
-keep class android.support.v7.widget.** { *; }
-keep class * implements java.io.Serializable { *; }
-keep class com.facebook.** { *; }
-keepattributes Signature
-keep class org.joda.** { *; }
-keep interface org.joda.** { *; }
-keep class android.support.** { *; }
-keep interface android.support.** { *; }


-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}

# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class * {
    public protected *;
}

-keepclassmembers class * implements java.io.Serializable{
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

##---------------End: proguard configuration for Gson  ----------


-dontwarn com.squareup.okhttp.**
-dontwarn com.google.appengine.api.urlfetch.**
-dontwarn rx.**
-dontwarn retrofit.**
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}