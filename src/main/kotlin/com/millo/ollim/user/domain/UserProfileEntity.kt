package com.millo.ollim.user.domain

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.io.Serializable
import java.time.LocalDate
import java.util.*

/**
 * 닉네임, MBTI, 생활패턴 등 사용자 개인화 설정
 */
@Entity
@Table(name = "user_profiles")
class UserProfileEntity(

    /**
     * 사용자 엔티티와 1:1 매핑 (user_id 기반, PK와 FK 공유)
     */
    @Id
    @Column(name = "user_id", nullable = false)
    @Comment("FK → users.id, 사용자 계정 참조 (PK)")
    val userId: UUID,

    @Version
    @Column(name = "version")
    @Comment("JPA 낙관적 락 버전")
    var version: Long? = null,

    @MapsId
    @OneToOne(targetEntity = UserEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @Column(name = "nickname", nullable = false, length = 30)
    @Comment("사용자 닉네임 (중복 허용)")
    var nickname: String = "임시값",

    @Column(name = "gender", length = 10)
    @Comment("성별")
    var gender: String? = null,

    @Column(name = "birth_date")
    @Comment("생년월일")
    var birthDate: LocalDate? = null,

    @Column(name = "active_time", length = 10)
    @Comment("주간/야간 활동 시간대")
    var activeTime: String? = null,

    @Column(name = "energy_type", length = 10)
    @Comment("낮봄/밤형 구분")
    var energyType: String? = null,

    @Column(name = "activity_spaces", length = 100)
    @Comment("활동 공간(예: 집, 회사)")
    var activitySpaces: String? = null,

    @Column(name = "mbti", length = 4)
    @Comment("MBTI 성격유형")
    var mbti: String? = null,

    @Column(name = "profile_image", columnDefinition = "TEXT")
    @Comment("감정 캐릭터 이미지 URL, ENUM사용 고려")
    @Enumerated(EnumType.STRING)
    var profileImage: EmotionCharacterType? = null
)
