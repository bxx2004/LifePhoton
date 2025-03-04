plugins {
    kotlin("jvm")
}

group = "cn.revoist.lifephoton.module.filemanagement"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":module:authentication"))
}

tasks.test {
    useJUnitPlatform()
}