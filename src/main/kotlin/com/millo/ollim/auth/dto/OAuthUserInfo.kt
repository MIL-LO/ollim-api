interface OAuthUserInfo {
    fun getProvider(): String
    fun getProviderId(): String
    fun getEmail(): String
    fun getName(): String
}
