plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.youhajun.feature.home.api"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.core.route)
}