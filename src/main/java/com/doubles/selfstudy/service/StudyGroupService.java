package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.alarm.AlarmType;
import com.doubles.selfstudy.dto.studygroup.StudyGroupDto;
import com.doubles.selfstudy.dto.studygroup.StudyGroupPosition;
import com.doubles.selfstudy.dto.studygroup.UserStudyGroupDto;
import com.doubles.selfstudy.entity.Alarm;
import com.doubles.selfstudy.entity.StudyGroup;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.entity.UserStudyGroup;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.*;
import com.doubles.selfstudy.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;
    private final UserStudyGroupRepository userStudyGroupRepository;
    private final StudyGroupBoardRepository studyGroupBoardRepository;
    private final StudyGroupBoardCommentRepository studyGroupBoardCommentRepository;
    private final AlarmRepository alarmRepository;

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
        userStudyGroupRepository.deleteAllByStudyGroup(userStudyGroup.getStudyGroup());
        studyGroupRepository.deleteById(userStudyGroup.getStudyGroup().getId());
    }

    // 스터디 그룹 권한 확인 및 알림 전송
    // TODO: 테스트 필요
    @Transactional
    public void inviteAlarmStudyGroupMember(String userId, String inviteUserId) {
        // study group 권한 확인
        UserStudyGroup userStudyGroup = serviceUtils.getUserStudyGroupAndPermissionCheck(userId);

        if (userStudyGroupRepository.countByStudyGroup(userStudyGroup.getStudyGroup()) >= 5) {
            throw new DoubleSApplicationException(
                    ErrorCode.STUDY_GROUP_FULL,
                    "해당 스터디 그룹은 이미 최대 인원입니다."
            );
        }

        // 초대할 user 확인
        UserAccount inviteMemberAccount = serviceUtils.getUserAccountOrException(inviteUserId);

        // 알람 생성
        if (alarmRepository.existsByUserAccountAndFromUserIdAndAlarmType(
                inviteMemberAccount, userId, AlarmType.INVITE_STUDY_GROUP)) {
            throw new DoubleSApplicationException(
                    ErrorCode.ALREADY_STUDY_GROUP_INVITE,
                    "이미 스터디 그룹에 초대했습니다."
            );
        }
        Alarm inviteAlarm = Alarm.of(inviteMemberAccount, AlarmType.INVITE_STUDY_GROUP, userId, userStudyGroup.getStudyGroup().getId(), userId);
        alarmRepository.save(inviteAlarm);
    }

    // 스터디 그룹 초대
    // TODO: 위에 알람이 추가됨으로써 수정 가능성이 생김
    @Transactional
    public void joinStudyGroupMember(String inviteUserId, String leaderUserId) {
        // study group 권한 확인
        UserStudyGroup userStudyGroup = serviceUtils.getUserStudyGroupAndPermissionCheck(leaderUserId);

        if (userStudyGroupRepository.countByStudyGroup(userStudyGroup.getStudyGroup()) >= 5) {
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

    // 스터디 그룹 리더 변경
    @Transactional
    public UserStudyGroupDto changeStudyGroupLeader(String nowLeaderId, String changeLeaderId) {
        // 리더 확인
        UserStudyGroup nowLeader = serviceUtils.getUserStudyGroupAndPermissionCheck(nowLeaderId);

        // 리더로 변경될 유저 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(changeLeaderId);
        UserStudyGroup changeLeader = serviceUtils.getUserStudyGroupOrException(userAccount);

        if (!Objects.equals(nowLeader.getStudyGroup().getId(), changeLeader.getStudyGroup().getId())) {
            throw new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION, "스터디 그룹이 다릅니다.");
        }

        // 리더 변경
        nowLeader.setPosition(StudyGroupPosition.Member);
        changeLeader.setPosition(StudyGroupPosition.Leader);

        // 리더가 된 사람에게 알람 전송
        Alarm alarm = Alarm.of(
                userAccount,
                AlarmType.CHANGE_LEADER,
                nowLeaderId,
                nowLeader.getStudyGroup().getId(),
                nowLeaderId
        );

        alarmRepository.save(alarm);

        return  UserStudyGroupDto.fromEntity(userStudyGroupRepository.saveAndFlush(changeLeader));
    }

    // 스터디 그룹원 강제 퇴장
    @Transactional
    public void forceExitStudyGroupMember(String userId, String deleteUserId) {
        // 유저 권한 확인 및 데이터 가져오기
        serviceUtils.getUserStudyGroupAndPermissionCheck(userId);

        // 삭제할 유저확인
        UserAccount deleteMemberAccount = serviceUtils.getUserAccountOrException(deleteUserId);

        userStudyGroupRepository.deleteByUserAccount(deleteMemberAccount);
    }

    // 스터디 그룹 퇴장
    @Transactional
    public void exitStudyGroupMySelf(String userId) {
        // 삭제할 유저확인
        UserAccount deleteMemberAccount = serviceUtils.getUserAccountOrException(userId);

        // 그룹에 가입 되어 있는지 확인
        UserStudyGroup userStudyGroup = serviceUtils.getUserStudyGroupOrException(deleteMemberAccount);

        if (userStudyGroup.getPosition() == StudyGroupPosition.Leader) {
            if (userStudyGroupRepository.countByStudyGroup(userStudyGroup.getStudyGroup()) == 1) {
                deleteStudyGroup(userId);
                return;
            }
            throw new DoubleSApplicationException(
                    ErrorCode.LEADER_NOT_EXIT,
                    "그룹원이 있으면 리더는 나갈 수 없습니다. 리더를 바꾸거나 그룹원을 비우세요."
            );
        }

        // 삭제
        userStudyGroupRepository.deleteByUserAccount(deleteMemberAccount);
    }
}
