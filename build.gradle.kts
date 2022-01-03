import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
}

repositories {
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://papermc.io/repo/repository/maven-public/")
    mavenCentral()
}
val kordVersion: String by project
dependencies {
    compileOnly(kotlin("stdlib-jdk8:1.5.21"))
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    compileOnly("io.github.monun:kommand-api:2.6.6")

    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    compileOnly("dev.kord:kord-core:$kordVersion")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_16.toString()
    }

    create<Jar>("sourcesJar") {
        from(sourceSets["main"].allSource)
        archiveClassifier.set("sources")
    }

    processResources {
        filesMatching("**/*.yml") {
            expand(project.properties)
        }
    }

    register<Jar>("paperJar") {
        archiveBaseName.set("Whitelist-Automation")
        from(sourceSets["main"].output)

        doLast {
            copy {
                from(archiveFile)
                val plugins = File(rootDir, ".server/plugins/")
                into(if (File(plugins, archiveFileName.get()).exists()) File(plugins, "update") else plugins)
            }
        }
    }
}