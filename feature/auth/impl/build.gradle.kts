import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.library.compose)
    alias(libs.plugins.youhajun.android.hilt)
}

android {
    namespace = "com.youhajun.feature.auth.impl"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField(
            "String",
            "GOOGLE_WEB_CLIENT_ID",
            gradleLocalProperties(rootDir, providers).getProperty("GOOGLE_WEB_CLIENT_ID")
        )
    }
}

dependencies {
    implementation(projects.feature.home.api)
    implementation(projects.feature.auth.api)
    implementation(projects.domain.auth)
    implementation(projects.core.ui)
    implementation(projects.core.design)
    implementation(projects.core.model)
    implementation(projects.core.route)
    implementation(libs.bundles.credentials)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.orbit)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}