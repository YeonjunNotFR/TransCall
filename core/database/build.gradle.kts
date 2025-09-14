plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.youhajun.core.database"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.bundles.dataStore)
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
    implementation(libs.timber)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}