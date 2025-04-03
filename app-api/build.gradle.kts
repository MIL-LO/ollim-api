plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("io.sentry.jvm.gradle")
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":infrastructure"))

    // Spring Boot 기본
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Micrometer
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Sentry
    implementation("io.sentry:sentry-spring-boot-starter:8.3.0")
    implementation("io.sentry:sentry-logback:8.3.0")

    // 테스트
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

sentry {
    includeSourceContext.set(true)
    org.set("millo-j0")
    projectName.set("ollim-api")
    authToken.set(System.getenv("SENTRY_AUTH_TOKEN"))
}

tasks.named<Jar>("jar") {
    enabled = false
}
