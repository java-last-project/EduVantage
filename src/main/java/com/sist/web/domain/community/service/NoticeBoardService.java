package com.sist.web.domain.community.service;
import com.sist.web.domain.community.entity.Notice_Board;

import java.util.*;
public interface NoticeBoardService {
    public List<Notice_Board> noticeBoardList(int page);
    public Map<String,Object> noticeBoardPage(int page);
    public Notice_Board noticeBoardDetail(int no);
}
