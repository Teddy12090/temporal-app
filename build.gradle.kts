plugins {
    java
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"
description = "temporal"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("io.temporal:temporal-spring-boot-starter:1.31.0")
    implementation("org.projectlombok:lombok")
    implementation("org.assertj:assertj-core")
    implementation("org.mockito:mockito-core")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("org.springframework.boot:spring-boot-docker-compose")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.google.code.gson:gson:2.13.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
