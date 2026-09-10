package com.sist.web.domain.community.service;

import com.sist.web.domain.community.entity.NoticeBoard;
import com.sist.web.domain.community.mapper.NoticeBoardMapper;
import com.sist.web.domain.community.repository.NoticeBoardRepository;
import com.sist.web.domain.community.vo.NoticeBoardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
@Service
@RequiredArgsConstructor
public class NoticeBoardServiceImpl implements NoticeBoardService{
    private final NoticeBoardMapper nMapper;
    private final NoticeBoardRepository nRepo;
    private final int ROW=20;

    @Override
    public List<NoticeBoardVO> noticeBoardList(int page,String fd,Integer categoryNo) {
        int start=(page*ROW)-ROW;
        Map<String,Object> map=new HashMap<>();
        map.put("start",start);
        if(fd!=null && !fd.trim().isEmpty()) {
            map.put("fd", fd);
        }
        if(categoryNo!=null && categoryNo!=0) {
            map.put("categoryNo", categoryNo);
        }
        List<NoticeBoardVO> list=nMapper.noticeBoardList(map);
        return list;
    }

    @Override
    public Map<String, Object> noticeBoardPage(int page, String fd, Integer categoryNo) {
        Map<String,Object> map=new HashMap<>();
        if(fd!=null && !fd.trim().isEmpty()) {
            map.put("fd", fd);
        }
        if(categoryNo!=null && categoryNo!=0) {
            map.put("categoryNo", categoryNo);
        }
        int count=nMapper.noticeBoardCount(map);
        int totalpage=(int)Math.ceil(count/(double)ROW);
        final int BLOCK=10;
        int startPage=((page-1)/BLOCK*BLOCK)+1;
        int endPage=((page-1)/BLOCK*BLOCK)+BLOCK;
        if(endPage>totalpage) endPage=totalpage;
        map=new HashMap<>();
        map.put("curpage",page);
        map.put("totalpage",totalpage);
        map.put("startPage",startPage);
        map.put("endPage",endPage);
        map.put("count",count);
        return map;
    }

    @Override
    @Transactional
    public NoticeBoard noticeBoardDetail(int no) {
        NoticeBoard vo=nRepo.findByNo(no);
        vo.hitIncrement();
        return vo;
    }

    @Override
    public Integer noticeInsert(Integer memberId, NoticeBoard vo) {
        NoticeBoard notice=new NoticeBoard(memberId,vo.getCategoryNo(),vo.getSubject(),vo.getContent());
        NoticeBoard save=nRepo.save(notice);
        return save.getNo();
    }

    @Override
    @Transactional
    public void noticeUpdate(Integer no, Integer categoryNo,String subject,String content) {
        NoticeBoard notice=nRepo.findByNo(no);
        notice.update(categoryNo,subject,content);
    }

    @Override
    public NoticeBoard noticeUpdateData(Integer no) {
        return nRepo.findByNo(no);
    }

    @Override
    public void noticeDelete(NoticeBoard vo) {
        nRepo.delete(vo);
    }


}
