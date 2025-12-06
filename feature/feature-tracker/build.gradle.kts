plugins {
    id("gizmo.feature")
}

android {
    namespace = "com.ghn.poker.feature.tracker"
}

compose.resources {
    publicResClass = true
    generateResClass = always
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:core-common"))
            implementation(project(":core:core-network"))
            implementation(project(":core:core-database"))
            implementation(project(":core:core-ui"))
            implementation(project(":core:core-resources"))
            implementation(project(":common"))
        }
    }
}
