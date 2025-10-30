plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.library.compose)
    alias(libs.plugins.youhajun.android.hilt)
}

android {
    namespace = "com.youhajun.feature.call"
}

dependencies {
    implementation(projects.feature.call.api)
    implementation(projects.domain.calling)
    implementation(projects.domain.room)
    implementation(projects.domain.conversation)
    implementation(projects.domain.user)
    implementation(projects.core.share)
    implementation(projects.core.common)
    implementation(projects.core.webRtc)
    implementation(projects.core.ui)
    implementation(projects.core.model)
    implementation(projects.core.design)
    implementation(projects.core.permission)
    implementation(projects.core.notification)
    implementation(projects.core.route)
    implementation(libs.bundles.compose.default)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.orbit)
    implementation(libs.bundles.webRtc)
    implementation(libs.timber)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}