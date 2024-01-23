import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

@Suppress("PropertyName")
val JAVA_TARGET = JavaVersion.VERSION_17

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = JAVA_TARGET.majorVersion
            }
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }

        iosTarget.binaries.forEach { nativeBinary ->
            nativeBinary.linkerOpts.add("-lsqlite3")
        }
    }

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(project(":common"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.animation)

            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(libs.datetime)
            implementation(libs.image.loader)
            implementation(libs.kermit)
            implementation(libs.precompose)
            implementation(libs.precompose.navigation.typesafe)
            api(libs.webview.multiplatform)

            api(libs.koinCore)
            api(libs.koinCompose)
            implementation(libs.koinAnnotations)

            implementation(libs.bundles.commonKtor)
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.coroutines)

            // Required until SQLite upgrade
            implementation("co.touchlab:stately-common:2.0.5")
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

            api("io.github.qdsfdhvh:image-loader-extension-imageio:1.7.3")
        }

        iosMain.dependencies {
            implementation(libs.ios.sqldelight.driver)
            implementation(libs.ktor.client.ios)
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", "io.insert-koin:koin-ksp-compiler:1.3.0")
//    add("kspJvm", "io.insert-koin:koin-ksp-compiler:1.3.0")
}

tasks.withType<KotlinCompile<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
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
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

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
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }

    sourceSets["main"].res.srcDirs("src/androidMain/res", "src/commonMain/resources")
}

compose.desktop {
    application {
        mainClass = "MainKt"

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
    filters {
        classes {
            excludes += listOf()
        }
    }
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
