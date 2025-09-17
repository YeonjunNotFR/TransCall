plugins {
    alias(libs.plugins.youhajun.android.application)
    alias(libs.plugins.youhajun.android.application.compose)
    alias(libs.plugins.youhajun.android.firebase)
    alias(libs.plugins.youhajun.android.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.youhajun.transcall"
}

dependencies {
    implementation(projects.feature.main)
    implementation(projects.feature.call)
    implementation(projects.feature.history)
    implementation(projects.feature.history.api)
    implementation(projects.feature.home)
    implementation(projects.domain.history)
    implementation(projects.domain.room)
    implementation(projects.domain.auth)
    implementation(projects.domain.calling)
    implementation(projects.domain.user)
    implementation(projects.domain.conversation)
    implementation(projects.data.history)
    implementation(projects.data.room)
    implementation(projects.data.auth)
    implementation(projects.data.calling)
    implementation(projects.data.user)
    implementation(projects.data.conversation)
    implementation(projects.core.route)
    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(projects.core.webRtc)
    implementation(projects.core.database)
    implementation(projects.core.notification)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.firebase.messaging)
    implementation(libs.timber)
    implementation(libs.bundles.workmanager)
    ksp(libs.androidx.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}