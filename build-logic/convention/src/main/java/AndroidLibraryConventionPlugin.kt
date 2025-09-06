import com.android.build.api.dsl.LibraryExtension
import com.youhajun.convention.configureKotlinAndroid
import com.youhajun.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.findPlugin("android-library").get().get().pluginId)
            apply(plugin = libs.findPlugin("kotlin-android").get().get().pluginId)

            extensions.configure<LibraryExtension> {
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                defaultConfig.consumerProguardFiles("consumer-rules.pro")

                configureKotlinAndroid(this)
            }
        }
    }
}
