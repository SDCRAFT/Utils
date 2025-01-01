import java.time.*
import java.util.*

plugins {
    kotlin("jvm") version "2.0.0"
    id("com.gradleup.shadow") version "8.3.5"
}

group = "org.sdcraft"
version = "1.0.0"

allprojects {
    group = rootProject.group
    version = rootProject.version
    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
            name = "spigotmc-repo"
        }
        maven("https://oss.sonatype.org/content/groups/public/") {
            name = "sonatype"
        }
    }
    apply(plugin = "org.jetbrains.kotlin.jvm")
    dependencies {
        testImplementation(kotlin("test"))
        compileOnly("org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    }
}

subprojects.filter {it.name !in listOf("commons")}.forEach{ project ->
    project.apply(plugin = "com.gradleup.shadow")
    val templateDest = project.layout.buildDirectory.dir("generated/sources/templates")
    val generateTemplates = project.tasks.register<Copy>("generateTemplates") {
        val props = mapOf(
            "name" to project.name,
            "uname" to project.name.substring(0, 1).uppercase(Locale.getDefault()) + project.name.substring(1),
            "version" to project.version.toString(),
            "timestamp" to Instant.now().epochSecond.toString()
        )
        inputs.properties(props)
        from(rootProject.file("src/templates/java"), rootProject.file("src/templates/resources"))
        into(templateDest)
        expand(props)
    }
    project.sourceSets.main {
        java.srcDir(generateTemplates.map { it.outputs.files })
    }
    project.tasks.withType<Jar> {
        dependsOn(generateTemplates)
        from(templateDest) {
            include("plugin.yml")
        }
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("outputs"))
    }
    project.tasks.jar {
        finalizedBy("shadowJar")
    }
    project.tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        dependencies {
            include(dependency("com.fasterxml.jackson.core:jackson-databind:2.18.2"))
            include(dependency("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.2"))
            include(dependency("com.fasterxml.jackson.core:jackson-core:2.18.2"))
        }
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
        archiveClassifier.set("")
        relocate("com.fasterxml.jackson.databind", "org.sdcraft.shadow.jackson.databind")
        relocate("com.fasterxml.jackson.dataformat.yaml", "org.sdcraft.shadow.jackson.dataformat.yaml")
        relocate("com.fasterxml.jackson.core", "org.sdcraft.shadow.jackson.core")
    }
}

tasks.jar {
    enabled = false
}