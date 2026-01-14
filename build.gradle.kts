plugins {
    id("java")
}

group = "com.stephanofer"
version = "1.1.0"
repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")

    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.codemc.io/repository/maven-public/")

}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("de.tr7zw:item-nbt-api-plugin:2.14.1")
    compileOnly(files("libs/RPGItems-reloaded.jar"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}


