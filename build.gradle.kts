import java.time.ZoneOffset
import java.time.LocalDateTime

plugins {
    kotlin("jvm") version "2.0.0"
}

group = "org.sdcraft"
version = "1.0-SNAPSHOT"

allprojects {
    group = "org.sdcraft.utils"
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
    apply(plugin = "java")
    apply(plugin = "eclipse")
    dependencies {
        testImplementation(kotlin("test"))
        compileOnly("org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    }
    tasks.test {
        useJUnitPlatform()
    }
    kotlin {
        jvmToolchain(11)
    }
    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

subprojects {
    tasks.jar {
        enabled = true
    }
    val templateSource = rootProject.file("src/templates")
    val templateDest = layout.buildDirectory.dir("generated/sources/templates")
    val generateTemplates = tasks.register<Copy>("generateTemplates") {
        val props = mapOf(
            "name" to project.name.substring(0, 1).toUpperCase() + project.name.substring(1),
            "version" to project.version.toString(),
            "timestamp" to LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toString()
        )
        inputs.properties(props)
        from(templateSource)
        into(templateDest)
        expand(props)
    }
    sourceSets.main {
        java.srcDir(generateTemplates.map { it.outputs.files })
    }
    tasks.withType<Jar> {
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

defaultTasks("clean", "build", "jar")