import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for feature modules.
 * Combines KMP library, Compose, and Koin KSP plugins with feature-specific dependencies.
 */
class GizmoFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("gizmo.kmp.library")
                apply("gizmo.compose")
                apply("gizmo.koin.ksp")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.configureEach {
                    languageSettings {
                        optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                    }
                }

                sourceSets.named("commonMain") {
                    dependencies {
                        // Core common (MviViewModel, utilities)
                        api(project(":core:core-common"))

                        // Navigation3
                        implementation(libs.findLibrary("navigation3-ui").get())

                        // Lifecycle
                        api(libs.findLibrary("lifecycle-viewmodel-compose").get())

                        // Koin Compose
                        api(libs.findLibrary("koin-compose").get())
                        api(libs.findLibrary("koin-compose-viewmodel").get())

                        // Coroutines
                        implementation(libs.findLibrary("coroutines-core").get())

                        // Datetime
                        implementation(libs.findLibrary("datetime").get())

                        // Logging
                        implementation(libs.findLibrary("kermit").get())
                    }
                }

                sourceSets.named("commonTest") {
                    dependencies {
                        implementation(kotlin("test"))
                        implementation(libs.findLibrary("coroutines-test").get())
                        implementation(libs.findLibrary("kotest-assertions-core").get())
                        implementation(project(":core:core-testing"))
                    }
                }
            }
        }
    }
}