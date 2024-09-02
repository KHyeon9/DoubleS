package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.studygroup.StudyGroupDto;
import com.doubles.selfstudy.dto.studygroup.StudyGroupPosition;
import com.doubles.selfstudy.dto.studygroup.UserStudyGroupDto;
import com.doubles.selfstudy.entity.StudyGroup;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.entity.UserStudyGroup;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.StudyGroupBoardCommentRepository;
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
    private final StudyGroupBoardCommentRepository studyGroupBoardCommentRepository;

    private final ServiceUtils serviceUtils;

    // 스터디 그룹 조회
    public StudyGroupDto studyGroupInfo(String userId) {
        // user 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // user study group 확인
        UserStudyGroup userStudyGroup = serviceUtils.getUserStudyGroupOrException(userAccount);

        return StudyGroupDto.fromEntity(
                userStudyGroup.getStudyGroup()
        );
    }

    // 그룹원 리스트 조회
    public List<UserStudyGroupDto> studyGroupMemberList(String userId) {
        // user 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // user study group 확인
        UserStudyGroup userStudyGroup = serviceUtils.getUserStudyGroupOrException(userAccount);

        List<UserStudyGroup> userStudyGroups = userStudyGroupRepository.findAllByStudyGroup(userStudyGroup.getStudyGroup());

        return userStudyGroups
                .stream()
                .map(UserStudyGroupDto::fromEntity)
                .toList();
    }

    // 스터디 그룹 생성
    @Transactional
    public void createStudyGroup(String userId, String studyGroupName, String description) {
        // user 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // study group 확인
        userStudyGroupRepository.findByUserAccount(userAccount).ifPresent(it -> {
            throw new DoubleSApplicationException(
                    ErrorCode.DUPLICATED_STUDY_GROUP,
                    String.format("'%s' 는 스터디 그룹이 있습니다.", userId)
            );
        });

        // 스터디 그룹 생성
        StudyGroup studyGroup = studyGroupRepository.save(StudyGroup.of(studyGroupName, description));

        // 스터디 그룹에 유저 등록
        userStudyGroupRepository.save(
                UserStudyGroup.of(userAccount, StudyGroupPosition.Leader, studyGroup)
        );
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

        // 삭제되는 게시글들 조회
        List<Long> boardIds = studyGroupBoardRepository
                .findBoardIdsByStudyGroupId(userStudyGroup.getStudyGroup().getId());
        
        // 댓글 한번에 삭제
        if (!boardIds.isEmpty()) {
            studyGroupBoardCommentRepository.deleteAllByStudyGroupBoardIdIn(boardIds);
        }

        studyGroupBoardRepository.deleteAllByStudyGroup(userStudyGroup.getStudyGroup());
        userStudyGroupRepository.deleteById(userStudyGroup.getId());
        studyGroupRepository.deleteById(userStudyGroup.getStudyGroup().getId());
    }

    // 스터디 그룹 초대
    @Transactional
    public void inviteStudyGroupMember(String userId, String inviteUserId) {
        // study group 권한 확인
        UserStudyGroup userStudyGroup = serviceUtils.getUserStudyGroupAndPermissionCheck(userId);

        if (userStudyGroupRepository.countByStudyGroup(userStudyGroup.getStudyGroup()) > 5) {
            throw new DoubleSApplicationException(
                    ErrorCode.STUDY_GROUP_FULL,
                    "해당 스터디 그룹은 이미 최대 인원입니다."
            );
        }

        // 초대할 user 확인
        UserAccount inviteMemberAccount = serviceUtils.getUserAccountOrException(inviteUserId);

        StudyGroup studyGroup = serviceUtils.getStudyGroupOrException(userStudyGroup.getStudyGroup().getId());

        UserStudyGroup inviteUserStudyGroup = UserStudyGroup.of(inviteMemberAccount, StudyGroupPosition.Member, studyGroup);

        userStudyGroupRepository.save(inviteUserStudyGroup);
    }

    // 스터디 그룹원 삭제
    @Transactional
    public void deleteStudyGroupMember(String userId, String deleteUserId) {
        // 유저 권한 확인
        serviceUtils.getUserStudyGroupAndPermissionCheck(userId);

        // 삭제할 유저확인
        UserAccount deleteMemberAccount = serviceUtils.getUserAccountOrException(deleteUserId);

        userStudyGroupRepository.deleteByUserAccount(deleteMemberAccount);
    }
}
