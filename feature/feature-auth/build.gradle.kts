plugins {
    id("gizmo.feature")
}

android {
    namespace = "com.ghn.poker.feature.auth"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:core-network"))
            implementation(project(":core:core-preferences"))
            implementation(project(":core:core-ui"))
            implementation(project(":common"))
        }
    }
}
