plugins {
    kotlin("jvm") version "2.1.0"
}

group = "cn.revoist.lifephoton.module.authentication"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":common"))
}

tasks.test {
    useJUnitPlatform()
}