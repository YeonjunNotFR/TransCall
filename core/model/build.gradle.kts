plugins {
    alias(libs.plugins.youhajun.jvm.library)
}

dependencies {
    compileOnly(libs.compose.marker)
    implementation(libs.kotlinx.collections.immutable)
}