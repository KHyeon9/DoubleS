package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.NoticeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> {
    
    // 공지사항 최신 10개 조회
    List<NoticeBoard> findTop10ByOrderByCreatedAtDesc();
}
