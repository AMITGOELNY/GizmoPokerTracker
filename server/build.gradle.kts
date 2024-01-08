import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import nu.studer.gradle.jooq.JooqGenerate
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging

plugins {
    kotlin("jvm")
    id("io.ktor.plugin") version libs.versions.ktorVersion.get()
    alias(libs.plugins.serialization)
    alias(libs.plugins.jooq)
    alias(libs.plugins.flyway)
    alias(libs.plugins.shadowjar)
}

val dbUrl = "jdbc:sqlite:${buildDir.resolve("reference.db")}"
val migrationPath = "$projectDir/src/main/resources/db/migration"

flyway {
    url = dbUrl
    locations = arrayOf("filesystem:$migrationPath")
    buildDir.mkdir()
}

group = "com.ghn"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

sourceSets {
    main {
        kotlin.srcDir(buildDir.resolve("generated-src/jooq/$main"))
    }
}

dependencies {
    implementation(project(":common"))

    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-auto-head-response-jvm")
    implementation("io.ktor:ktor-server-sessions-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("ch.qos.logback:logback-classic:${libs.versions.logbackVersion.get()}")
    implementation("io.ktor:ktor-server-config-yaml:2.3.7")

    implementation(libs.bouncyCastle)
    implementation(libs.datetime)
    implementation(libs.flyway.core)
    jooqGenerator(libs.jdbc.sqlite)
    jooqGenerator(projects.server.jooqGenerator)
    implementation(libs.jdbc.sqlite)
    implementation(libs.ktor.client.core.v203)
    implementation(libs.bundles.scrapeIt)
    implementation(libs.rssparser)

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${libs.versions.kotlin.get()}")
}

jooq {
    version.set(libs.versions.jooq.get())
    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = Logging.DEBUG
                jdbc.apply {
                    driver = "org.sqlite.JDBC"
                    url = dbUrl
                }
                generator.apply {
                    name = "JooqGenerator"
                    database.apply {
                        name = "org.jooq.meta.sqlite.SQLiteDatabase"
                        excludes =
                            listOf(
                                // Exclude flyway migration tables
                                "flyway_.*",
                            ).joinToString("|")
                        forcedTypes.addAll(
                            listOf(
                                ForcedType().apply {
                                    includeTypes = "DATETIME"
                                    userType = "kotlinx.datetime.Instant"
                                    binding = "com.ghn.database.JooqInstantBinding"
                                },
                                ForcedType().apply {
                                    includeTypes = "DATE"
                                    userType = "kotlinx.datetime.LocalDate"
                                    binding = "com.ghn.database.JooqLocalDateBinding"
                                },
                                ForcedType().apply {
                                    userType = "com.ghn.gizmodb.common.models.GameType"
                                    withEnumConverter(true)
                                    includeExpression = ".*\\.gameType"
                                },
                                ForcedType().apply {
                                    userType = "com.ghn.gizmodb.common.models.Venue"
                                    withEnumConverter(true)
                                    includeExpression = ".*\\.venue"
                                },
                            )
                        )
                    }
                    generate.apply {
                        isDaos = false
                        isRecords = true
                        // Pojos as simple data classes
                        isSerializablePojos = false
                        isImmutablePojos = true
                        isPojosToString = false
                        isPojosEqualsAndHashCode = false
                        isPojosAsKotlinDataClasses = true
                        isKotlinNotNullPojoAttributes = true
                        isJavaTimeTypes = false
                    }
                    target.apply {
                        packageName = "com.ghn.gizmodb"
                    }
                    strategy.name = "JooqStrategy"
                }
            }
        }
    }
}

tasks.create<Copy>("movePojos") {
    val pojosTree = fileTree(layout.buildDirectory.dir("generated-src/jooq/main")) {
        include("**/models/**")
    }
    from(pojosTree)
    into(rootProject.file("common/build/generated-src/jooq/main"))
    doLast {
        pojosTree.forEach { if (it.isFile) it.delete() }
    }
}

tasks.getByName<JooqGenerate>("generateJooq") {
    inputs.dir(migrationPath)
    allInputsDeclared.set(true)
    dependsOn("flywayMigrate")
    doFirst { Thread.sleep(2000) }
    finalizedBy("movePojos")
}

tasks.getByName<JavaExec>("run") {
    environment(
        "DATABASE_URL",
        properties["databaseUrl"] ?: environment["DATABASE_URL"]
            ?: "sqlite:${rootDir.resolve("gizmopoker.db")}"
    )
    val localProperties = gradleLocalProperties(rootDir)
    val secret = localProperties.getProperty("SECRET_JWT", "")
    environment(
        /* name = */
        "SECRET_JWT",
        /* value = */
        secret ?: environment["SECRET_JWT"] ?: ""
    )
}
