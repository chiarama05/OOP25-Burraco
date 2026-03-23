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
    // BOM (Bill of Materials) per sincronizzare le versioni di JUnit
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    // Annotazioni e asserzioni Jupiter
    testImplementation("org.junit.jupiter:junit-jupiter")
    // Motore runtime per l'esecuzione dei test
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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

tasks.processResources {
    from("src/main/resources")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "it.unibo.burraco.BurracoApp"
    }
}
