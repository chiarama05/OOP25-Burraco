plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
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
