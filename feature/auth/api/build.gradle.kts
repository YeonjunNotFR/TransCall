plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.library.compose)
    alias(libs.plugins.youhajun.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.youhajun.feature.auth.api"
}

dependencies {
    implementation(projects.core.route)
    implementation(projects.core.model)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
}