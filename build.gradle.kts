import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

plugins {
    kotlin("jvm") version "1.6.20"
    id("me.ste.stevesseries.bukkitgradle") version "1.2"
    `maven-publish`
}

group = "me.ste.stevesseries.guilib"
version = "0.0.0-mc1.18.2"
description = "A Spigot library plugin for building and management of interactive menus (GUIs)."

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    maven("https://mvn-public.steenesvc.cf/releases")
}

allprojects {
    apply<KotlinPluginWrapper>()
    apply<MavenPublishPlugin>()

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }

    dependencies {
        compileOnly(kotlin("stdlib"))
    }

    publishing {
        repositories {
            repositories {
                maven {
                    name = "SteenePublic"
                    url = uri("https://mvn-public.steenesvc.cf/releases")

                    credentials {
                        username = System.getenv("REPO_USERNAME")
                        password = System.getenv("REPO_PASSWORD")
                    }
                }
            }
        }
    }
}

dependencies {
    softDepend("me.ste.stevesseries.kotlin:kotlin:1.6.20")

    api(project(":API"))
}

tasks {
    jar {
        from(project(":API").sourceSets.main.get().output)
    }
}

runServer {
    downloadUri.set("https://papermc.io/api/v2/projects/paper/versions/1.18.2/builds/302/downloads/paper-1.18.2-302.jar")
    serverArgs.add("nogui")
}

pluginDescription {
    mainClass.set("me.ste.stevesseries.guilib.GuiLibraryPlugin")
    apiVersion.set("1.18")
    authors.add("SteveTheEngineer")
}

publishing {
    publications {
        create<MavenPublication>("plugin") {
            artifactId = "guilib"

            from(components.getByName("java"))
        }
    }
}
