package com.sist.web.domain.community.repository;

import com.sist.web.domain.community.entity.Notice_Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface NoticeBoardRepository extends JpaRepository<Notice_Board, Integer> {
    public Notice_Board findByNo(Integer no);
}
