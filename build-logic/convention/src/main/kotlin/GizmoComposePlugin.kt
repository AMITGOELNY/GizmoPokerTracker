import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for Compose Multiplatform modules.
 * Adds Compose dependencies and configuration.
 */
class GizmoComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val compose = extensions.getByType<ComposeExtension>()

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.all {
                    languageSettings {
                        optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
                        optIn("androidx.compose.material3.ExperimentalMaterial3Api")
                    }
                }

                sourceSets.named("commonMain") {
                    dependencies {
                        implementation(compose.dependencies.runtime)
                        implementation(compose.dependencies.foundation)
                        implementation(compose.dependencies.material3)
                        implementation(compose.dependencies.materialIconsExtended)
                        implementation(compose.dependencies.animation)
                        implementation(compose.dependencies.components.resources)
                        implementation(compose.dependencies.components.uiToolingPreview)
                    }
                }

                sourceSets.named("androidMain") {
                    dependencies {
                        implementation(compose.dependencies.uiTooling)
                    }
                }

                sourceSets.named("desktopMain") {
                    dependencies {
                        implementation(compose.dependencies.desktop.currentOs)
                    }
                }
            }
        }
    }
}