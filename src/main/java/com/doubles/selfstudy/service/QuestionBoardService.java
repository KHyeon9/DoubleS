package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.alarm.AlarmType;
import com.doubles.selfstudy.dto.question.QuestionBoardDto;
import com.doubles.selfstudy.dto.question.QuestionBoardTag;
import com.doubles.selfstudy.entity.Alarm;
import com.doubles.selfstudy.entity.QuestionBoard;
import com.doubles.selfstudy.entity.QuestionBoardLike;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.AlarmRepository;
import com.doubles.selfstudy.repository.QuestionBoardCommentRepository;
import com.doubles.selfstudy.repository.QuestionBoardLikeRepository;
import com.doubles.selfstudy.repository.QuestionBoardRepository;
import com.doubles.selfstudy.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class QuestionBoardService {

    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionBoardLikeRepository questionBoardLikeRepository;
    private final QuestionBoardCommentRepository questionBoardCommentRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmService alarmService;
    private final ServiceUtils serviceUtils;

    // 질문 게시글 리스트
    public Page<QuestionBoardDto> questionBoardList(Pageable pageable) {
        // 모든 질문 게시글들 가져오기
        Page<Object[]> results = questionBoardRepository.findAllQuestionBoardWithCounts(pageable);

        return results.map(result -> QuestionBoardDto.fromEntity((QuestionBoard) result[0], (Long) result[1], (Long) result[2]));
    }

    // 질문 게시글 태그 검색
    public Page<QuestionBoardDto> questionBoardListByTag(String tag, Pageable pageable) {
        QuestionBoardTag tagValue = QuestionBoardTag.fromString(tag);
        Page<Object[]> results = questionBoardRepository.findAllQuestionBoardWithCountsByTag(tagValue, pageable);

        return results.map(result -> QuestionBoardDto.fromEntity((QuestionBoard) result[0], (Long) result[1], (Long) result[2]));
    }

    // 나의 질문 게시글 리스트 조회
    public Page<QuestionBoardDto> myQuestionBoardList(String userId, Pageable pageable) {
        // 유저 확인
        serviceUtils.getUserAccountOrException(userId);
        
        // 나의 질문 게시글들 가져오기
        Page<Object[]> results = questionBoardRepository.findAllMyQuestionBoardWithCounts(userId, pageable);

        return results.map(result -> QuestionBoardDto.fromEntity((QuestionBoard) result[0], (Long) result[1], (Long) result[2]));
    }

    // 나의 질문 게시글 리스트 태그로 조회
    public Page<QuestionBoardDto> myQuestionBoardListByTag(String userId, String tag, Pageable pageable) {
        // 유저 확인
        serviceUtils.getUserAccountOrException(userId);

        // 태그 변환
        QuestionBoardTag tagValue = QuestionBoardTag.fromString(tag);

        // 나의 질문 게시글들 가져오기
        Page<Object[]> results = questionBoardRepository.findAllMyQuestionBoardWithCountsByTag(userId, tagValue, pageable);

        return results.map(result -> QuestionBoardDto.fromEntity((QuestionBoard) result[0], (Long) result[1], (Long) result[2]));
    }

    // 프로필의 질문 게시글 조회
    public List<QuestionBoardDto> profileQuestionBoardList(String userId) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // profile에 사용할 질문 게시글 가져오기
        List<QuestionBoard> questionBoardList = questionBoardRepository
                .findTop4ByUserAccountOrderByCreatedAtDesc(userAccount);

        return questionBoardList.stream().map(QuestionBoardDto::fromEntity).toList();
    }

    // 질문 게시글 상세 조회
    public QuestionBoardDto questionBoardDetail(Long questionBoardId) {
        // 질문 게시글 확인
        QuestionBoard questionBoard = serviceUtils.getQuestionBoardOrException(questionBoardId);

        // 조회수 증가
        questionBoard.plusViewCount(); 
        questionBoardRepository.save(questionBoard);

        // 좋아요와 댓글 갯수 가져오기
        Long likes = questionBoardLikeRepository.countByQuestionBoard(questionBoard);
        Long comments = questionBoardCommentRepository.countByQuestionBoard(questionBoard);

        return QuestionBoardDto.fromEntity(questionBoard, likes, comments);
    }

    // 질문 게시글 생성
    @Transactional
    public void createQuestionBoard(String userId, String title, String content, String tag) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);
        QuestionBoardTag questionBoardTag = QuestionBoardTag.fromString(tag);
        // 질문 게시글 저장
        questionBoardRepository.save(
                QuestionBoard.of(userAccount, title, content, questionBoardTag)
        );
    }

    // 질문 게시글 수정
    @Transactional
    public QuestionBoardDto modifyQuestionBoard(String userId, Long questionBoardId, String title, String content, String tag) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 질문 게시글 확인
        QuestionBoard questionBoard = serviceUtils.getQuestionBoardOrException(questionBoardId);;

        // 질문 게시글 권한 확인
        if (!Objects.equals(questionBoard.getUserAccount().getUserId(), userAccount.getUserId())) {
                throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION, String.format(
                            "%s는 권한이 게시판 번호: '%s' 에 대해서 권한이 없습니다.",
                            userId,
                            questionBoardId
                    )
                );
        }
        
        // 변경 내용 수정
        questionBoard.setTitle(title);
        questionBoard.setContent(content);
        questionBoard.setTag(QuestionBoardTag.fromString(tag));

        return QuestionBoardDto.fromEntity(questionBoardRepository.saveAndFlush(questionBoard));
    }

    // 질문 게시글 삭제
    @Transactional
    public void deleteQuestionBoard(String userId, Long questionBoardId) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 질문 게시글 확인
        QuestionBoard questionBoard = serviceUtils.getQuestionBoardOrException(questionBoardId);;

        // 질문 게시판 권한 확인
        if (!Objects.equals(questionBoard.getUserAccount().getUserId(), userAccount.getUserId())) {
                throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION, String.format(
                        "%s는 권한이 게시판 번호: '%s' 에 대해서 권한이 없습니다.",
                        userId,
                        questionBoardId
                    )
                );
        }

        // 게시글과 관련된 모든 것 삭제
        alarmRepository.deleteByTargetIdAndAlarmType(questionBoardId, AlarmType.NEW_LIKE_ON_POST);
        alarmRepository.deleteByTargetIdAndAlarmType(questionBoardId, AlarmType.NEW_COMMENT_ON_POST);
        questionBoardLikeRepository.deleteAllByQuestionBoardId(questionBoardId);
        questionBoardCommentRepository.deleteAllByQuestionBoardId(questionBoardId);
        questionBoardRepository.delete(questionBoard);
    }



    // 좋아요 기능
    @Transactional
    public void questionBoardLike(String userId, Long questionBoardId) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 질문 게시글 확인
        QuestionBoard questionBoard = serviceUtils.getQuestionBoardOrException(questionBoardId);

        // 좋아요 확인
        questionBoardLikeRepository.findByUserAccountAndQuestionBoard(userAccount, questionBoard)
                .ifPresent(questionBoardLike -> {
                    throw new DoubleSApplicationException(
                            ErrorCode.ALREADY_LIKED,
                            String.format("유저 %s는 %d번 게시글에 이미 좋아요를 눌렀습니다.", userId, questionBoardId)
                    );
                });

        // 좋아요 저장
        questionBoardLikeRepository.save(QuestionBoardLike.of(questionBoard, userAccount));
        
        // 작성자가 자기 게시글 좋아요를 제외하고 알람 저장
        if (!Objects.equals(questionBoard.getUserAccount().getUserId(), userId)) {
            Alarm alarm = alarmRepository.save(
                    Alarm.of(
                    questionBoard.getUserAccount(),
                    AlarmType.NEW_LIKE_ON_POST,
                    userId,
                    questionBoardId,
                    questionBoard.getTitle()
                )
            );

            alarmService.alarmSend(alarm.getId(), questionBoard.getUserAccount().getUserId());
        }
    }

    // 좋아요 삭제
    @Transactional
    public void questionBoardDisLike(String userId, Long questionBoardId) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 질문 게시글 확인
        QuestionBoard questionBoard = serviceUtils.getQuestionBoardOrException(questionBoardId);

        // 좋아요 저장
        questionBoardLikeRepository.deleteByQuestionBoardAndUserAccount(questionBoard, userAccount);
    }
    
    // 좋아요 갯수 조회
    public Long questionBoardLikeCount(Long questionBoardId) {
        // 질문 게시글 확인
        QuestionBoard questionBoard = serviceUtils.getQuestionBoardOrException(questionBoardId);

        return questionBoardLikeRepository.countByQuestionBoard(questionBoard);
    }
}
