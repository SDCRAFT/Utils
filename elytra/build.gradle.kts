plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":commons"))
    testImplementation(kotlin("test"))
    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.25.2")
}

tasks.withType<Jar> {
    from(project(":commons").sourceSets.main.get().output) //Compile
    from(project(":commons").sourceSets.main.get().resources) //Resources
}