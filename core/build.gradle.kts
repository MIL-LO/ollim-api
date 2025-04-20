plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

dependencies {

    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Spring Data REST (HAL 자동 노출 및 Repository 어노테이션 사용 시 필요)
    implementation("org.springframework.boot:spring-boot-starter-data-rest")

    // PostgreSQL
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("org.postgresql:postgresql")

    // Mongo
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // Kotlin 리플렉션
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Kotlin 전용 Jackson 직렬화
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Jasypt
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")

    // 테스트
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Jar>("jar") {
    enabled = true
}

tasks.register("prepareKotlinBuildScriptModel") {}
