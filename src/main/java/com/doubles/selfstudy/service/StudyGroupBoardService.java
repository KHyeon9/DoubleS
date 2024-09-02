package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.studygroup.StudyGroupBoardDto;
import com.doubles.selfstudy.entity.StudyGroupBoard;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.entity.UserStudyGroup;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.StudyGroupBoardCommentRepository;
import com.doubles.selfstudy.repository.StudyGroupBoardRepository;
import com.doubles.selfstudy.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StudyGroupBoardService {

    private final StudyGroupBoardRepository studyGroupBoardRepository;
    private final StudyGroupBoardCommentRepository studyGroupBoardCommentRepository;
    private final ServiceUtils serviceUtils;

    // 스터디 그룹전용 게시판 리스트 조회
    public Page<StudyGroupBoardDto> studyGroupBoardList(String userId, Pageable pageable) {
        // user 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // user study group 확인
        UserStudyGroup userStudyGroup = serviceUtils.getUserStudyGroupOrException(userAccount);

        return studyGroupBoardRepository
                .findAllByStudyGroupId(userStudyGroup.getStudyGroup().getId(), pageable)
                .map(StudyGroupBoardDto::fromEntity);
    }

    // 스터디 그룹 게시글 상세 조회
    public StudyGroupBoardDto studyGroupBoardDetail(String userId, Long studyGroupBoardId) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);
        
        // 스터디 그룹 유저인지 확인
        UserStudyGroup userStudyGroup = serviceUtils.getUserStudyGroupOrException(userAccount);

        // 스터디 그룹 게시판 조회
        StudyGroupBoard studyGroupBoard = serviceUtils.getStudyGroupBoardOrException(studyGroupBoardId);

        if (userStudyGroup.getStudyGroup() != studyGroupBoard.getStudyGroup()) {
            throw new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION,
                        "스터디 그룹에 가입되어 있지 않습니다."
                    );
        }

        // 댓글 수 조회
        int comments = studyGroupBoardCommentRepository.countByStudyGroupBoard(studyGroupBoard);

        return StudyGroupBoardDto.fromEntity(studyGroupBoard, comments);
    }

    // 스터디 그룹 게시글 생성
    @Transactional
    public void createStudyGroupBoard(String userId, String title, String content) {
        // user 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // user study group 확인
        UserStudyGroup userStudyGroup = serviceUtils.getUserStudyGroupOrException(userAccount);

        StudyGroupBoard studyGroupBoard = StudyGroupBoard.of(
                userAccount,
                userStudyGroup.getStudyGroup(),
                title,
                content
        );

        studyGroupBoardRepository.save(studyGroupBoard);
    }

    // 스터디 그룹 게시글 수정
    @Transactional
    public StudyGroupBoardDto modifyStudyGroupBoard(String userId, Long studyGroupBoardId, String title, String content) {
        // user 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // study group board 조회
        StudyGroupBoard studyGroupBoard = serviceUtils.getStudyGroupBoardOrException(studyGroupBoardId);

        if (studyGroupBoard.getUserAccount() != userAccount) {
            throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION, String.format(
                    "%s는 권한이 스터디 게시판 번호: '%s' 에 대해서 권한이 없습니다.",
                    userId,
                    studyGroupBoardId
                )
            );
        }

        studyGroupBoard.setTitle(title);
        studyGroupBoard.setContent(content);

        return StudyGroupBoardDto.fromEntity(
                studyGroupBoardRepository
                        .saveAndFlush(studyGroupBoard)
        );
    }

    // 스터그 그룹 게시글 삭제
    @Transactional
    public void deleteStudyGroupBoard(String userId, Long studyGroupBoardId) {
        // user 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // study group board 조회
        StudyGroupBoard studyGroupBoard = serviceUtils.getStudyGroupBoardOrException(studyGroupBoardId);

        if (studyGroupBoard.getUserAccount() != userAccount) {
            throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION, String.format(
                    "%s는 권한이 스터디 게시판 번호: '%s' 에 대해서 권한이 없습니다.",
                    userId,
                    studyGroupBoardId
                )
            );
        }

        studyGroupBoardCommentRepository.deleteAllByStudyGroupBoard(studyGroupBoard);
        studyGroupBoardRepository.delete(studyGroupBoard);
    }
}
