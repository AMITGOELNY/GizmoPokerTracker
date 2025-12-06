plugins {
    id("gizmo.kmp.library")
    id("gizmo.compose")
}

android {
    namespace = "com.ghn.poker.core.resources"
}

compose.resources {
    publicResClass = true
    generateResClass = always
}
