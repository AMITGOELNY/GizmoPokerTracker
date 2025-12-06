plugins {
    id("gizmo.feature")
}

android {
    namespace = "com.ghn.poker.feature.cards"
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
            implementation(project(":core:core-ui"))
            implementation(project(":core:core-resources"))
            implementation(project(":common"))
            implementation(libs.collections.immutable)
        }
    }
}
