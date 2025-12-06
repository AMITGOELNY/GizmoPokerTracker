rootProject.name = "GizmoPoker"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jogamp.org/deployment/maven")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

// Core modules
include(":core:core-common")
include(":core:core-di")
include(":core:core-preferences")
include(":core:core-network")
include(":core:core-database")
include(":core:core-ui")

// Existing modules
include(":composeApp")
include(":server")
include(":server:jooq-generator")
include(":common")
include(":evaluator")
