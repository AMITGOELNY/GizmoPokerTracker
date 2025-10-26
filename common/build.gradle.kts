@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.serialization)
    alias(libs.plugins.kotlinMultiplatform)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    wasmJs { browser() }

    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.time.ExperimentalTime")
            }
        }

        val commonMain by getting {
            kotlin.srcDir(layout.buildDirectory.dir("generated-src/jooq/main"))

            languageSettings {
                languageVersion = "2.2"
                apiVersion = "2.2"
            }

            dependencies {
                api(libs.datetime)
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    mustRunAfter(
        ":server:generateJooq",
        ":server:movePojos"
    )
}
