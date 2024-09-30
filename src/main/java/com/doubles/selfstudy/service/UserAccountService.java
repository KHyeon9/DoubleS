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

        // 토큰 생성
        String token = JwtTokenUtils.createJwtToken(userId, secretKey, expiredTimeMs);

        return token;
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

            studyGroupBoardCommentRepository.deleteAllByUserAccount(userAccount);
            studyGroupBoardRepository.deleteAllByUserAccount(userAccount);
            userStudyGroupRepository.deleteByUserAccount(userAccount);
        }
        
        // 질문 게시글 관련 삭제
        questionBoardCommentRepository.deleteAllByUserAccount(userAccount);
        questionBoardLikeRepository.deleteAllByUserAccount(userAccount);
        questionBoardRepository.deleteAllByUserAccount(userAccount);
        
        // todo 관련 삭제
        todoRepository.deleteAllByUserAccount(userAccount);
        
        // 관련된 채팅방 조회
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByUser(userAccount);

        if (!chatRooms.isEmpty()) {
            for (ChatRoom chatRoom : chatRooms) {
                ChatMessage message = null;

                if (chatRoom.getLeaveUserId() != null) {
                    // 채팅방에 남은 사람이 한명이면 채팅방 삭제
                    chatMessageRepository.deleteAllByChatRoom(chatRoom);
                    chatRoomRepository.delete(chatRoom);
                    continue;
                } else if (chatRoom.getUser1() == userAccount) {
                    // 유저1의 탈퇴시 상황
                    chatRoom.setLeaveUserId(chatRoom.getUser1().getUserId());
                    message = ChatMessage.of(chatRoom, userAccount, userAccount.getNickname() + "이 채팅방을 나갔습니다.");
                } else if (chatRoom.getUser2() == userAccount) {
                    // 유저2의 탈퇴시 상황
                    chatRoom.setLeaveUserId(chatRoom.getUser2().getUserId());
                    message = ChatMessage.of(chatRoom, userAccount, userAccount.getNickname() + "이 채팅방을 나갔습니다.");
                }

                if (message != null) {
                    chatMessageRepository.save(message);
                }
            }
        }

        userAccountRepository.deleteById(userId);
    }

    // 토큰 필터 설정에서 좀 더 편하게 사용할 수 있도록 작성
    public UserAccountDto loadUserByUserId(String userId) {
        return userAccountRepository.findById(userId).map(UserAccountDto::fromEntity).orElseThrow(
                () -> new DoubleSApplicationException(
                        ErrorCode.USER_NOT_FOUND, String.format("%s를 찾지 못했습니다", userId))
        );
    }
}
