plugins {
    id("gizmo.kmp.library")
}

android {
    namespace = "com.ghn.poker.core.di"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.koin.core)
            api(libs.koin.annotations)
        }
    }
}
