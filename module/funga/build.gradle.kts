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
    compileOnly(project(":module:authentication"))
    compileOnly(project(":module:file-management"))
    implementation("dev.langchain4j:langchain4j:1.0.0-beta3")
    implementation("dev.langchain4j:langchain4j-open-ai:1.0.0-beta3")
    implementation("dev.langchain4j:langchain4j-ollama:1.0.0-beta3")
    implementation("io.milvus:milvus-sdk-java:2.5.9")
}

tasks.test {
    useJUnitPlatform()
}