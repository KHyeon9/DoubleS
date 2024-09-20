package com.doubles.selfstudy.service;

import com.doubles.selfstudy.config.DatabaseCleanup;
import com.doubles.selfstudy.entity.StudyGroup;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.entity.UserStudyGroup;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.fixture.StudyGroupFixture;
import com.doubles.selfstudy.fixture.UserStudyGroupFixture;
import com.doubles.selfstudy.repository.AlarmRepository;
import com.doubles.selfstudy.repository.StudyGroupRepository;
import com.doubles.selfstudy.repository.UserAccountRepository;
import com.doubles.selfstudy.repository.UserStudyGroupRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class StudyGroupServiceTest {

    @Autowired
    private StudyGroupService studyGroupService;

    @MockBean
    private UserAccountRepository userAccountRepository;
    @MockBean
    private StudyGroupRepository studyGroupRepository;
    @MockBean
    private UserStudyGroupRepository userStudyGroupRepository;
    @MockBean
    private AlarmRepository alarmRepository;

    @Test
    void 스터디_그룹_생성에_성공한_경우() {
        // Given
        String userId = "userId";
        String studyGroupName = "studyGroupName";
        String description = "description";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(mock(UserAccount.class)));
        when(studyGroupRepository.save(any()))
                .thenReturn(mock(StudyGroup.class));
        when(userStudyGroupRepository.save(any()))
                .thenReturn(mock(UserStudyGroup.class));

        // Then
        assertDoesNotThrow(() -> studyGroupService.createStudyGroup(userId, studyGroupName, description));
    }

    @Test
    void 스터디_그룹_생성시_로그인한_유저가_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        String studyGroupName = "studyGroupName";
        String description = "description";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.createStudyGroup(userId, studyGroupName, description)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_생성시_스터디_그룹에_이미_속한_경우_에러_반환() {
        // Given
        String userId = "userId";
        String studyGroupName = "studyGroupName";
        String description = "description";
        StudyGroup studyGroupFixture = StudyGroupFixture.get();
        UserStudyGroup userStudyGroupFixture = UserStudyGroupFixture.getLeader(userId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupFixture));
        when(studyGroupRepository.save(any()))
                .thenReturn(mock(StudyGroup.class));
        when(userStudyGroupRepository.save(any()))
                .thenReturn(mock(UserStudyGroup.class));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.createStudyGroup(userId, studyGroupName, description)
        );
        assertEquals(ErrorCode.DUPLICATED_STUDY_GROUP, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_수정이_성공한_경우() {
        // Given
        String userId = "userId";
        String studyGroupName = "studyGroupName";
        String modifyStudyGroupName = "modify_studyGroupName";
        String description = "description";
        String modifyDescription = "modify_description";
        StudyGroup studyGroupFixture = StudyGroupFixture.get(studyGroupName, description);
        StudyGroup modifyStudyGroupFixture = StudyGroupFixture.get(modifyStudyGroupName, modifyDescription);

        UserStudyGroup userStudyGroupLeaderFixture = UserStudyGroupFixture.getLeader(userId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupLeaderFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupLeaderFixture));
        when(studyGroupRepository.findById(userStudyGroupLeaderFixture.getStudyGroup().getId()))
                .thenReturn(Optional.of(studyGroupFixture));
        when(studyGroupRepository.saveAndFlush(any()))
                .thenReturn(modifyStudyGroupFixture);

        // Then
        assertDoesNotThrow(() -> studyGroupService.modifyStudyGroup(userId, modifyStudyGroupName, modifyDescription));
    }

    @Test
    void 스터디_그룹_수정시_로그인_안된_경우_에러_반환() {
        // Given
        String userId = "userId";
        String modifyStudyGroupName = "modify_studyGroupName";
        String modifyDescription = "modify_description";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.modifyStudyGroup(userId, modifyStudyGroupName, modifyDescription)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_수정시_가입된_스터디_그룹이_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        String modifyStudyGroupName = "modify_studyGroupName";
        String modifyDescription = "modify_description";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(mock(UserAccount.class)));
        when(userStudyGroupRepository.findByUserAccount(any()))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.modifyStudyGroup(userId, modifyStudyGroupName, modifyDescription)
        );
        assertEquals(ErrorCode.USER_STUDY_GROUP_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_수정시_리더가_아닌_경우_에러_반환() {
        // Given
        String userId = "userId";
        String studyGroupName = "studyGroupName";
        String modifyStudyGroupName = "modify_studyGroupName";
        String description = "description";
        String modifyDescription = "modify_description";
        StudyGroup studyGroupFixture = StudyGroupFixture.get(studyGroupName, description);
        StudyGroup modifyStudyGroupFixture = StudyGroupFixture.get(modifyStudyGroupName, modifyDescription);

        UserStudyGroup userStudyGroupMemberFixture = UserStudyGroupFixture.getMember(userId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupMemberFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupMemberFixture));
        when(studyGroupRepository.findById(userStudyGroupMemberFixture.getStudyGroup().getId()))
                .thenReturn(Optional.of(studyGroupFixture));
        when(studyGroupRepository.saveAndFlush(any()))
                .thenReturn(modifyStudyGroupFixture);

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.modifyStudyGroup(userId, modifyStudyGroupName, modifyDescription)
        );
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_수정시_스터디_그룹이_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        String studyGroupName = "studyGroupName";
        String modifyStudyGroupName = "modify_studyGroupName";
        String description = "description";
        String modifyDescription = "modify_description";
        StudyGroup studyGroupFixture = StudyGroupFixture.get(studyGroupName, description);

        UserStudyGroup userStudyGroupLeaderFixture = UserStudyGroupFixture.getLeader(userId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupLeaderFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupLeaderFixture));
        when(studyGroupRepository.findById(userStudyGroupLeaderFixture.getStudyGroup().getId()))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.modifyStudyGroup(userId, modifyStudyGroupName, modifyDescription)
        );
        assertEquals(ErrorCode.STUDY_GROUP_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_삭제가_성공한_경우() {
        // Given
        String userId = "userId";
        StudyGroup studyGroupFixture = StudyGroupFixture.get();

        UserStudyGroup userStudyGroupLeaderFixture = UserStudyGroupFixture.getLeader(userId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupLeaderFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupLeaderFixture));

        // Then
        assertDoesNotThrow(() -> studyGroupService.deleteStudyGroup(userId));
    }

    @Test
    void 스터디_그룹_삭제시_로그인_하지_않는_경우_에러_반환() {
        // Given
        String userId = "userId";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.deleteStudyGroup(userId)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_삭제시_스터디_그룹이_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        StudyGroup studyGroupFixture = StudyGroupFixture.get();

        UserStudyGroup userStudyGroupLeaderFixture = UserStudyGroupFixture.getLeader(userId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupLeaderFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.deleteStudyGroup(userId)
        );
        assertEquals(ErrorCode.USER_STUDY_GROUP_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_삭제시_리더가_아닌_경우_에러_반환() {
        // Given
        String userId = "userId";
        StudyGroup studyGroupFixture = StudyGroupFixture.get();

        UserStudyGroup userStudyGroupMemberFixture = UserStudyGroupFixture.getMember(userId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupMemberFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupMemberFixture));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.deleteStudyGroup(userId)
        );
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_가입_알림_전송이_성공한_경우() {
        // Given
        String leaderUserId = "leaderUserId";
        String inviteUserId = "inviteUserId";
        String studyGroupName = "studyGroupName";
        String description = "description";

        StudyGroup studyGroupFixture = StudyGroupFixture.get(studyGroupName, description);
        UserStudyGroup userStudyGroupLeaderFixture = UserStudyGroupFixture.getLeader(leaderUserId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupLeaderFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(leaderUserId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupLeaderFixture));
        when(userAccountRepository.findById(inviteUserId))
                .thenReturn(Optional.of(mock(UserAccount.class)));

        // Then
        assertDoesNotThrow(() -> studyGroupService.inviteAlarmStudyGroupMember(leaderUserId, inviteUserId));
    }

    @Test
    void 스터디_그룹_가입_알림_전송시_로그인_안한_경우_에러_반환() {
        // Given
        String leaderUserId = "leaderUserId";
        String inviteUserId = "inviteUserId";

        // When
        when(userAccountRepository.findById(leaderUserId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.inviteAlarmStudyGroupMember(leaderUserId, inviteUserId)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_가입_알림_전송시_전송_유저가_그룹을_탈퇴한_경우_에러_반환() {
        // Given
        String leaderUserId = "leaderUserId";
        String inviteUserId = "inviteUserId";
        String studyGroupName = "studyGroupName";
        String description = "description";

        StudyGroup studyGroupFixture = StudyGroupFixture.get(studyGroupName, description);
        UserStudyGroup userStudyGroupLeaderFixture = UserStudyGroupFixture.getLeader(leaderUserId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupLeaderFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(leaderUserId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.inviteAlarmStudyGroupMember(leaderUserId, inviteUserId)
        );
        assertEquals(ErrorCode.USER_STUDY_GROUP_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_가입_알림_전송시_전송_유저가_리더가_아닌_경우_에러_반환() {
        // Given
        String notLeaderUserId = "notLeaderUserId";
        String inviteUserId = "inviteUserId";
        String studyGroupName = "studyGroupName";
        String description = "description";

        StudyGroup studyGroupFixture = StudyGroupFixture.get(studyGroupName, description);
        UserStudyGroup userStudyGroupMemberFixture = UserStudyGroupFixture.getMember(notLeaderUserId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupMemberFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(notLeaderUserId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupMemberFixture));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.inviteAlarmStudyGroupMember(notLeaderUserId, inviteUserId)
        );
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_가입_알림_전송시_그룹이_다찬_경우_에러_반환() {
        // Given
        String leaderUserId = "leaderUserId";
        String inviteUserId = "inviteUserId";
        String studyGroupName = "studyGroupName";
        String description = "description";

        StudyGroup studyGroupFixture = StudyGroupFixture.get(studyGroupName, description);
        UserStudyGroup userStudyGroupLeaderFixture = UserStudyGroupFixture.getLeader(leaderUserId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupLeaderFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(leaderUserId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupLeaderFixture));
        when(userStudyGroupRepository.countByStudyGroup(userStudyGroupLeaderFixture.getStudyGroup()))
                .thenReturn(5);


        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.inviteAlarmStudyGroupMember(leaderUserId, inviteUserId)
        );
        assertEquals(ErrorCode.STUDY_GROUP_FULL, e.getErrorCode());
    }

    @Test
    void 스터디_그룹_가입_알림_전송시_초대한_유저가_없는_경우_에러_반환() {
        // Given
        String leaderUserId = "leaderUserId";
        String inviteUserId = "inviteUserId";
        String studyGroupName = "studyGroupName";
        String description = "description";

        StudyGroup studyGroupFixture = StudyGroupFixture.get(studyGroupName, description);
        UserStudyGroup userStudyGroupLeaderFixture = UserStudyGroupFixture.getLeader(leaderUserId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupLeaderFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(leaderUserId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupLeaderFixture));
        when(userAccountRepository.findById(inviteUserId))
                .thenReturn(Optional.empty());


        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.inviteAlarmStudyGroupMember(leaderUserId, inviteUserId)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹에_가입에_성공한_경우() {
        // Given
        String inviteUserId = "inviteUserId";
        String leaderUserId = "leaderUserId";
        String studyGroupName = "studyGroupName";
        String description = "description";

        StudyGroup studyGroupFixture = StudyGroupFixture.get(studyGroupName, description);
        UserStudyGroup userStudyGroupLeaderFixture = UserStudyGroupFixture.getLeader(leaderUserId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupLeaderFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(leaderUserId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupLeaderFixture));
        when(userAccountRepository.findById(inviteUserId))
                .thenReturn(Optional.of(mock(UserAccount.class)));
        when(studyGroupRepository.findById(userStudyGroupLeaderFixture.getStudyGroup().getId()))
                .thenReturn(Optional.of(studyGroupFixture));


        // Then
        assertDoesNotThrow(() -> studyGroupService.joinStudyGroupMember(inviteUserId, leaderUserId));
    }

    @Test
    void 스터디_그룹에_가입시_로그인하지_않은_경우_에러_반환() {
        // Given
        String inviteUserId = "inviteUserId";
        String leaderUserId = "leaderUserId";

        // When
        when(userAccountRepository.findById(inviteUserId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.joinStudyGroupMember(inviteUserId, leaderUserId)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹에_가입시_인원이_다찬_경우_에러_반환() {
        // Given
        String inviteUserId = "inviteUserId";
        String leaderUserId = "leaderUserId";
        String studyGroupName = "studyGroupName";
        String description = "description";
        StudyGroup studyGroupFixture = StudyGroupFixture.get(studyGroupName, description);

        UserStudyGroup userStudyGroupLeaderFixture = UserStudyGroupFixture.getLeader(leaderUserId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupLeaderFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(leaderUserId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupLeaderFixture));
        when(userStudyGroupRepository.countByStudyGroup(studyGroupFixture))
                .thenReturn(5);

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.joinStudyGroupMember(inviteUserId, leaderUserId)
        );
        assertEquals(ErrorCode.STUDY_GROUP_FULL, e.getErrorCode());
    }

    @Test
    void 스터디_그룹에_가입시_초대자가_스터디_그룹에_탈퇴한_경우_에러_반환() {
        // Given
        String inviteUserId = "inviteUserId";
        String leaderUserId = "leaderUserId";

        // When
        when(userAccountRepository.findById(leaderUserId))
                .thenReturn(Optional.of(mock(UserAccount.class)));
        when(userStudyGroupRepository.findByUserAccount(any()))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.joinStudyGroupMember(inviteUserId, leaderUserId)
        );
        assertEquals(ErrorCode.USER_STUDY_GROUP_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹에_가입시_초대받는_사람이_없는_경우_에러_반환() {
        // Given
        String inviteUserId = "inviteUserId";
        String leaderUserId = "leaderUserId";
        String studyGroupName = "studyGroupName";
        String description = "description";
        StudyGroup studyGroupFixture = StudyGroupFixture.get(studyGroupName, description);

        UserStudyGroup userStudyGroupLeaderFixture = UserStudyGroupFixture.getLeader(leaderUserId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupLeaderFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(leaderUserId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupLeaderFixture));
        when(studyGroupRepository.findById(userStudyGroupLeaderFixture.getStudyGroup().getId()))
                .thenReturn(Optional.of(studyGroupFixture));
        when(userAccountRepository.findById(inviteUserId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.joinStudyGroupMember(inviteUserId, leaderUserId)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹에_가입시_초대한_유저가_리더가_아닌_경우_에러_반환() {
        // Given
        String inviteUserId = "inviteUserId";
        String notLeaderUserId = "notLeaderUserId";
        String studyGroupName = "studyGroupName";
        String description = "description";
        StudyGroup studyGroupFixture = StudyGroupFixture.get(studyGroupName, description);

        UserStudyGroup userStudyGroupMemberFixture = UserStudyGroupFixture.getMember(notLeaderUserId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupMemberFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(notLeaderUserId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupMemberFixture));
        when(studyGroupRepository.findById(userStudyGroupMemberFixture.getStudyGroup().getId()))
                .thenReturn(Optional.of(studyGroupFixture));
        when(userAccountRepository.findById(inviteUserId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.joinStudyGroupMember(inviteUserId, notLeaderUserId)
        );
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 스터디_그룹의_그룹원_삭제가_성공한_경우() {
        // Given
        String userId = "userId";
        String deleteUserId = "deleteUserId";
        String studyGroupName = "studyGroupName";
        String description = "description";
        StudyGroup studyGroupFixture = StudyGroupFixture.get(studyGroupName, description);

        UserStudyGroup userStudyGroupLeaderFixture = UserStudyGroupFixture.getLeader(userId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupLeaderFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupLeaderFixture));
        when(userAccountRepository.findById(deleteUserId))
                .thenReturn(Optional.of(mock(UserAccount.class)));

        // Then
        assertDoesNotThrow(() -> studyGroupService.deleteStudyGroupMember(userId, deleteUserId));
    }

    @Test
    void 스터디_그룹의_그룹원_삭제시_로그인을_안한_경우_에러_반환() {
        // Given
        String userId = "userId";
        String deleteUserId = "deleteUserId";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.deleteStudyGroupMember(userId, deleteUserId)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹의_그룹원_삭제시_가입된_스터디_그룹이_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        String deleteUserId = "deleteUserId";

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(mock(UserAccount.class)));
        when(userStudyGroupRepository.findByUserAccount(any()))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.deleteStudyGroupMember(userId, deleteUserId)
        );
        assertEquals(ErrorCode.USER_STUDY_GROUP_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹의_그룹원_삭제시_삭제할_유저가_없는_경우_에러_반환() {
        // Given
        String userId = "userId";
        String deleteUserId = "deleteUserId";
        String studyGroupName = "studyGroupName";
        String description = "description";
        StudyGroup studyGroupFixture = StudyGroupFixture.get(studyGroupName, description);

        UserStudyGroup userStudyGroupLeaderFixture = UserStudyGroupFixture.getLeader(userId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupLeaderFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupLeaderFixture));
        when(userAccountRepository.findById(deleteUserId))
                .thenReturn(Optional.empty());

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.deleteStudyGroupMember(userId, deleteUserId)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 스터디_그룹의_그룹원_삭제시_리더가_아닌_경우_에러_반환() {
        // Given
        String userId = "userId";
        String deleteUserId = "deleteUserId";
        String studyGroupName = "studyGroupName";
        String description = "description";
        StudyGroup studyGroupFixture = StudyGroupFixture.get(studyGroupName, description);

        UserStudyGroup userStudyGroupMemberFixture = UserStudyGroupFixture.getMember(userId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupMemberFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupMemberFixture));
        when(userAccountRepository.findById(deleteUserId))
                .thenReturn(Optional.of(mock(UserAccount.class)));

        // Then
        DoubleSApplicationException e = assertThrows(
                DoubleSApplicationException.class,
                () -> studyGroupService.deleteStudyGroupMember(userId, deleteUserId)
        );
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 스터디_그룹원_리스트_조회_성공한_경우() {
        // Given
        String userId = "userId";
        String studyGroupName = "studyGroupName";
        String description = "description";
        StudyGroup studyGroupFixture = StudyGroupFixture.get(studyGroupName, description);

        UserStudyGroup userStudyGroupLeaderFixture = UserStudyGroupFixture.getLeader(userId, studyGroupFixture);
        UserAccount userAccount = userStudyGroupLeaderFixture.getUserAccount();

        // When
        when(userAccountRepository.findById(userId))
                .thenReturn(Optional.of(userAccount));
        when(userStudyGroupRepository.findByUserAccount(userAccount))
                .thenReturn(Optional.of(userStudyGroupLeaderFixture));
        when(userStudyGroupRepository.findAllByStudyGroup(studyGroupFixture))
                .thenReturn(List.of());

        // Then
        assertDoesNotThrow(() -> studyGroupService.studyGroupMemberList(userId));
    }
}