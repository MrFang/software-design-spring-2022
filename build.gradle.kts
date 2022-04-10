import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    kotlin("jvm") version "1.6.10"
    application
    jacoco
}

group = "me.fang"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val main = "me.fang.kosh.MainKt"

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        xml.outputLocation.set(File("$buildDir/reports/jacoco/report.xml"))
        html.required.set(false)
        csv.required.set(false)
    }
    executionData(File("build/jacoco/test.exec"))
    dependsOn("test")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

tasks.getByName("run", JavaExec::class) {
    standardInput = System.`in`
}

tasks.jar {
    manifest.attributes["Main-Class"] = main

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

application {
    mainClass.set(main)
}
