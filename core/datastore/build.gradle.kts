plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.hilt)
}

android {
    namespace = "com.youhajun.core.datastore"
}

dependencies {

    implementation(libs.bundles.dataStore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}