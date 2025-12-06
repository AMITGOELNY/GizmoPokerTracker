plugins {
    id("gizmo.feature")
    alias(libs.plugins.mokkery)
}

android {
    namespace = "com.ghn.poker.feature.auth"
}

compose.resources {
    publicResClass = true
    generateResClass = always
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:core-network"))
            implementation(project(":core:core-preferences"))
            implementation(project(":core:core-ui"))
            implementation(project(":core:core-resources"))
            implementation(project(":common"))
        }
        commonTest.dependencies {
            implementation(libs.mokkery.core)
            implementation(libs.mokkery.coroutines)
        }
    }
}
