plugins {
    kotlin("jvm")
}

group = "cn.revoist.lifephoton.module.mti"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":module:file-management-not-auth"))
}

tasks.test {
    useJUnitPlatform()
}