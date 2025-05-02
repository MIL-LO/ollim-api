package com.millo.ollim.common.config

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableEncryptableProperties
class JasyptConfig(
    @Value("\${jasypt.encryptor.password}")
    private val jasyptPassword: String
) {

    /**
     * 암호화/복호화 처리를 위한 Jasypt StringEncryptor Bean 정의
     */
    @Bean(name = ["jasyptStringEncryptor"])
    fun stringEncryptor(): StringEncryptor {
        val encryptor = PooledPBEStringEncryptor()
        val config = SimpleStringPBEConfig().apply {
            password = jasyptPassword
            algorithm = "PBEWithMD5AndDES"
            setKeyObtentionIterations("1000")
            setPoolSize("1")
            providerName = "SunJCE"
            setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator")
            setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator")
            stringOutputType = "base64"
        }

        encryptor.setConfig(config)

        return encryptor
    }
}
