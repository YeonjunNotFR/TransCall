plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.youhajun.data.conversation"
}

dependencies {
    implementation(projects.domain.conversation)
    implementation(projects.data.common)
    implementation(projects.core.network)
    implementation(projects.core.model)
    implementation(projects.core.database)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.room)
    implementation(libs.kotlinx.collections.immutable)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}