package com.doubles.selfstudy.utils;

import com.doubles.selfstudy.dto.studygroup.StudyGroupPosition;
import com.doubles.selfstudy.dto.user.RoleType;
import com.doubles.selfstudy.entity.*;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class ServiceUtils {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountCacheRepository userAccountCacheRepository;
    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionBoardCommentRepository questionBoardCommentRepository;
    private final NoticeBoardRepository noticeBoardRepository;
    private final TodoRepository todoRepository;
    private final UserStudyGroupRepository userStudyGroupRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final StudyGroupBoardRepository studyGroupBoardRepository;
    private final StudyGroupBoardCommentRepository studyGroupBoardCommentRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public UserAccount getUserAccountOrException(String userId) {
        // 유저 정보 가져오면서 못 찾는 경우 검사
        UserAccount userAccount = userAccountCacheRepository.getUserAccount(userId).orElseGet(() ->
                userAccountRepository.findById(userId).orElseThrow(
                        () -> new DoubleSApplicationException(
                                ErrorCode.USER_NOT_FOUND, String.format("%s를 찾지 못했습니다", userId))
                )
        );
        userAccountCacheRepository.setUserAccount(userAccount);

        return userAccount;
    }

    public UserAccount getAdminUserAccountOrException(String userId) {
        // 유저 정보 가져오가
        UserAccount userAccount = getUserAccountOrException(userId);

        // 유저 권한 확인
        if (!Objects.equals(userAccount.getRoleType().getRoleName(), RoleType.ADMIN.getRoleName())) {
            throw new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s는 관리자가 아닙니다.", userId));
        }

        return userAccount;
    }

    public QuestionBoard getQuestionBoardOrException(Long questionBoardId) {
        // 질문 게시글 가져오면서 못 찾는 경우 검사
        return questionBoardRepository.findById(questionBoardId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND, String.format("게시글 번호: '%s' 를 찾지 못했습니다.", questionBoardId))
        );
    }

    public QuestionBoardComment getQuestionBoardCommentOrException(Long questionBoardCommentId) {
        // 댓글 가져오기
        return questionBoardCommentRepository.findById(questionBoardCommentId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.COMMENT_NOT_FOUND, String.format("댓글번호: '%s' 를 찾지 못했습니다.", questionBoardCommentId))
        );
    }

    public NoticeBoard getNoticeBoardOrException(Long noticeBoardId) {
        // 공지사항 글을 가져오면서 못 찾는 경우 검사
        return noticeBoardRepository.findById(noticeBoardId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND, String.format("공지사항 %d번을 찾지 못했습니다.", noticeBoardId)
                )
        );
    }
    
    public Todo getTodoOrException(Long todoId) {
        // todo 정보 가져오면서 검사
        return todoRepository.findById(todoId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.TODO_NOT_FOUND, String.format("todo %d번을 찾지 못했습니다.", todoId)
                )
        );
    }

    public UserStudyGroup getUserStudyGroupOrException(UserAccount userAccount) {
        // UserStudyGroup 정보 가져오면서 검사
        return userStudyGroupRepository.findByUserAccount(userAccount).orElseThrow(() ->
                    new DoubleSApplicationException(
                            ErrorCode.USER_STUDY_GROUP_NOT_FOUND,
                            String.format("%s에 대한 User Study Group를 찾지 못했습니다.", userAccount.getUserId())
                )
        );
    }

    public StudyGroup getStudyGroupOrException(Long studyGroupId) {
        // study group 가져오면서 검사
        return studyGroupRepository.findById(studyGroupId).orElseThrow(() ->
                    new DoubleSApplicationException(
                            ErrorCode.STUDY_GROUP_NOT_FOUND,
                            String.format(
                                    "%d에 대한 Study Group를 찾지 못했습니다.",
                                    studyGroupId
                            )
                )
        );
    }

    // study group 권한 확인
    public UserStudyGroup getUserStudyGroupAndPermissionCheck(String userId) {
        // user 확인
        UserAccount userAccount = getUserAccountOrException(userId);

        // study group 권한 확인
        UserStudyGroup userStudyGroup = getUserStudyGroupOrException(userAccount);

        if (userStudyGroup.getPosition() != StudyGroupPosition.Leader) {
            throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION,
                    String.format("%s는 해당 스터디 그룹에 대한 권한이 없습니다.", userId)
            );
        }

        return userStudyGroup;
    }

    // 스터디 그룹 게시판 글 상세 조회
    public StudyGroupBoard getStudyGroupBoardOrException(Long studyGroupBoardId) {
        return studyGroupBoardRepository.findById(studyGroupBoardId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND,
                        String.format("스터디 그룹 게시글 %d번을 찾지 못했습니다.", studyGroupBoardId)
                )
        );
    }

    // 스터디 그룹 게시글의 댓글 조회
    public StudyGroupBoardComment getStudyGroupBoardCommentOrException(Long studyGroupBoardCommentId) {
        return studyGroupBoardCommentRepository.findById(studyGroupBoardCommentId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.COMMENT_NOT_FOUND,
                        String.format("스터디 그룹 게시글의 댓글 %d번을 찾지 못했습니다.", studyGroupBoardCommentId)
                )
        );
    }

    // 채팅룸 조회
    public ChatRoom getChatRoomOrException(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() ->
                new DoubleSApplicationException(ErrorCode.CHAT_ROOM_NOT_FOUND,
                    String.format("채팅룸 %d번을 찾지 못했습니다.", chatRoomId)
                )
        );
    }

    // 채팅룸을 유저 아이디 2개로 조회
    public ChatRoom getChatRoomByUserIdsOrException(String userId1, String userId2) {
        return chatRoomRepository.findByUserIds(userId1, userId2).orElseThrow(() ->
            new DoubleSApplicationException(ErrorCode.CHAT_ROOM_NOT_FOUND,
                   "채팅룸을 찾지 못했습니다."
            )
        );
    }

    // 채팅룸 id로 마지막 메세지 검색
    public ChatMessage getChatMessageByChatRoomIdOrNull(Long chatRoomId) {
        return chatMessageRepository.findLastMessageByChatRoomId(chatRoomId).orElse(null);
    }
}
