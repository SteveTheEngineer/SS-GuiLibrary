import me.ste.stevesseries.bukkitgradle.extension.description.LoadingPhase
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

plugins {
    kotlin("jvm") version "1.8.0"
    id("com.github.SteveTheEngineer.SS-BukkitGradle") version "1.6"
    `maven-publish`
}

group = "me.ste.stevesseries.guilib"
version = "0.1.0-mc1.19.3"
description = "A Spigot library plugin for building and management of interactive menus (GUIs)."

repositories {
    mavenLocal()
    maven("https://jitpack.io")
}

allprojects {
    apply<KotlinPluginWrapper>()
    apply<MavenPublishPlugin>()

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(8))
    }

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }

    dependencies {
        compileOnly(kotlin("stdlib"))
    }
}

dependencies {
    softDepend("com.github.SteveTheEngineer:SS-Kotlin:1.8.0")

    api(project(":API"))
}

tasks {
    jar {
        from(
            project(":API").sourceSets.main.get().output,
            project(":Compat").sourceSets.main.get().output
        )
    }
}

runServer {
    downloadUri.set("https://papermc.io/api/v2/projects/paper/versions/1.18.2/builds/388/downloads/paper-1.18.2-388.jar")
    serverArgs.add("nogui")
}

pluginDescription {
    mainClass.set("me.ste.stevesseries.guilib.GuiLibraryPlugin")
    apiVersion.set("1.16")
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

// TODO the old plugin name was different from the current one. fix somehow!
