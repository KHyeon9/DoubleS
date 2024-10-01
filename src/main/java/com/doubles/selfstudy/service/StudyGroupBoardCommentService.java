package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.alarm.AlarmType;
import com.doubles.selfstudy.dto.studygroup.StudyGroupBoardCommentDto;
import com.doubles.selfstudy.entity.Alarm;
import com.doubles.selfstudy.entity.StudyGroupBoard;
import com.doubles.selfstudy.entity.StudyGroupBoardComment;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.AlarmRepository;
import com.doubles.selfstudy.repository.StudyGroupBoardCommentRepository;
import com.doubles.selfstudy.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class StudyGroupBoardCommentService {

    private final StudyGroupBoardCommentRepository studyGroupBoardCommentRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmService alarmService;
    private final ServiceUtils serviceUtils;

    // 스터디 그룹 게시글의 댓글들 조회
    public Page<StudyGroupBoardCommentDto> studyGroupBoardCommentList(Long studyGroupBoardId, Pageable pageable) {
        // study group board 검사 및 가져오기
        StudyGroupBoard studyGroupBoard = serviceUtils.getStudyGroupBoardOrException(studyGroupBoardId);


        return studyGroupBoardCommentRepository
                .findAllByStudyGroupBoard(studyGroupBoard, pageable)
                .map(StudyGroupBoardCommentDto::fromEntity);
    }

    // 스터디 그룹 게시글의 댓글 생성
    @Transactional
    public void createStudyGroupBoardCommnet(String userId, Long studyGroupBoardId, String comment) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 스터디 그룹 게시글 확인
        StudyGroupBoard studyGroupBoard = serviceUtils.getStudyGroupBoardOrException(studyGroupBoardId);

        // 댓글 저장
        studyGroupBoardCommentRepository.save(
                StudyGroupBoardComment.of(userAccount, studyGroupBoard, comment)
        );
        
        // 알람 저장
        Alarm alarm = alarmRepository.save(Alarm.of(
                studyGroupBoard.getUserAccount(),
                AlarmType.NEW_COMMENT_ON_STUDY_GROUP_POST,
                userId,
                studyGroupBoardId,
                studyGroupBoard.getTitle()
            )
        );

        alarmService.alarmSend(alarm.getId(), studyGroupBoard.getUserAccount().getUserId());
    }

    // 스터디 그룹 게시글의 댓글 수정
    @Transactional
    public StudyGroupBoardCommentDto modifyStudyGroupBoardComment(String userId, Long studyGroupBoardCommentId, String comment) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 댓글 확인
        StudyGroupBoardComment studyGroupBoardComment =
                serviceUtils.getStudyGroupBoardCommentOrException(studyGroupBoardCommentId);

        // 댓글 작성자 확인
        if (!Objects.equals(studyGroupBoardComment.getUserAccount().getUserId(), userAccount.getUserId())) {
            throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION, String.format(
                    "%s는 권한이 댓글 번호: '%s' 에 대해서 권한이 없습니다.",
                    userId,
                    studyGroupBoardCommentId
                )
            );
        }

        studyGroupBoardComment.setComment(comment);

        return StudyGroupBoardCommentDto.fromEntity(
                studyGroupBoardCommentRepository
                        .saveAndFlush(studyGroupBoardComment)
        );
    }

    // 스터디 그룹 게시글의 댓글 삭제
    @Transactional
    public void deleteStudyGroupBoardComment(String userId, Long studyGroupBoardCommentId) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 댓글 확인
        StudyGroupBoardComment studyGroupBoardComment =
                serviceUtils.getStudyGroupBoardCommentOrException(studyGroupBoardCommentId);

        // 댓글 작성자 확인
        if (!Objects.equals(studyGroupBoardComment.getUserAccount().getUserId(), userAccount.getUserId())) {
            throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION, String.format(
                    "%s는 권한이 댓글 번호: '%s' 에 대해서 권한이 없습니다.",
                    userId,
                    studyGroupBoardCommentId
                )
            );
        }

        studyGroupBoardCommentRepository.deleteById(studyGroupBoardCommentId);
    }
}
