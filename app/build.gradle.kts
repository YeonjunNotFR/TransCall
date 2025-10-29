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

val featureModules = listOf(
    projects.feature.main,
    projects.feature.splash,
    projects.feature.splash.api,
    projects.feature.home,
    projects.feature.home.api,
    projects.feature.auth,
    projects.feature.auth.api,
    projects.feature.room,
    projects.feature.room.api,
    projects.feature.call,
    projects.feature.call.api,
    projects.feature.history,
    projects.feature.history.api,
)

val domainModules = listOf(
    projects.domain.history,
    projects.domain.room,
    projects.domain.auth,
    projects.domain.calling,
    projects.domain.user,
    projects.domain.conversation
)

val dataModules = listOf(
    projects.data.history,
    projects.data.room,
    projects.data.auth,
    projects.data.calling,
    projects.data.user,
    projects.data.conversation
)

val coreModules = listOf(
    projects.core.route,
    projects.core.network,
    projects.core.common,
    projects.core.webRtc,
    projects.core.database,
    projects.core.datastore,
    projects.core.design,
    projects.core.event,
    projects.core.model,
    projects.core.notification
)

dependencies {
    (featureModules + domainModules + dataModules + coreModules).forEach(::implementation)
}