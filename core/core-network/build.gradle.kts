plugins {
    id("gizmo.kmp.library")
    id("gizmo.koin.ksp")
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.ghn.poker.core.network"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:core-common"))
            implementation(project(":core:core-di"))
            implementation(project(":core:core-preferences"))
            api(libs.bundles.common.ktor)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
        }

        val desktopMain by getting
        desktopMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
    }
}
