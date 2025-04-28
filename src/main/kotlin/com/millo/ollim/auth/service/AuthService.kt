package com.millo.ollim.auth.service

import OAuthUserInfo
import com.millo.ollim.user.domain.UserEntity

/**
 * AuthService Interface
 */
interface AuthService {
    fun saveOrUpdateUser(userInfo: OAuthUserInfo) : UserEntity
}
