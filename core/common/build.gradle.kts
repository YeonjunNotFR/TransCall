plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.hilt)
}

android {
    namespace = "com.youhajun.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}