package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.studygroup.StudyGroupDto;
import com.doubles.selfstudy.dto.studygroup.StudyGroupPosition;
import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.StudyGroup;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.entity.UserStudyGroup;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.StudyGroupBoardRepository;
import com.doubles.selfstudy.repository.StudyGroupRepository;
import com.doubles.selfstudy.repository.UserStudyGroupRepository;
import com.doubles.selfstudy.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;
    private final UserStudyGroupRepository userStudyGroupRepository;
    private final StudyGroupBoardRepository studyGroupBoardRepository;
    private final ServiceUtils serviceUtils;

    // 그룹원 리스트 조회
    public List<UserAccountDto> studyGroupMemberList(String userId) {
        // user 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // user study group 확인
        UserStudyGroup userStudyGroup = serviceUtils.getUserStudyGroupOrException(userAccount);

        List<UserAccount> members = userStudyGroupRepository.findAllByStudyGroupId(userStudyGroup.getId());

        return members
                .stream()
                .map(UserAccountDto::fromEntity)
                .toList();
    }

    // 스터디 그룹 생성
    @Transactional
    public StudyGroupDto createStudyGroup(String userId, String studyGroupName, String description) {
        // user 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // study group 확인
        userStudyGroupRepository.findByUserAccount(userAccount).ifPresent(it -> {
            throw new DoubleSApplicationException(
                    ErrorCode.DUPLICATED_STUDY_GROUP,
                    String.format("'%s' 는 스터디 그룹이 있습니다.", userId)
            );
        });

        StudyGroup studyGroup = studyGroupRepository.save(StudyGroup.of(studyGroupName, description));
        userStudyGroupRepository.save(
                UserStudyGroup.of(userAccount, StudyGroupPosition.Leader, studyGroup)
        );

        return StudyGroupDto.fromEntity(studyGroup);
    }

    // 스터디 그룹 정보 수정
    @Transactional
    public StudyGroupDto modifyStudyGroup(String userId, String studyGroupName, String description) {
        // study group 권한 확인
        UserStudyGroup userStudyGroup = serviceUtils.getUserStudyGroupAndPermissionCheck(userId);
        
        StudyGroup studyGroup = serviceUtils.getStudyGroupOrException(
                userStudyGroup.getStudyGroup().getId()
        );

        studyGroup.setStudyGroupName(studyGroupName);
        studyGroup.setDescription(description);

        return StudyGroupDto.fromEntity(
                studyGroupRepository.saveAndFlush(studyGroup)
        );
    }

    // 스터디 그룹 삭제
    @Transactional
    public void deleteStudyGroup(String userId) {
        // study group 권한 확인
        UserStudyGroup userStudyGroup = serviceUtils.getUserStudyGroupAndPermissionCheck(userId);

        studyGroupBoardRepository.deleteAllByStudyGroup(userStudyGroup.getStudyGroup());
        userStudyGroupRepository.deleteById(userStudyGroup.getId());
        studyGroupRepository.deleteById(userStudyGroup.getStudyGroup().getId());
    }

    // 스터디 그룹 초대
    @Transactional
    public void inviteStudyGroup(String userId, String inviteUserId) {
        // study group 권한 확인
        UserStudyGroup userStudyGroup = serviceUtils.getUserStudyGroupAndPermissionCheck(userId);

        // 초대할 user 확인
        UserAccount inviteMemberAccount = serviceUtils.getUserAccountOrException(inviteUserId);

        StudyGroup studyGroup = serviceUtils.getStudyGroupOrException(userStudyGroup.getStudyGroup().getId());

        UserStudyGroup inviteUserStudyGroup = UserStudyGroup.of(inviteMemberAccount, StudyGroupPosition.Member, studyGroup);

        userStudyGroupRepository.save(inviteUserStudyGroup);
    }

    // 스터디 그룹원 삭제
    @Transactional
    public void deleteStudyGroup(String userId, String deleteUserId) {
        // 유저 권한 확인
        serviceUtils.getUserStudyGroupAndPermissionCheck(userId);

        // 삭제할 유저확인
        UserAccount deleteMemberAccount = serviceUtils.getUserAccountOrException(deleteUserId);

        userStudyGroupRepository.deleteByUserAccount(deleteMemberAccount);
    }
}
