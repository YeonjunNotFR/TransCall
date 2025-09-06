
import com.youhajun.convention.configureKotlinJvm
import com.youhajun.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.findPlugin("jvm-library").get().get().pluginId)
            apply(plugin = libs.findPlugin("jetbrains-kotlin-jvm").get().get().pluginId)

            configureKotlinJvm()
        }
    }
}
