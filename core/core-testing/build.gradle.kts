plugins {
    id("gizmo.kmp.library")
}

android {
    namespace = "com.ghn.poker.core.testing"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlin.test)
            api(libs.coroutines.test)
        }
        androidMain.dependencies {
            api(libs.kotlin.test.junit)
        }
        desktopMain.dependencies {
            api(libs.kotlin.test.junit)
        }
    }
}
