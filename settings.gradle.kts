rootProject.name = "SS-GuiLibrary"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        maven("https://jitpack.io")
    }
}

include("API", "Compat")
