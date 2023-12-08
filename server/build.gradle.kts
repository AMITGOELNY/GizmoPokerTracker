import nu.studer.gradle.jooq.JooqGenerate
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Logging

plugins {
    kotlin("jvm")
    id("io.ktor.plugin") version "2.3.7"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21"
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
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-auto-head-response-jvm")
    implementation("io.ktor:ktor-server-sessions-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:${libs.versions.logbackVersion.get()}")
    implementation("io.ktor:ktor-server-config-yaml:2.3.7")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${libs.versions.kotlin.get()}")

    implementation(libs.flyway.core)
    jooqGenerator(libs.jdbc.sqlite)
    implementation(libs.jdbc.sqlite)
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
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.sqlite.SQLiteDatabase"
                        excludes =
                            listOf(
                                // Exclude flyway migration tables
                                "flyway_.*",
                            ).joinToString("|")
                    }
                    generate.apply {
                        isDaos = false
                        isRecords = true
                        isImmutablePojos = true
                        isPojosAsKotlinDataClasses = true
                        isKotlinNotNullPojoAttributes = true
                        isKotlinNotNullRecordAttributes = true
                        isKotlinNotNullInterfaceAttributes = true
                    }
                    target.apply {
                        packageName = "com.ghn.gizmodb" // TODO: Your java package name
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"

                    strategy.withMatchers(
                        Matchers()
                            .withTables(
                                MatchersTableType()
                                    .withTableClass(
                                        MatcherRule()
                                            .withTransform(MatcherTransformType.PASCAL)
                                            .withExpression("$0_table")
                                    )
                                    .withPojoClass(
                                        MatcherRule()
                                            .withTransform(MatcherTransformType.PASCAL)
                                            .withExpression("$0_db")
                                    )
                            )
                    )
                }
            }
        }
    }
}

tasks.named<JooqGenerate>("generateJooq") {
    inputs.dir(migrationPath)
    allInputsDeclared.set(true)
    dependsOn("flywayMigrate")
    doFirst { Thread.sleep(2000) }
}

tasks.getByName<JavaExec>("run") {
    environment(
        "DATABASE_URL",
        properties["databaseUrl"] ?: environment["DATABASE_URL"]
            ?: "sqlite:${rootDir.resolve("gizmopoker.db")}"
    )
}
