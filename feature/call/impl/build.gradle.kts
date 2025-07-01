plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.library.compose)
    alias(libs.plugins.youhajun.android.hilt)
}

android {
    namespace = "com.youhajun.feature.call.impl"
}

dependencies {
    implementation(projects.feature.call.api)
    implementation(projects.domain.calling)
    implementation(projects.domain.room)
    implementation(projects.domain.user)
    implementation(projects.domain.conversation)
    implementation(projects.core.common)
    implementation(projects.core.webRtc)
    implementation(projects.core.stt)
    implementation(projects.core.ui)
    implementation(projects.core.model)
    implementation(projects.core.design)
    implementation(projects.core.notification)
    implementation(projects.core.route)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.orbit)
    implementation(libs.stream.webrtc)
    implementation(libs.stream.webrtc.android.ui)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}