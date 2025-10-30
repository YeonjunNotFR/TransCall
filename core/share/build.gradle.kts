plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.youhajun.core.share"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.appcompat)
}