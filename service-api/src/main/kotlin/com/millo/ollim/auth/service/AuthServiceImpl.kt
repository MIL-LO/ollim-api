package com.millo.ollim.auth.service

import com.millo.ollim.core.auth.domain.OAuthUserInfo
import com.millo.ollim.core.auth.dto.LoginDTO
import com.millo.ollim.core.auth.port.AuthService
import com.millo.ollim.core.auth.port.OAuthService
import com.millo.ollim.core.auth.port.UserService
import com.millo.ollim.infrastructure.auth.jwt.JwtTokenProvider
import org.springframework.stereotype.Service
import java.util.*

/**
 * OAuth 기반 로그인 흐름을 처리하는 AuthService 구현체 (Facade 역할)
 */
@Service
class AuthServiceImpl(
    private val oAuthServices: List<OAuthService>,
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider
) : AuthService {

    /**
     * 소셜 로그인 전체 흐름을 처리하는 메서드
     * @param request 로그인 요청 DTO
     * @return 로그인 응답 DTO
     */
    override fun login(request: LoginDTO.Request): LoginDTO.Response {
        // provider에 해당하는 OAuthService 찾기
        val oAuthService = oAuthServices.find { it.supports(request.provider) }
            ?: throw IllegalArgumentException("지원하지 않는 OAuth 제공자입니다: ${request.provider}")

        // 인가 코드로 AccessToken 획득
        val accessToken = oAuthService.getAccessToken(request.provider, request.code)

        // AccessToken으로 사용자 정보 조회
        val userInfo: OAuthUserInfo = oAuthService.getUserInfo(request.provider, accessToken)

        // 사용자 등록 또는 조회
        val userResult = userService.createOrFindUser(request.provider, userInfo)
        val userId = userResult.id
        val isNewUser = userResult.isNew
        val role = "USER" // TODO: 추후 역할 분리 시 enum으로 대체
        val nickname = userInfo.nickname

        // JWT 토큰 생성
        val jwtAccessToken = jwtTokenProvider.createAccessToken(userId, role)
        val jwtRefreshToken = jwtTokenProvider.createRefreshToken(userId, role)

        // TODO: RefreshToken Redis 저장 로직 추가 예정

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
