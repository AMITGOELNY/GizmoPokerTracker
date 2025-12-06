plugins {
    id("gizmo.kmp.library")
    id("gizmo.koin.ksp")
    alias(libs.plugins.sqldelight)
}

android {
    namespace = "com.ghn.poker.core.database"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:core-common"))
            implementation(project(":core:core-di"))
        }

        androidMain.dependencies {
            implementation(libs.android.sqldelight.driver)
        }

        iosMain.dependencies {
            implementation(libs.ios.sqldelight.driver)
        }

        val desktopMain by getting
        desktopMain.dependencies {
            implementation(libs.jvm.sqldelight.driver)
        }
    }
}

sqldelight {
    databases {
        create("GizmoPokerDb") {
            packageName.set("com.ghn.poker.core.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            verifyMigrations.set(true)
        }
    }
    linkSqlite.set(true)
}
