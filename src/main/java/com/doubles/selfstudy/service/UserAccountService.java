package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.studygroup.StudyGroupPosition;
import com.doubles.selfstudy.dto.user.UserAccountDto;
import com.doubles.selfstudy.entity.ChatMessage;
import com.doubles.selfstudy.entity.ChatRoom;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.entity.UserStudyGroup;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.*;
import com.doubles.selfstudy.utils.JwtTokenUtils;
import com.doubles.selfstudy.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserStudyGroupRepository userStudyGroupRepository;
    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionBoardCommentRepository questionBoardCommentRepository;
    private final QuestionBoardLikeRepository questionBoardLikeRepository;
    private final TodoRepository todoRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final StudyGroupBoardRepository studyGroupBoardRepository;
    private final StudyGroupBoardCommentRepository studyGroupBoardCommentRepository;
    private final AlarmRepository alarmRepository;
    private final UserAccountCacheRepository userAccountCacheRepository;

    private final BCryptPasswordEncoder encoder;
    private final ServiceUtils serviceUtils;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    // 회원가입
    @Transactional
    public UserAccountDto regist(String userId, String password, String email, String nickname) {
        // 회원 가입하는 userId 중복 체크
        userAccountRepository.findById(userId).ifPresent(it -> {
            throw new DoubleSApplicationException(ErrorCode.DUPLICATED_USER_ID, String.format("'%s' 는 있는 아이디입니다.", userId));
        });

        // 회원 가입 진행
        UserAccount userAccount = userAccountRepository.save(
                UserAccount.of(userId, encoder.encode(password), email, nickname, null));

        return UserAccountDto.fromEntity(userAccount);
    }

    // 로그인
    public String login(String userId, String password) {
        // 회원 가입 체크
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 비밀 번호 체크
        if (!encoder.matches(password, userAccount.getPassword())) {
            throw new DoubleSApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        userAccountCacheRepository.setUserAccount(userAccount);

        // 토큰 생성
        return JwtTokenUtils.createJwtToken(userId, secretKey, expiredTimeMs);
    }
    
    // 유저 정보 조회
    public UserAccountDto getUserInfo(String userId) {
        // 아이디 가져옴
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 스터디 그룹 참여 여부 확인
        Optional<UserStudyGroup> userStudyGroup =
                userStudyGroupRepository.findByUserAccount(userAccount);

        boolean nowStudyGroupInvite = userStudyGroup.isPresent();

        return UserAccountDto.fromEntity(userAccount, nowStudyGroupInvite);
    }

    // 유저 정보 수정
    @Transactional
    public UserAccountDto modifiyUserInfo(String userId, String nickname, String email, String memo) {
        // 유저 정보 가져옴
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        userAccount.setNickname(nickname);
        userAccount.setEmail(email);
        userAccount.setMemo(memo);
        
        // 변경 내용 수정
        return UserAccountDto.fromEntity(userAccountRepository.saveAndFlush(userAccount));
    }

    // 유저 비밀번호 수정
    public UserAccountDto modifiyUserPassword(String userId, String nowPassword, String changePassword) {
        // 유저 정보 가져옴
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 비밀 번호 체크
        if (!encoder.matches(nowPassword, userAccount.getPassword())) {
            throw new DoubleSApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 변경 내용 수정
        userAccount.setPassword(encoder.encode(changePassword));

        return UserAccountDto.fromEntity(userAccountRepository.saveAndFlush(userAccount));
    }

    // 유저 삭제
    @Transactional
    public void deleteUserAccount(String userId) {
        // 유저 정보 가져옴
        UserAccount userAccount = serviceUtils.getUserAccountOrException(userId);

        // 탈퇴시 관리지 계정으로 알림하기 위해 가져옴
        UserAccount admin = serviceUtils.getAdminUserAccountOrException("admin");

        // 알람 삭제
        alarmRepository.deleteAllByUserAccount(userAccount);

        // 유저 스터디 그룹의 리더인 경우 에러 반환
        UserStudyGroup userStudyGroup = userStudyGroupRepository
                .findByUserAccount(userAccount).orElse(null);

        if (userStudyGroup != null) {
            if (userStudyGroup.getPosition() == StudyGroupPosition.Leader) {
                throw new DoubleSApplicationException(
                        ErrorCode.LEADER_NOT_EXIT,
                        "그룹 리더는 삭제할 수 없습니다. 리더를 바꾸거나, 스터디 그룹 삭제 혹은 그룹원을 비우세요"
                );
            }

            studyGroupBoardCommentRepository.deleteAllMyBoardCommentByUserAccount(userAccount);
            studyGroupBoardCommentRepository.deleteAllByUserAccount(userAccount);
            studyGroupBoardRepository.deleteAllByUserAccount(userAccount);
            userStudyGroupRepository.deleteByUserAccount(userAccount);
        }
        
        // 질문 게시글 관련 삭제
        questionBoardLikeRepository.deleteAllByUserAccount(userAccount);
        questionBoardLikeRepository.deleteAllMyBoardLikesByUserAccount(userAccount);
        questionBoardCommentRepository.deleteAllByUserAccount(userAccount);
        questionBoardCommentRepository.deleteAllMyBoardCommentByUserAccount(userAccount);
        questionBoardRepository.deleteAllByUserAccount(userAccount);
        
        // todo 관련 삭제
        todoRepository.deleteAllByUserAccount(userAccount);
        
        // 관련된 채팅방 조회
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByUser(userAccount);

        if (!chatRooms.isEmpty()) {
            for (ChatRoom chatRoom : chatRooms) {
                chatMessageRepository.deleteAllByChatRoom(chatRoom);

                if (chatRoom.getLeaveUserId() != null) {
                    // 채팅방에 남은 사람이 한명이면 채팅방 삭제
                    chatRoomRepository.delete(chatRoom);
                    continue;
                } else if (Objects.equals(chatRoom.getUser1().getUserId(), userAccount.getUserId())) {
                    // 유저1의 탈퇴시 상황
                    chatRoom.setLeaveUserId(chatRoom.getUser1().getUserId());
                    chatRoom.setUser1(admin);
                } else if (Objects.equals(chatRoom.getUser2().getUserId(), userAccount.getUserId())) {
                    // 유저2의 탈퇴시 상황
                    chatRoom.setLeaveUserId(chatRoom.getUser2().getUserId());
                    chatRoom.setUser2(admin);
                }

                // 사용자 나갔음을 알리는 메시지 저장
                ChatMessage message = ChatMessage.of(chatRoom, admin, userAccount.getNickname() + "님이 회원을 탈퇴했습니다..");
                chatMessageRepository.save(message);
            }
        }

        // redis에서 유저 삭제
        userAccountCacheRepository.deleteUserAccount(userId);

        userAccountRepository.deleteById(userId);
    }

    // 토큰 필터 설정에서 좀 더 편하게 사용할 수 있도록 작성
    public UserAccount loadUserByUserId(String userId) {
        return serviceUtils.getUserAccountOrException(userId);
    }
}
