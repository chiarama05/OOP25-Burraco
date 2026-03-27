import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    application

    id("com.gradleup.shadow") version "9.3.1"
    id("org.danilopianini.gradle-java-qa") version "1.164.0"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
}

application {
    mainClass.set("it.unibo.burraco.BurracoApp") // Usa il package completo
}

tasks.processResources {
    from("src/main/resources")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform() // Abilita JUnit 5
    testLogging {
        events(*(TestLogEvent.entries.toTypedArray())) 
    }
    testLogging.showStandardStreams = true 
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "it.unibo.burraco.BurracoApp"
    }
}
