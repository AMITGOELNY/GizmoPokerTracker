// Root build file for core modules container
// This registers a clean task so Android Studio's "Clean Project" works correctly

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}
