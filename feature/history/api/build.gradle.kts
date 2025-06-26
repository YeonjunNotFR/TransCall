plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.youhajun.feature.history.api"
}

dependencies {
    implementation(projects.core.route)
    implementation(libs.kotlinx.serialization.json)
}