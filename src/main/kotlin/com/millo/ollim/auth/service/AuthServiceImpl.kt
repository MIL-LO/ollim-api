package com.millo.ollim.auth.service

import com.millo.ollim.auth.dto.AuthDTO
import com.millo.ollim.auth.dto.OAuthUserInfo
import com.millo.ollim.user.domain.ProviderType
import com.millo.ollim.user.domain.UserEntity
import com.millo.ollim.user.domain.UserOAuthEntity
import com.millo.ollim.user.domain.UserRole
import com.millo.ollim.user.repository.UserOAuthRepository
import com.millo.ollim.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * OAuth 인증 로직 구현체
 */
@Service
class AuthServiceImpl(
    private val googleOAuthClient: GoogleOAuthClient,
    private val userRepository: UserRepository,
    private val userOAuthRepository: UserOAuthRepository,
    @Value("\${oauth.google.client-id}") private val clientId: String,
    @Value("\${oauth.google.redirect-uri}") private val redirectUri: String
) : AuthService {

    /**
     * Google 인증 URL 생성
     */
    override fun generateGoogleAuthorizeUrl(): String {
        val baseUrl = "https://accounts.google.com/o/oauth2/v2/auth"
        val responseType = "code"
        val scope = "openid email profile"
        val accessType = "offline"
        val prompt = "consent"

        return "$baseUrl?client_id=$clientId&redirect_uri=$redirectUri" +
                "&response_type=$responseType&scope=$scope" +
                "&access_type=$accessType&prompt=$prompt"
    }

    /**
     * Google OAuth 로그인 처리
     * - code로 access token 요청
     * - access token으로 사용자 정보 조회
     * - 기존 회원 여부 확인 후 저장 또는 조회
     * - JWT 발급 예정
     */
    @Transactional
    override fun loginWithGoogle(request: AuthDTO.OAuthLoginRequest): AuthDTO.OAuthLoginResponse {
        val accessToken = googleOAuthClient.getAccessToken(request.code)
        val userInfo: OAuthUserInfo = googleOAuthClient.getUserInfo(accessToken)

        val user = userOAuthRepository.findByProviderAndOauthId(ProviderType.GOOGLE, userInfo.oauthId)
            ?.user
            ?: saveNewUser(userInfo)

        // TODO: JWT 토큰 발급 로직으로 대체
        val fakeAccessToken = "access-token-${user.id}"
        val fakeRefreshToken = "refresh-token-${user.id}"

        return AuthDTO.OAuthLoginResponse(
            accessToken = fakeAccessToken,
            refreshToken = fakeRefreshToken
        )
    }

    /**
     * 신규 Google 사용자 저장
     */
    private fun saveNewUser(userInfo: OAuthUserInfo): UserEntity {
        val user = userRepository.save(
            UserEntity(
                email = userInfo.email,
                role = UserRole.USER
            )
        )
        userOAuthRepository.save(
            UserOAuthEntity(
                user = user,
                provider = ProviderType.GOOGLE,
                oauthId = userInfo.oauthId
            )
        )
        return user
    }
}
