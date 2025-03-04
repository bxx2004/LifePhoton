plugins {
    kotlin("jvm")
}

group = "cn.revoist.lifephoton"
version = "0.0.1"

repositories {
    mavenCentral()
}
application {
    mainClass.set("cn.revoist.lifephoton.Booster")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

tasks.test {
    useJUnitPlatform()
}