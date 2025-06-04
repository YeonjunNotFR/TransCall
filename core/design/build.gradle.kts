plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.library.compose)
}

android {
    namespace = "com.youhajun.core.design"
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}