plugins {
    id("gizmo.feature")
}

android {
    namespace = "com.ghn.poker.feature.feed"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:core-common"))
            implementation(project(":core:core-network"))
            implementation(project(":common"))
        }
    }
}
