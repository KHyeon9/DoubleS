package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.notification.NotificationBoardDto;
import com.doubles.selfstudy.dto.user.RoleType;
import com.doubles.selfstudy.entity.NotificationBoard;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.NotificationBoardRepository;
import com.doubles.selfstudy.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class NotificationBoardService {

    private final NotificationBoardRepository notificationBoardRepository;
    private final UserAccountRepository userAccountRepository;

    // 공지사항 목록 조회
    public Page<NotificationBoardDto> nofificationBoardList(Pageable pageable) {
        return notificationBoardRepository.findAll(pageable).map(NotificationBoardDto::fromEntity);
    }

    // 공지사항 생성
    @Transactional
    public void createNofificationBoard(String userId, String title, String content) {
        // 유저 확인
        UserAccount userAccount = getUserAccountOrException(userId);

        notificationBoardRepository.save(NotificationBoard.of(userAccount, title, content));
    }

    // 공지사항 수정
    @Transactional
    public NotificationBoardDto updateNofificationBoard(String userId, Long notificationBoardId, String title, String content) {
        // 유저 확인
        UserAccount userAccount = getUserAccountOrException(userId);

        // 공지사항 글 가져오기
        NotificationBoard notificationBoard = getNotificationBoardOrException(notificationBoardId);
        
        // 권한 검사
        if (notificationBoard.getUserAccount() != userAccount) {
            throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION, String.format(
                    "%s는 권한이 공지사항 번호: '%s' 에 대해서 권한이 없습니다.",
                    userId,
                    notificationBoardId
                )
            );
        }

        // 변경 내용 수정
        notificationBoard.setTitle(title);
        notificationBoard.setContent(content);

        return NotificationBoardDto.fromEntity(notificationBoardRepository.saveAndFlush(notificationBoard));
    }

    // 공지사항 삭제
    public void deleteNofificationBoard(String userId, Long notificationBoardId) {
        // 유저 확인
        UserAccount userAccount = getUserAccountOrException(userId);

        // 공지사항 글 가져오기
        NotificationBoard notificationBoard = getNotificationBoardOrException(notificationBoardId);

        // 권한 검사
        if (notificationBoard.getUserAccount() != userAccount) {
            throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION, String.format(
                    "%s는 권한이 공지사항 번호: '%s' 에 대해서 권한이 없습니다.",
                    userId,
                    notificationBoardId
                )
            );
        }

        notificationBoardRepository.delete(notificationBoard);
    }

    private UserAccount getUserAccountOrException(String userId) {
        UserAccount userAccount = userAccountRepository.findById(userId).orElseThrow(() ->
                    new DoubleSApplicationException(ErrorCode.USER_NOT_FOUND, String.format("유저 %s를 찾지 못했습니다.", userId)
                )
        );

        // 유저 권한 확인
        if (!Objects.equals(userAccount.getRoleType().getRoleName(), RoleType.ADMIN.getRoleName())) {
            throw new DoubleSApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s는 관리자가 아닙니다.", userId));
        }

        return userAccount;
    }

    private NotificationBoard getNotificationBoardOrException(Long notificationBoardId) {
        // 공지사항 글을 가져오면서 못 찾는 경우 검사
        return notificationBoardRepository.findById(notificationBoardId).orElseThrow(() ->
                    new DoubleSApplicationException(ErrorCode.POST_NOT_FOUND, String.format("공지사항 %d번을 찾지 못했습니다.", notificationBoardId)
                )
        );
    }
}
