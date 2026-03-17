# ============================================
# LastRegrets ProGuard Rules
# ============================================

# --- Firebase ---
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# --- Room ---
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# --- Data models (used by Firestore reflection) ---
-keep class com.lastregrets.data.model.** { *; }
-keep class com.lastregrets.data.remote.CategoryStatRemote { *; }

# --- Kotlin ---
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-dontwarn kotlinx.coroutines.**

# --- DataStore ---
-keep class androidx.datastore.** { *; }
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {
    <fields>;
}

# --- Compose ---
-dontwarn androidx.compose.**
