package com.millo.ollim.common.enums

/*
    TODO
    - 삭제 가능성 있음
    - 싱글톤 형식으로만 버전 관리 하게 될 수 있음
*/
enum class Path(
    val version: String,        // version
    val path: String,           // API 엔드포인트 경로
    val description: String,    // API 설명
//    val requiredRole: String  // 요구되는 권한
) {
    AUTH_LOGIN(Versions.V1, "/auth/login", "로그인 요청")
}
