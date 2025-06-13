plugins {
    alias(libs.plugins.youhajun.jvm.library)
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.domain.user)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)

}