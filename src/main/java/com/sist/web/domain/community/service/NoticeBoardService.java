package com.sist.web.domain.community.service;
import com.sist.web.domain.community.entity.NoticeBoard;
import com.sist.web.domain.community.vo.NoticeBoardVO;

import java.util.*;
public interface NoticeBoardService {
    public List<NoticeBoardVO> noticeBoardList(int page, String fd, Integer categoryNo);
    public Map<String,Object> noticeBoardPage(int page, String fd, Integer categoryNo);
    public NoticeBoard noticeBoardDetail(int no);
    public Integer noticeInsert(Integer memberId,NoticeBoard vo);
    public void noticeUpdate(Integer no, Integer categoryNo,String subject,String content);
    public NoticeBoard noticeUpdateData(Integer no);
    public void noticeDelete(NoticeBoard vo);
}
