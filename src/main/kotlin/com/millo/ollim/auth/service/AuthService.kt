package com.millo.ollim.auth.service

import OAuthUserInfo
import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.auth.dto.AuthInfoDTO
import com.millo.ollim.auth.dto.TokenDTO
import com.millo.ollim.user.domain.UserEntity
import com.millo.ollim.user.dto.UserProfileDTO
import jakarta.servlet.http.HttpServletRequest
import java.util.*

/**
 * 인증 관련 Service Interface
 */
interface AuthService {

    /**
     * OAuth2 로그인 성공 시 사용자 저장 또는 업데이트
     */
    fun saveOrUpdateUser(userInfo: OAuthUserInfo): UserEntity

    /**
     * 로그아웃 처리 (RefreshToken 삭제, AccessToken 블랙리스트 등록)
     */
    fun logout(user: UserPrincipal, accessToken: String)

    /**
     * 현재 로그인한 사용자의 인증 정보 반환
     */
    fun getAuthInfo(user: UserPrincipal): AuthInfoDTO.AuthInfoResponse

    /**
     * 회원가입 시 사용자 프로필 저장 및 상태 변경
     */
    fun registerUserProfile(userId: UUID, userProfileRequest: UserProfileDTO.UserProfileRequest)

    /**
     * RefreshToken을 기반으로 AccessToken 재발급
     */
    fun refresh(tokenRequest: TokenDTO.TokenRequest): TokenDTO.TokenResponse

    /**
     * 로그아웃 처리 (요청 객체에서 AccessToken 추출 포함)
     */
    fun logoutWithRequest(user: UserPrincipal, request: HttpServletRequest)
}
