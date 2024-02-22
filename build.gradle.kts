plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "org.acme"
version = "1.0-SNAPSHOT"

val optaplannerVersion = "9.44.0.Final"
val logbackVersion = "1.4.12"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation(platform("org.optaplanner:optaplanner-bom:${optaplannerVersion}"))
    implementation("org.optaplanner:optaplanner-core")
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