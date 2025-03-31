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

    // Spring Web
    implementation("org.springframework.boot:spring-boot-starter-web")

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

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

sentry {
    includeSourceContext.set(true) // Sentry에서 소스 코드 보여주기
    org.set("millo-j0")            // Sentry 조직 이름
    projectName.set("ollim-api-wn") // Sentry 프로젝트 이름
    authToken.set(System.getenv("SENTRY_AUTH_TOKEN")) // 환경변수로 인증 토큰 설정
}

tasks.named<Jar>("jar") {
    enabled = false
}
