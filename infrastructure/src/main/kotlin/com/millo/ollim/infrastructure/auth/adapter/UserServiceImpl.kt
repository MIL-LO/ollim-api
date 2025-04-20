package com.millo.ollim.infrastructure.auth.adapter

import com.millo.ollim.core.auth.domain.OAuthProvider
import com.millo.ollim.core.auth.domain.OAuthUserInfo
import com.millo.ollim.core.auth.dto.UserResult
import com.millo.ollim.core.auth.port.UserService
import org.springframework.stereotype.Service
import java.util.*

/**
 * 사용자 등록 또는 조회를 처리하는 UserService 구현체
 */
@Service
class UserServiceImpl : UserService {

    /**
     * OAuth 제공자와 사용자 정보를 바탕으로 사용자 등록 또는 조회
     * @param provider OAuth 제공자
     * @param userInfo 사용자 정보
     * @return UserResult (UUID 및 신규 여부 포함)
     */
    override fun createOrFindUser(provider: OAuthProvider, userInfo: OAuthUserInfo): UserResult {
        val dummyId = UUID.randomUUID()
        val isNew = true // 실제 DB 확인 결과에 따라 판단

        return UserResult(
            id = dummyId,
            isNew = isNew
        )
    }
}
