import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    application
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

group = "me.fang"
version = "1.0-SNAPSHOT"

val main = "me.fang.game.MainKt"

application {
    mainClass.set(main)
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.hexworks.zircon:zircon.core-jvm:2021.1.0-RELEASE")
    implementation("org.hexworks.zircon:zircon.jvm.swing:2021.1.0-RELEASE")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

tasks.getByName<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.getByName<Jar>("jar") {
    manifest.attributes["Main-Class"] = main

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
