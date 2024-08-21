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
        // user 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // study group 권한 확인
        UserStudyGroup userStudyGroup = serviceUtils.getUserStudyGroupOrException(userAccount);

        if (userStudyGroup.getPosition() != StudyGroupPosition.Leader) {
            throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION,
                    String.format("%s는 해당 스터디 그룹에 대한 권한이 없습니다.", userId)
            );
        }
        
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
        // user 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // study group 권한 확인
        UserStudyGroup userStudyGroup = serviceUtils.getUserStudyGroupOrException(userAccount);

        if (userStudyGroup.getPosition() != StudyGroupPosition.Leader) {
            throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION,
                    String.format("%s는 해당 스터디 그룹에 대한 권한이 없습니다.", userId)
            );
        }

        studyGroupBoardRepository.deleteAllByStudyGroup(userStudyGroup.getStudyGroup());
        userStudyGroupRepository.deleteById(userStudyGroup.getId());
        studyGroupRepository.deleteById(userStudyGroup.getStudyGroup().getId());
    }

    // 스터디 그룹원 삭제
    @Transactional
    public void deleteStudyGroup(String userId, String deleteUserId) {
        // user 확인
        UserAccount leaderAccount = serviceUtils.getUserAccountOrException(userId);
        UserAccount deleteMemberAccount = serviceUtils.getUserAccountOrException(deleteUserId);

        // study group 권한 확인
        UserStudyGroup userStudyGroup = serviceUtils.getUserStudyGroupOrException(leaderAccount);

        if (userStudyGroup.getPosition() != StudyGroupPosition.Leader) {
            throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION,
                    String.format("%s는 해당 스터디 그룹에 대한 권한이 없습니다.", userId)
            );
        }

        userStudyGroupRepository.deleteByUserAccount(deleteMemberAccount);
    }
}
