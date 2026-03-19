plugins {
    java
    application
    id("com.gradleup.shadow") version "9.3.1"
    id("org.danilopianini.gradle-java-qa") version "1.164.0"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21)) 
    }
}

application {
    mainClass.set("BurracoApp")
}

tasks.processResources {
    from("src/main/resources")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "BurracoApp"
    }
}
