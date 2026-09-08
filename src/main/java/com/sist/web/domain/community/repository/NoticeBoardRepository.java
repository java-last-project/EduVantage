package com.sist.web.domain.community.repository;

import com.sist.web.domain.community.entity.NoticeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Integer> {
    public NoticeBoard findByNo(Integer no);
}
