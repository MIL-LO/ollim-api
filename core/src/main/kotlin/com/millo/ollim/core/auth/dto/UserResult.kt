package com.millo.ollim.core.auth.dto

import java.util.UUID

/**
 * 사용자 생성 또는 조회 후 결과 반환 DTO
 * @property id 사용자 고유 UUID
 * @property isNew 새로 생성된 사용자 여부
 */
data class UserResult(
    val id: UUID,
    val isNew: Boolean
)
