plugins {
    id("gizmo.kmp.library")
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.ghn.poker.core.common"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":common"))
            api(libs.coroutines.core)
            api(libs.datetime)
            api(libs.kermit)
            api(libs.lifecycle.viewmodel.compose)
            api(libs.navigation3.ui)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
