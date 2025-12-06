import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.serialization)
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_21.majorVersion
            }
        }

//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_21)
//        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "GizmoPoker"
            isStatic = true

            // Export all feature modules for iOS umbrella framework
            export(project(":core:core-common"))
            export(project(":core:core-ui"))
            export(project(":core:core-di"))
            export(project(":core:core-network"))
            export(project(":core:core-resources"))
            export(project(":feature:feature-auth"))
            export(project(":feature:feature-tracker"))
            export(project(":feature:feature-feed"))
            export(project(":feature:feature-cards"))
        }

        iosTarget.binaries.forEach { nativeBinary ->
            nativeBinary.linkerOpts.add("-lsqlite3")
        }
    }

//    wasmJs {
//        moduleName = "composeApp"
//        browser {
//            val rootDirPath = project.rootDir.path
//            val projectDirPath = project.projectDir.path
//            commonWebpackConfig {
//                outputFileName = "composeApp.js"
//                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
//                    static = (static ?: mutableListOf()).apply {
//                        // Serve sources to debug inside browser
//                        add(rootDirPath)
//                        add(projectDirPath)
//                    }
//                }
//            }
//
//        }
//        binaries.executable()
//    }

    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
                optIn("androidx.compose.material3.ExperimentalMaterial3Api")
                optIn("kotlin.time.ExperimentalTime")

                languageVersion = "2.2"
                apiVersion = "2.2"
            }

            compilerOptions {
                // Common compiler options applied to all Kotlin source sets
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }

        val desktopMain by getting

        commonMain {
            languageSettings {
                languageVersion = "2.2"
                apiVersion = "2.2"
            }

            dependencies {
                implementation(project(":common"))
                implementation(project(":core:core-ui"))
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.animation)

                implementation(libs.navigation.compose)
                api(libs.lifecycle.viewmodel.compose)

                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(libs.coroutines.core)

                implementation(libs.coil.core)
                implementation(libs.coil.compose)
                implementation(libs.coil.network)
                implementation(libs.coil.svg)
                implementation(libs.collections.immutable)
                implementation(libs.datetime)
                implementation(libs.kermit)
                api(libs.webview.multiplatform)

                api(libs.koin.compose)
                api(libs.koin.compose.viewmodel)
                api(libs.koin.core)
                implementation(libs.koin.annotations)

                api(libs.bundles.common.ktor)
                implementation(libs.multiplatform.settings)
                implementation(libs.multiplatform.settings.coroutines)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.coroutines.test)
                implementation(libs.kotest.assertions.core)
                implementation(libs.mokkery.core)
                implementation(libs.mokkery.coroutines)
            }
        }

        androidMain.dependencies {
            implementation(libs.compose.ui)
            implementation(libs.compose.material)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.android.sqldelight.driver)
            implementation(libs.androidx.security.crypto.ktx)
            implementation(libs.ktor.client.okhttp)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.jvm.sqldelight.driver)
            implementation(libs.ktor.client.okhttp)

            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.10.1")
        }

        iosMain.dependencies {
            implementation(libs.ios.sqldelight.driver)
            implementation(libs.ktor.client.ios)

            // Export feature modules for iOS umbrella framework
            api(project(":core:core-common"))
            api(project(":core:core-ui"))
            api(project(":core:core-di"))
            api(project(":core:core-network"))
            api(project(":core:core-resources"))
            api(project(":feature:feature-auth"))
            api(project(":feature:feature-tracker"))
            api(project(":feature:feature-feed"))
            api(project(":feature:feature-cards"))
        }
    }
}

ksp {
    arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
}

dependencies {
    add("kspCommonMainMetadata", libs.koin.compiler)
    add("kspAndroid", libs.koin.compiler)
    add("kspIosX64", libs.koin.compiler)
    add("kspIosArm64", libs.koin.compiler)
    add("kspIosSimulatorArm64", libs.koin.compiler)
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

// Fix KSP task dependencies to prevent implicit dependency warnings
tasks.configureEach {
    if (name.startsWith("ksp") && name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

// Koin Annotation Config
kotlin.sourceSets.commonMain {
    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
}

android {
    namespace = "com.ghn.poker.tracker"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/composeResources")

    defaultConfig {
        applicationId = "com.ghn.poker.tracker"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }

    sourceSets["main"].res.srcDirs("src/androidMain/res", "src/commonMain/composeResources")
}

compose.desktop {
    application {
        mainClass = "com.ghn.poker.tracker.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.ghn.poker.tracker"
            packageVersion = "1.0.0"
        }
    }
}

sqldelight {
    databases {
        create("GizmoPokerDb") {
            packageName.set("com.ghn.poker.tracker")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            verifyMigrations.set(true)
        }
    }
    linkSqlite.set(true)
}

kover {
//    filters {
//        classes {
//            excludes += listOf()
//        }
//    }
}

afterEvaluate {
    tasks.withType<JavaExec> {
        jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
        jvmArgs("--add-opens", "java.desktop/java.awt.peer=ALL-UNNAMED")

        if (System.getProperty("os.name").contains("Mac")) {
            jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
        }
    }
}
