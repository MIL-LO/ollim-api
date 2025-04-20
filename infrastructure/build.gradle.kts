plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

dependencies {
    implementation(project(":core"))

    // spring-web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // oauth-client
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

     // Mongo
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // Kotlin 리플렉션
    implementation("org.jetbrains.kotlin:kotlin-reflect")

     // Kotlin 전용 Jackson 직렬화
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // 테스트
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Jar>("jar") {
    enabled = true
}
