# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Preserve line number information for debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# === Hilt ===
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelMap { *; }
-keepclassmembers @dagger.hilt.android.lifecycle.HiltViewModel class * {
    @javax.inject.Inject <fields>;
}
-dontwarn dagger.hilt.**

# === Firebase ===
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# === Retrofit & OkHttp ===
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**

# === Gson ===
-keep class com.google.gson.** { *; }
-keep class com.google.gson.reflect.TypeToken { *; }
-keepclassmembers class * extends com.google.gson.reflect.TypeToken { *; }
# Keep DTOs used by Gson
-keep class com.lodrean.network.** { *; }
-keep class com.lodrean.model.** { *; }

# === Kotlinx Serialization ===
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
    @kotlinx.serialization.Serializable <fields>;
}
-keep class * extends kotlinx.serialization.KSerializer { *; }

# === Room ===
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-dontwarn androidx.room.paging.**

# === Navigation ===
-keep class * extends androidx.navigation.NavArgs { *; }
-keep class androidx.navigation.** { *; }

# === Glide ===
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule { <init>(...); }
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# === Parcelable ===
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# === Keep domain models across all modules ===
-keep class com.lodrean.search.domain.** { *; }
-keep class com.lodrean.auth.domain.** { *; }
-keep class com.lodrean.account.domain.** { *; }
-keep class com.lodrean.favorites.domain.** { *; }

# === Keep ViewModels and Fragments (Hilt + Navigation) ===
-keep class * extends androidx.lifecycle.ViewModel { <init>(...); }
-keep class * extends androidx.fragment.app.Fragment { <init>(...); }
-keep class * extends android.app.Activity { <init>(...); }

# === Remove logging in release ===
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}
