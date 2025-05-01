plugins {
    alias(libs.plugins.youhajun.android.library)
    alias(libs.plugins.youhajun.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.youhajun.core.network"

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("String", "TOKEN_REFRESH_ENDPOINT", "\"refresh\"")
            buildConfigField("String", "BASE_URL", "\"https://dev.api.transcall.com\"")
        }
        release {
            buildConfigField("String", "TOKEN_REFRESH_ENDPOINT", "\"refresh\"")
            buildConfigField("String", "BASE_URL", "\"https://api.transcall.com\"")
        }
    }
}

dependencies {

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.ktor)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}