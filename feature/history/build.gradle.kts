plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.library.compose)
    alias(libs.plugins.youhajun.android.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.youhajun.feature.history"
}

dependencies {
    implementation(projects.feature.history.api)
    implementation(projects.domain.user)
    implementation(projects.domain.history)
    implementation(projects.domain.conversation)
    implementation(projects.core.ui)
    implementation(projects.core.model)
    implementation(projects.core.design)
    implementation(projects.core.route)
    implementation(libs.bundles.compose.default)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.workmanager)
    implementation(libs.bundles.orbit)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.kotlinx.collections.immutable)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}