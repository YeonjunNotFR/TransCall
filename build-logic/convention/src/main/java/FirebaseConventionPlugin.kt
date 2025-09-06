import com.youhajun.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class FirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.findPlugin("gms").get().get().pluginId)

            dependencies {
                add("implementation", platform(libs.findLibrary("firebase-bom").get()))
            }
        }
    }
}