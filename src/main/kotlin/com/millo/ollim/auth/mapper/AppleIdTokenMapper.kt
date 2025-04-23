package com.millo.ollim.auth.mapper

import com.millo.ollim.auth.dto.AppleIdTokenPayload
import com.millo.ollim.auth.dto.AppleUserInfo

/**
 * AppleIdTokenPayload에서 AppleUserInfo로 매핑하는 확장 함수
 */
fun AppleIdTokenPayload.toUserInfo(): AppleUserInfo {
    return AppleUserInfo(
        sub = this.sub,
        email = this.email
    )
}
