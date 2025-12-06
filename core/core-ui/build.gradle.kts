plugins {
    id("gizmo.kmp.library")
    id("gizmo.compose")
}

android {
    namespace = "com.ghn.poker.core.ui"
}

compose.resources {
    publicResClass = true
    generateResClass = always
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:core-common"))
            api(libs.coil.core)
            api(libs.coil.network)
            api(libs.coil.compose)
            api(libs.coil.svg)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}
