import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        val commonMain by getting {
            kotlin.srcDir(layout.buildDirectory.dir("generated-src/jooq/main"))
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
