plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.library.compose)
    alias(libs.plugins.youhajun.android.hilt)
}

android {
    namespace = "com.youhajun.core.webRtc"
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.androidx.core.ktx)
    implementation(libs.audio.switch)
    implementation(libs.bundles.webRtc)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}