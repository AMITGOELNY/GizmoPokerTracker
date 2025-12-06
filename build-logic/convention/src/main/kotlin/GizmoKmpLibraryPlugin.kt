import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for KMP library modules.
 * Sets up Android, iOS, and Desktop targets with common configuration.
 */
class GizmoKmpLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                applyDefaultHierarchyTemplate()

                androidTarget {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_21)
                    }
                }

                jvm("desktop")

                listOf(
                    iosX64(),
                    iosArm64(),
                    iosSimulatorArm64()
                ).forEach { iosTarget ->
                    iosTarget.binaries.forEach { nativeBinary ->
                        nativeBinary.linkerOpts.add("-lsqlite3")
                    }
                }

                sourceSets.all {
                    languageSettings {
                        optIn("kotlin.time.ExperimentalTime")
                        languageVersion = "2.2"
                        apiVersion = "2.2"
                    }
                }

                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }

            extensions.configure<com.android.build.gradle.LibraryExtension> {
                configureAndroid(this)
            }
        }
    }
}

internal fun Project.configureAndroid(extension: com.android.build.gradle.LibraryExtension) {
    extension.apply {
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