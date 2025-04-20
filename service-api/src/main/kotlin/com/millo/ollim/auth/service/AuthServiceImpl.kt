package com.millo.ollim.auth.service

import com.millo.ollim.core.auth.domain.OAuthUserInfo
import com.millo.ollim.core.auth.dto.LoginDTO
import com.millo.ollim.core.auth.port.AuthService
import com.millo.ollim.core.auth.port.OAuthService
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * OAuth 기반 로그인 흐름을 처리하는 AuthService 구현체 (Facade 역할)
 */
@Service
class AuthServiceImpl(
    private val oAuthServices: List<OAuthService>
) : AuthService {

    /**
     * 소셜 로그인 전체 흐름을 처리하는 메서드
     * @param request 로그인 요청 DTO
     * @return 로그인 결과 응답 DTO
     */
    override fun login(request: LoginDTO.Request): LoginDTO.Response {
        // provider에 해당하는 OAuthService 찾기
        val oAuthService = oAuthServices.find { it.supports(request.provider) }
            ?: throw IllegalArgumentException("지원하지 않는 OAuth 제공자입니다: ${request.provider}")

        // 인가 코드로 AccessToken 획득
        val accessToken = oAuthService.getAccessToken(request.provider, request.code)

        // AccessToken으로 사용자 정보 조회
        val userInfo: OAuthUserInfo = oAuthService.getUserInfo(request.provider, accessToken)

        // 사용자 DB 등록 또는 조회 (임시 UUID 사용)
        val isNewUser = true // TODO: 실제 유저 DB 조회 및 저장 로직 필요
        val userId = UUID.randomUUID() // TODO: 실제 저장된 UUID로 대체
        val role = "USER" // TODO: 실제 역할 반환
        val nickname = userInfo.nickname

        // JWT 토큰 생성
        val jwtAccessToken = "access.jwt.token" // TODO: JWT 발급 로직 필요
        val jwtRefreshToken = "refresh.jwt.token" // TODO: RefreshToken 발급 후 Redis 저장 필요

        return LoginDTO.Response(
            accessToken = jwtAccessToken,
            refreshToken = jwtRefreshToken,
            isNewUser = isNewUser,
            userId = userId,
            role = role,
            nickname = nickname
        )
    }
}
