package com.sist.web.domain.community.service;

import com.sist.web.domain.community.entity.Notice_Board;
import com.sist.web.domain.community.repository.NoticeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
@RequiredArgsConstructor
public class NoticeBoardServiceImpl implements NoticeBoardService{
    private final NoticeBoardRepository nRepo;
    private final int ROW=20;

    @Override
    public List<Notice_Board> noticeBoardList(int page) {
        Pageable pg= PageRequest.of(page-1,ROW, Sort.by(Sort.Direction.DESC,"no"));
        Page<Notice_Board> p=nRepo.findAll(pg);
        List<Notice_Board> list=new ArrayList<>();
        if(p!=null && p.hasContent()){
            list=p.getContent();
        }
        return list;
    }

    @Override
    public Map<String, Object> noticeBoardPage(int page) {
        int count=(int)nRepo.count();
        int totalpage=(int)Math.ceil(count/(double)ROW);
        final int BLOCK=10;
        int startPage=((page-1)/BLOCK*BLOCK)+1;
        int endPage=((page-1)/BLOCK*BLOCK)+BLOCK;
        if(endPage>totalpage) endPage=totalpage;
        Map<String,Object> map=new HashMap<>();
        map.put("curpage",page);
        map.put("totalpage",totalpage);
        map.put("startPage",startPage);
        map.put("endPage",endPage);
        map.put("count",count);
        return map;
    }

    @Override
    public Notice_Board noticeBoardDetail(int no) {
        return nRepo.findByNo(no);
    }
}
