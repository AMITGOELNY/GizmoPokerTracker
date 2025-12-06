plugins {
    id("gizmo.feature")
}

android {
    namespace = "com.ghn.poker.feature.cards"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:core-network"))
            implementation(libs.collections.immutable)
        }
    }
}
