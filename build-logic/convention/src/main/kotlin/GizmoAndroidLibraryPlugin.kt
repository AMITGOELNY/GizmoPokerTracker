import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Convention plugin for Android library modules (non-KMP).
 * Sets up standard Android library configuration.
 */
class GizmoAndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<com.android.build.gradle.LibraryExtension> {
                compileSdk = 36

                defaultConfig {
                    minSdk = 26
                }

                compileOptions {
                    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_21
                    targetCompatibility = org.gradle.api.JavaVersion.VERSION_21
                }
            }
        }
    }
}