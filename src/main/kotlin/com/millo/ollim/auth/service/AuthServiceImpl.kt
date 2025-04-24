package com.millo.ollim.auth.service

import OAuthUserInfo
import com.millo.ollim.user.domain.*
import com.millo.ollim.user.repository.UserOAuthRepository
import com.millo.ollim.user.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * AuthServiceImpl
 */
@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val userOAuthRepository: UserOAuthRepository
) : AuthService {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun saveOrUpdateUser(userInfo: OAuthUserInfo) {
        val providerType = ProviderType.valueOf(userInfo.getProvider().uppercase())

        log.info(">>> 사용자 이메일로 회원 존재 여부 확인 시작: email={}", userInfo.getEmail())
        val existingUser = userRepository.findByEmail(userInfo.getEmail())

        // 기존 회원이면 종료
        if (existingUser != null) {
            log.info(">>> 기존 사용자 로그인: {}", existingUser.email)
            return
        }

        // 신규 유저 + 프로필 Cascade 저장
        val newUser = UserEntity(
            email = userInfo.getEmail(),
            role = UserRole.USER,
            status = UserStatus.ACTIVE,
            lastLoginAt = LocalDateTime.now()
        )

        val profile = UserProfileEntity(
            userId = newUser.id,
            user = newUser,
            nickname = generateDefaultNickname(userInfo)
        )

        newUser.profile = profile // Cascade 관계 설정

        userRepository.save(newUser)
        log.info(">>> 신규 사용자 및 프로필 저장 완료: {}", newUser.id)

        userOAuthRepository.save(
            UserOAuthEntity(
                user = newUser,
                provider = providerType,
                oauthId = userInfo.getProviderId(),
                createdAt = LocalDateTime.now()
            )
        )
        log.info(">>> OAuth 연동 저장 완료")
    }

    private fun generateDefaultNickname(userInfo: OAuthUserInfo): String {
        val emailPart = userInfo.getEmail().substringBefore("@")
        return "${userInfo.getProvider().lowercase()}_$emailPart"
    }
}
