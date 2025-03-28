package com.millo.ollim.core.config

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableEncryptableProperties
class JasyptConfig {

    @Value("\${jasypt.encryptor.password}")
    private val jasyptPassword: String? = null

    @Bean(name = ["jasyptStringEncryptor"])
    fun stringEncryptor(): StringEncryptor {

        val encryptor = PooledPBEStringEncryptor()
        val config = SimpleStringPBEConfig()

        config.password = jasyptPassword // Jasypt 비밀번호
        config.algorithm = "PBEWITHHMACSHA512ANDAES_256"
        config.setKeyObtentionIterations("1000") // 키 획득 반복 횟수 (기본값: 1000)
        config.setPoolSize("1") // 암호화 풀 크기
        config.providerName = "SunJCE" // 기본 Java 암호화 제공자 사용
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator") // 랜덤 Salt 사용
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator") // 랜덤 IV 사용 (AES-256을 위해 필수)
        config.stringOutputType = "base64" // Base64 형식으로 암호화된 값 저장

        encryptor.setConfig(config)

        return encryptor
    }
}
