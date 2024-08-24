package com.doubles.selfstudy.service;

import com.doubles.selfstudy.entity.StudyGroup;
import com.doubles.selfstudy.entity.StudyGroupBoard;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.entity.UserStudyGroup;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.StudyGroupBoardFixture;
import com.doubles.selfstudy.fixture.StudyGroupFixture;
import com.doubles.selfstudy.fixture.UserAccountFixture;
import com.doubles.selfstudy.fixture.UserStudyGroupFixture;
import com.doubles.selfstudy.repository.StudyGroupBoardRepository;
import com.doubles.selfstudy.repository.StudyGroupRepository;
import com.doubles.selfstudy.repository.UserAccountRepository;
import com.doubles.selfstudy.repository.UserStudyGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class StudyGroupBoardServiceTest {

    @Autowired
    private StudyGroupBoardService studyGroupBoardService;

    @MockBean
    private UserAccountRepository userAccountRepository;
    @MockBean
    private StudyGroupRepository studyGroupRepository;
    @MockBean
    private UserStudyGroupRepository userStudyGroupRepository;
    @MockBean
    private StudyGroupBoardRepository studyGroupBoardRepository;

    @Test
    void 스터디_그룹_게시글_생성_성공한_경우() {
        // Given
        String userId = "userId";
        String title = "title";
        String content = "content";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(mock(UserAccount.class)));
        when(userStudyGroupRepository.findByUserAccount(any()))
                .thenReturn(Optional.of(mock(UserStudyGroup.class)));

        // Then
        assertDoesNotThrow(() -> studyGroupBoardService.createStudyGroupBoard(userId, title, content));
    }

    @Test
    void 스터디_그룹_게시글_생성시_로그인이_안된_경우_에러_반환() {
        // Given
        String userId = "userId";
        String title = "title";
        String content = "content";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());
        when(userStudyGroupRepository.findByUserAccount(any()))
                .thenReturn(Optional.of(mock(UserStudyGroup.class)));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupBoardService.createStudyGroupBoard(userId, title, content)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_게시글_생성시_스터디_그룹에_가입이_안된_경우_에러_반환() {
        // Given
        String userId = "userId";
        String title = "title";
        String content = "content";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(mock(UserAccount.class)));
        when(userStudyGroupRepository.findByUserAccount(any()))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupBoardService.createStudyGroupBoard(userId, title, content)
        );
        assertEquals(ErrorCode.USER_STUDY_GROUP_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_게시글_수정이_성공한_경우() {
        // Given
        String userId = "userId";
        String password = "password";
        Long studyGroupBoardId = 1L;
        String title = "modify_title";
        String content = "modify_content";

        UserAccount userAccount = UserAccountFixture.get(userId, password);
        StudyGroup studyGroup = StudyGroupFixture.get();
        StudyGroupBoard studyGroupBoard = StudyGroupBoardFixture.get(userAccount, studyGroup, "title", "content");
        StudyGroupBoard modifyStudyGroupBoard = StudyGroupBoardFixture.get(userAccount, studyGroup, title, content);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(studyGroupBoardRepository.findById(studyGroupBoardId))
                .thenReturn(Optional.of(studyGroupBoard));
        when(studyGroupBoardRepository.saveAndFlush(any()))
                .thenReturn(modifyStudyGroupBoard);

        // Then
        assertDoesNotThrow(() -> studyGroupBoardService.modifyStudyGroupBoard(userId, studyGroupBoardId, title, content));
    }

    @Test
    void 스터디_그룹_게시글_수정시_로그인이_안된_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long studyGroupBoardId = 1L;
        String title = "modify_title";
        String content = "modify_content";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupBoardService
                        .modifyStudyGroupBoard(userId, studyGroupBoardId, title, content)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_게시글_수정시_게시글이_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        String password = "password";
        Long studyGroupBoardId = 1L;
        String title = "modify_title";
        String content = "modify_content";

        UserAccount userAccount = UserAccountFixture.get(userId, password);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(studyGroupBoardRepository.findById(studyGroupBoardId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupBoardService
                        .modifyStudyGroupBoard(userId, studyGroupBoardId, title, content)
        );
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_게시글_수정시_작성자가_다른_경우_에러_반환() {
        // Given
        String userId = "userId";
        String password = "password";
        Long studyGroupBoardId = 1L;
        String title = "modify_title";
        String content = "modify_content";

        UserAccount userAccount = UserAccountFixture.get(userId, password);
        UserAccount writer = UserAccountFixture.get("writer", "password");
        StudyGroup studyGroup = StudyGroupFixture.get();
        StudyGroupBoard studyGroupBoard = StudyGroupBoardFixture.get(writer, studyGroup, "title", "content");

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(studyGroupBoardRepository.findById(studyGroupBoardId))
                .thenReturn(Optional.of(studyGroupBoard));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupBoardService
                        .modifyStudyGroupBoard(userId, studyGroupBoardId, title, content)
        );
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_게시글_삭제가_성공한_경우() {
        // Given
        String userId = "userId";
        String password = "password";
        Long studyGroupBoardId = 1L;

        UserAccount userAccount = UserAccountFixture.get(userId, password);
        StudyGroup studyGroup = StudyGroupFixture.get();
        StudyGroupBoard studyGroupBoard = StudyGroupBoardFixture
                .get(userAccount, studyGroup, "title", "content");

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(studyGroupBoardRepository.findById(studyGroupBoardId))
                .thenReturn(Optional.of(studyGroupBoard));

        // Then
        assertDoesNotThrow(() -> studyGroupBoardService.deleteStudyGroupBoard(userId, studyGroupBoardId));
    }

    @Test
    void 스터디_그룹_게시글_삭제시_로그인하지_않은_경우_에러_반환() {
        // Given
        String userId = "userId";
        Long studyGroupBoardId = 1L;

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupBoardService
                        .deleteStudyGroupBoard(userId, studyGroupBoardId)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_게시글_삭제시_게시글이_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        String password = "password";
        Long studyGroupBoardId = 1L;

        UserAccount userAccount = UserAccountFixture.get(userId, password);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(studyGroupBoardRepository.findById(studyGroupBoardId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupBoardService
                        .deleteStudyGroupBoard(userId, studyGroupBoardId)
        );
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_게시글_삭제시_유저가_다른_경우_에러_반환() {
        // Given
        String userId = "userId";
        String password = "password";
        Long studyGroupBoardId = 1L;

        UserAccount userAccount = UserAccountFixture.get(userId, password);
        UserAccount writer = UserAccountFixture.get("writer", "password");
        StudyGroup studyGroup = StudyGroupFixture.get();
        StudyGroupBoard studyGroupBoard = StudyGroupBoardFixture
                .get(writer, studyGroup, "title", "content");

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(studyGroupBoardRepository.findById(studyGroupBoardId))
                .thenReturn(Optional.of(studyGroupBoard));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupBoardService
                        .deleteStudyGroupBoard(userId, studyGroupBoardId)
        );
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_게시글_리스트_조회_성공한_경우() {
        // Given
        String userId = "userId";
        String password = "password";
        Pageable pageable = mock(Pageable.class);

        UserAccount userAccount = UserAccountFixture.get(userId, password);
        StudyGroup studyGroup = StudyGroupFixture.get();
        UserStudyGroup userStudyGroup = UserStudyGroupFixture.getLeader(userId, studyGroup);

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(any()))
                .thenReturn(Optional.of(userStudyGroup));
        when(studyGroupBoardRepository.findAllByStudyGroupId(studyGroup.getId(), pageable))
                .thenReturn(Page.empty());

        // Then
        assertDoesNotThrow(() -> studyGroupBoardService.studyGroupBoardList(userId, pageable));
    }
}