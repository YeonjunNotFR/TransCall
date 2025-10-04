plugins {
    alias(libs.plugins.youhajun.jvm.library)
}

dependencies {
    implementation(projects.domain.conversation)
    implementation(projects.domain.room)
    implementation(projects.core.model)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.collections.immutable)

}