plugins {
    id("java")
}

group = "com.stephanofer"
version = "3.0.0"
repositories {
    mavenCentral()
    maven(url = "https://repo.hpfxd.com/releases/")
    maven("https://repo.codemc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.hpfxd.pandaspigot:pandaspigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("de.tr7zw:item-nbt-api-plugin:2.14.1")
    compileOnly(files("libs/RPGItems-reloaded-5.1.2.jar"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.jar {
    destinationDirectory.set(layout.projectDirectory.dir("target"))
}


