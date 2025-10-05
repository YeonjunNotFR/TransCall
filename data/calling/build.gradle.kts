plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.youhajun.data.signaling"
}

dependencies {
    implementation(projects.domain.calling)
    implementation(projects.data.common)
    implementation(projects.data.room)
    implementation(projects.core.network)
    implementation(projects.core.model)
    implementation(libs.bundles.ktor)
    implementation(libs.timber)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.collections.immutable)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}