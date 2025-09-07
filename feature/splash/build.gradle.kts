plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.library.compose)
    alias(libs.plugins.youhajun.android.hilt)
}

android {
    namespace = "com.youhajun.feature.splash"
}

dependencies {
    implementation(projects.feature.splash.api)
    implementation(projects.core.ui)
    implementation(projects.core.design)
    implementation(projects.core.model)
    implementation(projects.core.route)
    implementation(libs.bundles.compose.default)
    implementation(libs.bundles.navigation)
    implementation(libs.kotlinx.collections.immutable)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}