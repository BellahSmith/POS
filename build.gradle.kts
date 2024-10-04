

plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("com.google.cloud:google-cloud-aiplatform:3.35.0")
    implementation (platform("com.google.cloud:libraries-bom:26.33.0"))
    implementation ("com.google.cloud:google-cloud-vision")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.14.0")
    implementation("com.google.api:gax:2.17.1")
    implementation("org.slf4j:slf4j-api:2.0.0")
    implementation("org.slf4j:slf4j-simple:2.0.0")
    implementation(files("C:/Users/my pc/Documents/bridj-0.7.0.jar"))
    implementation (files("C:/Users/my pc/Documents/webcam-capture-0.3.12.jar"))
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation ("org.json:json:20210307")
}

tasks.test {
    useJUnitPlatform()
}