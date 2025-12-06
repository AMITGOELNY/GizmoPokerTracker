plugins {
    id("gizmo.kmp.library")
    id("gizmo.koin.ksp")
}

android {
    namespace = "com.ghn.poker.core.preferences"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:core-common"))
            implementation(project(":core:core-di"))
            api(libs.multiplatform.settings)
            api(libs.multiplatform.settings.coroutines)
        }

        androidMain.dependencies {
            implementation(libs.androidx.security.crypto.ktx)
        }
    }
}
