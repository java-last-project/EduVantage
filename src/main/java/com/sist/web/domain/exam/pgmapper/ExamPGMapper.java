package com.sist.web.domain.exam.pgmapper;

import com.sist.web.domain.exam.vo.RecommendCourseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExamPGMapper {
    public List<RecommendCourseVO> findRecommendedCourses(@Param("questionNos") List<Integer> questionNos);
	public List<RecommendCourseVO> findAiRecommendedCourses(@Param("queryVector") String queryVector);
}
