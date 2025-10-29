plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.library.compose)
    alias(libs.plugins.youhajun.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.youhajun.feature.call.api"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.route)
    implementation(projects.core.webRtc)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
}