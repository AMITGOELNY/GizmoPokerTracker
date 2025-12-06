import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for Koin DI with KSP annotation processing.
 * Sets up Koin dependencies and KSP compiler for annotation-based DI.
 */
class GizmoKoinKspPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<com.google.devtools.ksp.gradle.KspExtension> {
                arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.named("commonMain") {
                    dependencies {
                        implementation(libs.findLibrary("koin-core").get())
                        implementation(libs.findLibrary("koin-annotations").get())
                    }
                }

                // Add generated KSP sources to commonMain
                sourceSets.named("commonMain") {
                    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
                }
            }

            dependencies {
                add("kspCommonMainMetadata", libs.findLibrary("koin-compiler").get())
                add("kspAndroid", libs.findLibrary("koin-compiler").get())
                add("kspIosX64", libs.findLibrary("koin-compiler").get())
                add("kspIosArm64", libs.findLibrary("koin-compiler").get())
                add("kspIosSimulatorArm64", libs.findLibrary("koin-compiler").get())
            }

            // Ensure KSP tasks run in correct order
            tasks.configureEach {
                if (name.startsWith("ksp") && name != "kspCommonMainKotlinMetadata") {
                    dependsOn("kspCommonMainKotlinMetadata")
                }
            }

            tasks.matching { it.name.startsWith("compile") && it.name.contains("Kotlin") }.configureEach {
                if (name != "kspCommonMainKotlinMetadata") {
                    dependsOn("kspCommonMainKotlinMetadata")
                }
            }
        }
    }
}