plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "org.acme"
version = "1.0-SNAPSHOT"

val optaplannerVersion = "9.44.0.Final"
val logbackVersion = "1.4.12"
val apacheCommonsVersion = "3.13.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation(platform("org.optaplanner:optaplanner-bom:${optaplannerVersion}"))
    implementation("org.optaplanner:optaplanner-core")
    implementation("org.apache.commons:commons-lang3:${apacheCommonsVersion}")
    testImplementation("org.optaplanner:optaplanner-test")

    runtimeOnly("ch.qos.logback:logback-classic:${logbackVersion}")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}