plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.youhajun.data.auth"
}

dependencies {
    implementation(projects.data.common)
    implementation(projects.domain.auth)
    implementation(projects.core.network)
    implementation(projects.core.datastore)
    implementation(projects.core.model)
    implementation(projects.core.event)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.dataStore)
    implementation(libs.kotlinx.collections.immutable)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}