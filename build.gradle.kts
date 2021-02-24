import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    kotlin("jvm") version "1.4.30"
    id("org.jetbrains.dokka") version "1.4.20"
    id("maven-publish")
    id("java-library")
}

group = "me.ste.stevesseries"
version = System.getenv("BUILD_NUMBER") ?: "0"

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven(url = "https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    implementation("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
}

tasks.processResources {
    from(sourceSets.main.get().resources.srcDirs) {
        include("plugin.yml")
        filter<ReplaceTokens>("tokens" to hashMapOf("version" to version))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "inventoryguilibrary"

            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("https://repo.stev.gq/repository/generic/")

            credentials {
                username = System.getenv("NEXUS_USER")
                password = System.getenv("NEXUS_PASS")
            }
        }
    }
}

java {
    withSourcesJar()
}