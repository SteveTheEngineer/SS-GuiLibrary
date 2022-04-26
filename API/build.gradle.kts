group = "me.ste.stevesseries.guilib"
version = rootProject.version

dependencies {
    api("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("api") {
            artifactId = "guilib-api"

            from(components.getByName("java"))
        }
    }
}
