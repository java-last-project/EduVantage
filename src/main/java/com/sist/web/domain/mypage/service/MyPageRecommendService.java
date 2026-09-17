package com.sist.web.domain.mypage.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sist.web.domain.exam.vo.RecommendCourseVO;

public interface MyPageRecommendService {
	//public List<String> findCourseEmbeddings(@Param("courseNos") List<Integer> courseNos);
	public List<RecommendCourseVO> courseRecomandListData(List<Integer> courseNos);
}
