plugins {
    id("gizmo.feature")
}

android {
    namespace = "com.ghn.poker.feature.tracker"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:core-common"))
            implementation(project(":core:core-network"))
            implementation(project(":core:core-database"))
            implementation(project(":common"))
        }
    }
}
