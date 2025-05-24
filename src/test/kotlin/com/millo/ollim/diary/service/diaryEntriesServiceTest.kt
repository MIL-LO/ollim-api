package com.millo.ollim.diary.service

import com.millo.ollim.common.util.AIConnector
import com.millo.ollim.diary.domain.*
import com.millo.ollim.diary.dto.DiaryDTO
import com.millo.ollim.diary.service.impl.DiaryServiceImpl
import com.millo.ollim.user.domain.*
import com.millo.ollim.user.dto.UserProfileDTO
import com.millo.ollim.user.service.UserProfileService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class diaryEntriesServiceTest:StringSpec ({

    lateinit var diaryEntriesService:DiaryEntriesService
    lateinit var diaryEmotionsService:DiaryEmotionsService
    lateinit var userProfileService: UserProfileService
    lateinit var diaryContentService: DiaryContentsService
    lateinit var diaryCollectionItemsService: DiaryCollectionItemsService
    lateinit var aiConnector: AIConnector

    lateinit var diaryService: DiaryServiceImpl

    val userId = UUID.randomUUID()
    val user = UserEntity(
        id = userId,
        email = "email",
        role = UserRole.USER,
        status = UserStatus.PENDING,
        lastLoginAt = LocalDateTime.now(),
        withdrawnAt = LocalDateTime.now(),
        profile = null,
    )
    val profile = UserProfileDTO.UserProfileResponse(
        nickname = "nick",
        gender = "man",
        birthDate = LocalDate.now(),
        activeTime = "active time",
        energyType = "energy time",
        activitySpaces = "",
        mbti = UserMBTI.ENFJ,
        profileImage = null,
    )

    val entries = mutableListOf(
        DiaryEntryEntity(UUID.randomUUID(),userId,"mood", "1",false),
        DiaryEntryEntity(UUID.randomUUID(),userId,"mood", "1",false),
        DiaryEntryEntity(UUID.randomUUID(),userId,"mood", "1",false),
        DiaryEntryEntity(UUID.randomUUID(),userId,"mood", "1",false),
        DiaryEntryEntity(UUID.randomUUID(),userId,"mood", "1",false),
    )
    val contents = mutableListOf(
        DiaryContentEntity(entries[0].id.toString(),userId.toString(),"content1","url", LocalDateTime.now(), LocalDateTime.now()),
        DiaryContentEntity(entries[1].id.toString(),userId.toString(),"content2","url", LocalDateTime.now(), LocalDateTime.now()),
        DiaryContentEntity(entries[2].id.toString(),userId.toString(),"content3","url", LocalDateTime.now(), LocalDateTime.now()),
        DiaryContentEntity(entries[3].id.toString(),userId.toString(),"content4","url", LocalDateTime.now(), LocalDateTime.now()),
        DiaryContentEntity(entries[4].id.toString(),userId.toString(),"content5","url", LocalDateTime.now(), LocalDateTime.now()),
    )
    val tags = mutableListOf(
        EmotionTagEntity(1,"tag1","description1","color1","category1","group1",true),
        EmotionTagEntity(2,"tag2","description2","color2","category2","group2",true),
        EmotionTagEntity(3,"tag3","description3","color3","category3","group3",true),
        EmotionTagEntity(4,"tag4","description4","color4","category4","group4",true),
        EmotionTagEntity(5,"tag5","description5","color5","category5","group5",true),
    )
    val diaryTags = mutableListOf(
        DiaryEmotionEntity(DiaryEntryEmotionId(entries[0].id,1),tags[0]),
        DiaryEmotionEntity(DiaryEntryEmotionId(entries[1].id,2),tags[0]),
        DiaryEmotionEntity(DiaryEntryEmotionId(entries[2].id,3),tags[0]),
        DiaryEmotionEntity(DiaryEntryEmotionId(entries[3].id,4),tags[0]),
        DiaryEmotionEntity(DiaryEntryEmotionId(entries[4].id,5),tags[0]),
    )

    beforeEach {
         diaryEntriesService= mockk()
         diaryEmotionsService= mockk()
         userProfileService= mockk()
         diaryContentService= mockk()
         diaryCollectionItemsService= mockk()
        aiConnector = mockk()

        diaryService= DiaryServiceImpl(
            diaryEntriesService,
            diaryContentService,
            diaryEmotionsService,
            diaryCollectionItemsService,
            userProfileService,
            aiConnector
        )

    }

    "다이어리 조회 - 아무 다이어리 없는 경우 빈 배열 반환한다. "{
        // given
        val page= 1
        val expectPage= PageRequest.of(page - 1, 10, Sort.by("createdAt").descending())
        every {
            diaryEntriesService.findByUserIdWithIsNotDeleted(userId, expectPage)
        } returns mutableListOf<DiaryEntryEntity>()
        // when
        val result = diaryService.getDiaries(userId, page)

        // then
        result.shouldBeEmpty()

        verify {
            diaryEntriesService.findByUserIdWithIsNotDeleted(userId, expectPage)
        }
    }

    "다이어리 조회 - 최신순으로 정렬된 형태로 반환한다." {
        // given
        val page= 1
        val expectPage= PageRequest.of(page - 1, 10, Sort.by("createdAt").descending())

        every {
            diaryEntriesService.findByUserIdWithIsNotDeleted(userId, expectPage)
        } returns entries
        every {
            diaryContentService.findByDiaryId(any())
        } returns contents[0]
        every {
            diaryEmotionsService.findByDiaryId(any())
        } returns diaryTags

        // when
        val result = diaryService.getDiaries(userId, page)
        // then
        result.shouldNotBeEmpty()

        result.size shouldBe 5
        verify {
            diaryEntriesService.findByUserIdWithIsNotDeleted(userId, expectPage)
        }
    }

    "다이어리 생성" {
        // given
        val request = DiaryDTO.CreateRequest("content",null,"mood", listOf(1,2,3))
        every{
            diaryEntriesService.save(any())
        } returns entries[0]
        every{
            diaryContentService.save(any(), any(),any())
        } returns contents[0]
        every{
            diaryEmotionsService.save(any(),request.emotionTags)
        } returns diaryTags
        every {
            userProfileService.getProfile(userId)
        }returns profile
        every {
            aiConnector.sendDiaryToAI(any())
        }returns emptyArray()

        // when
        diaryService.createNewDiary(userId,request)
        // then
        verify{
            diaryEntriesService.save(any())
        }
        verify{
            diaryContentService.save(any(), any(),any())
        }
        verify{
            diaryEmotionsService.save(any(),request.emotionTags)
        }
        verify {
            userProfileService.getProfile(userId)
        }
    }

})
