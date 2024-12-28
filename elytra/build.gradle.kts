plugins {
    kotlin("jvm")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":commons"))
}

tasks.withType<Jar> {
    from(project(":commons").sourceSets.main.get().output) //Compile
    from(project(":commons").sourceSets.main.get().resources) //Resources
}