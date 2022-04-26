group = "me.ste.stevesseries"
version = rootProject.version

dependencies {
    api("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
}

publishing {
    publications {
        create<MavenPublication>("api") {
            artifactId = "guilib-api"

            from(components.getByName("java"))
        }
    }
}
