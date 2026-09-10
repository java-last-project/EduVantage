package com.sist.web.domain.community.mapper;

import com.sist.web.domain.community.vo.NoticeBoardVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface NoticeBoardMapper {
    public List<NoticeBoardVO> noticeBoardList(Map<String,Object> map);
    public int noticeBoardCount(Map<String,Object> map);
}
