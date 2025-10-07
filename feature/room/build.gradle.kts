plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.library.compose)
    alias(libs.plugins.youhajun.android.hilt)
}

android {
    namespace = "com.youhajun.feature.room"
}

dependencies {
    implementation(projects.feature.room.api)
    implementation(projects.feature.call.api)
    implementation(projects.domain.room)
    implementation(projects.domain.user)
    implementation(projects.core.ui)
    implementation(projects.core.permission)
    implementation(projects.core.design)
    implementation(projects.core.model)
    implementation(projects.core.route)
    implementation(libs.bundles.compose.default)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.orbit)
    implementation(libs.kotlinx.collections.immutable)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}