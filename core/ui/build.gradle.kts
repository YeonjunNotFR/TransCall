plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.library.compose)
}

android {
    namespace = "com.youhajun.core.ui"
}

dependencies {
    implementation(projects.core.design)
    implementation(projects.core.model)
    implementation(libs.bundles.compose.default)
    implementation(libs.bundles.webRtc)
    implementation(libs.androidx.paging.compose)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.coil)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}