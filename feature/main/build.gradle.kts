plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.library.compose)
    alias(libs.plugins.youhajun.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.youhajun.feature.main.impl"
}

dependencies {
    implementation(projects.core.design)
    implementation(projects.core.ui)
    implementation(projects.core.route)
    implementation(projects.core.event)
    implementation(projects.core.model)
    implementation(projects.feature.splash.api)
    implementation(projects.feature.splash.impl)
    implementation(projects.feature.auth.api)
    implementation(projects.feature.auth.impl)
    implementation(projects.feature.home.api)
    implementation(projects.feature.home.impl)
    implementation(projects.feature.history.api)
    implementation(projects.feature.history.impl)
    implementation(projects.feature.call.api)
    implementation(projects.feature.room.api)
    implementation(projects.feature.room.impl)
    implementation(projects.domain.auth)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.orbit)
    implementation(libs.bundles.navigation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material3)
    implementation(libs.kotlinx.collections.immutable)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}