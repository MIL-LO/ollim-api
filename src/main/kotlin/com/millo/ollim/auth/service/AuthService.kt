package com.millo.ollim.auth.service

import OAuthUserInfo

/**
 * AuthService Interface
 */
interface AuthService {
    fun saveOrUpdateUser(userInfo: OAuthUserInfo)
}
