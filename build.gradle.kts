plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "com.desticube.addons"
version = "1.0-SNAPSHOT"
description = "DestiCube's main addon plugin for inventories, small commands, etc."
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")

}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    compileOnly("com.github.DestiCube:DestiPlaceholders:063d8015d6")

    implementation("com.github.GamerDuck123:DuckCommons:4b98647497")

}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks {
    shadowJar {
//        minimize()
        archiveFileName.set("${rootProject.name}-[v${rootProject.version}].jar")

        listOf("com.gamerduck.commons").forEach {
            relocate(it, "${rootProject.group}.commons")
        }
    }

    compileJava {
        options.release.set(17)
//        options.encoding = "UTF-8"
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(
                "name" to rootProject.name,
                "group" to rootProject.group,
                "version" to rootProject.version,
                "description" to rootProject.description
            )
        }
    }
}