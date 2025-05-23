plugins {
    kotlin("jvm")
}

group = "cn.revoist.lifephoton.module.funga"
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