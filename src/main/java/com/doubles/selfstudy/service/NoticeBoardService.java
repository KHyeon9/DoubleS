package com.doubles.selfstudy.service;

import com.doubles.selfstudy.dto.notice.NoticeBoardDto;
import com.doubles.selfstudy.entity.NoticeBoard;
import com.doubles.selfstudy.entity.UserAccount;
import com.doubles.selfstudy.exception.DoubleSApplicationException;
import com.doubles.selfstudy.exception.ErrorCode;
import com.doubles.selfstudy.repository.NoticeBoardRepository;
import com.doubles.selfstudy.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NoticeBoardService {

    private final NoticeBoardRepository noticeBoardRepository;
    private final ServiceUtils serviceUtils;

    // 공지사항 목록 조회
    public Page<NoticeBoardDto> noticeBoardList(Pageable pageable) {
        return noticeBoardRepository.findAll(pageable).map(NoticeBoardDto::fromEntity);
    }

    // 공지사항 상세 조회
    public NoticeBoardDto noticeBoardDetail(Long noticeBoardId) {
        NoticeBoard noticeBoard = serviceUtils.getNoticeBoardOrException(noticeBoardId);

        return NoticeBoardDto.fromEntity(noticeBoard);
    }

    // 공지사항 생성
    @Transactional
    public void createNoticeBoard(String userId, String title, String content) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getAdminUserAccountOrException(userId);

        noticeBoardRepository.save(NoticeBoard.of(userAccount, title, content));
    }

    // 공지사항 수정
    @Transactional
    public NoticeBoardDto modifyNoticeBoard(String userId, Long noticeBoardId, String title, String content) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getAdminUserAccountOrException(userId);

        // 공지사항 글 가져오기
        NoticeBoard noticeBoard = serviceUtils.getNoticeBoardOrException(noticeBoardId);
        
        // 권한 검사
        if (noticeBoard.getUserAccount() != userAccount) {
            throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION, String.format(
                        "%s는 권한이 공지사항 번호: '%s' 에 대해서 권한이 없습니다.",
                        userId,
                        noticeBoardId
                    )
            );
        }

        // 변경 내용 수정
        noticeBoard.setTitle(title);
        noticeBoard.setContent(content);

        return NoticeBoardDto.fromEntity(noticeBoardRepository.saveAndFlush(noticeBoard));
    }

    // 공지사항 삭제
    public void deleteNoticeBoard(String userId, Long noticeBoardId) {
        // 유저 확인
        UserAccount userAccount = serviceUtils.getAdminUserAccountOrException(userId);

        // 공지사항 글 가져오기
        NoticeBoard noticeBoard = serviceUtils.getNoticeBoardOrException(noticeBoardId);

        // 권한 검사
        if (noticeBoard.getUserAccount() != userAccount) {
            throw new DoubleSApplicationException(
                    ErrorCode.INVALID_PERMISSION, String.format(
                        "%s는 권한이 공지사항 번호: '%s' 에 대해서 권한이 없습니다.",
                        userId,
                        noticeBoardId
                    )
            );
        }

        noticeBoardRepository.delete(noticeBoard);
    }
}
