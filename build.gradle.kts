import java.time.*
import java.util.*

plugins {
    kotlin("jvm") version "2.0.0"
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
    println(project.name)
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
}

tasks.jar {
    enabled = false
}