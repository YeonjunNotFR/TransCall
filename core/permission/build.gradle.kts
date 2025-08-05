plugins {
    alias(libs.plugins.youhajun.android.library.compose)
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.hilt)
}

android {
    namespace = "com.youhajun.core.permission"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.design)
    implementation(projects.core.ui)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
}