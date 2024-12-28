plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":commons"))
    testImplementation(kotlin("test"))
}

tasks.withType<Jar> {
    from(project(":commons").sourceSets.main.get().output) //Compile
    from(project(":commons").sourceSets.main.get().resources) //Resources
}