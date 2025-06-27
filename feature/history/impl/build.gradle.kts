plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.library.compose)
    alias(libs.plugins.youhajun.android.hilt)
}

android {
    namespace = "com.youhajun.feature.history.impl"
}

dependencies {
    implementation(projects.feature.history.api)
    implementation(projects.domain.history)
    implementation(projects.core.ui)
    implementation(projects.core.model)
    implementation(projects.core.design)
    implementation(projects.core.route)
    implementation(libs.bundles.navigation)
    implementation(libs.androidx.compose.constraintlayout)
    implementation(libs.bundles.orbit)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}