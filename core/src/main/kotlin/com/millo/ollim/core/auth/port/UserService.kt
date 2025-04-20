package com.millo.ollim.core.auth.port

import com.millo.ollim.core.auth.domain.OAuthProvider
import com.millo.ollim.core.auth.domain.OAuthUserInfo
import com.millo.ollim.core.auth.dto.UserResult

interface UserService {
    fun createOrFindUser(provider: OAuthProvider, userInfo: OAuthUserInfo): UserResult
}
