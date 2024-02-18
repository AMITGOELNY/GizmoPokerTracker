import org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.konan.target.CompilerOutputKind

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.jetbrainsCompose)

}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = "Evaluator"
//            isStatic = true
//        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.components.resources)
            }

            resources.srcDirs(
                "src/commonMain/composeResources",
                "src/commonMain/resources",
            )
        }
    }
}

// TODO: Look for alternative if not working.  Ideally should not depend on ComposeResources
//  Config to extract the hash table text files as resources for use on native iOS
//  This should allow usage of accessing these files within Evaluator module
copyNativeResources("commonMain")
copyNativeResources("iosMain")

fun copyNativeResources(sourceSet: String) {
    if (sourceSet.isEmpty()) throw IllegalStateException("Valid sourceSet required")

    val prefix = "copy${sourceSet.capitalize()}Resources"

    tasks.withType<KotlinNativeLink> {
        val firstIndex = name.indexOfFirst { it.isUpperCase() }
        val taskName = "$prefix${name.substring(firstIndex)}"

        dependsOn(
            tasks.register<Copy>(taskName) {
                from("src/$sourceSet/resources")
                when (outputKind) {
                    CompilerOutputKind.FRAMEWORK -> into(outputFile.get())
                    CompilerOutputKind.PROGRAM -> into(destinationDirectory.get())
                    else -> throw IllegalStateException("Unhandled binary outputKind: $outputKind")
                }
            }
        )
    }

    tasks.withType<FatFrameworkTask> {
        if (destinationDir.path.contains("Temp")) return@withType

        val firstIndex = name.indexOfFirst { it.isUpperCase() }
        val taskName = "$prefix${name.substring(firstIndex)}"

        dependsOn(
            tasks.register<Copy>(taskName) {
                from("src/$sourceSet/resources")
                into(fatFramework)
            }
        )
    }
}
