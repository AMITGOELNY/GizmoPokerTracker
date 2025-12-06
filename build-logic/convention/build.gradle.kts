plugins {
    `kotlin-dsl`
}

group = "com.ghn.poker.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

dependencies {
    compileOnly(libs.plugins.kotlinMultiplatform.toDep())
    compileOnly(libs.plugins.android.library.toDep())
    compileOnly(libs.plugins.android.application.toDep())
    compileOnly(libs.plugins.jetbrainsCompose.toDep())
    compileOnly(libs.plugins.compose.compiler.toDep())
    compileOnly(libs.plugins.ksp.toDep())
    compileOnly(libs.plugins.serialization.toDep())
}

fun Provider<PluginDependency>.toDep() = map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "gizmo.kmp.library"
            implementationClass = "GizmoKmpLibraryPlugin"
        }
        register("compose") {
            id = "gizmo.compose"
            implementationClass = "GizmoComposePlugin"
        }
        register("koinKsp") {
            id = "gizmo.koin.ksp"
            implementationClass = "GizmoKoinKspPlugin"
        }
        register("feature") {
            id = "gizmo.feature"
            implementationClass = "GizmoFeaturePlugin"
        }
        register("androidLibrary") {
            id = "gizmo.android.library"
            implementationClass = "GizmoAndroidLibraryPlugin"
        }
    }
}