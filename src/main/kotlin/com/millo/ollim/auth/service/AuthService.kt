package com.millo.ollim.auth.service

import OAuthUserInfo
import com.millo.ollim.auth.domain.UserPrincipal
import com.millo.ollim.user.domain.UserEntity

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
}
