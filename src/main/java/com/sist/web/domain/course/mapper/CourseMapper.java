package com.sist.web.domain.course.mapper;

import java.util.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.course.vo.TechStackVO;

@Mapper
@Repository
public interface CourseMapper {
	public List<CourseVO> courseMainList();
	public List<CourseVO> courseListData(Map<String,Object> map);
	public int courseCount(Map<String,Object> map);
	public List<TechStackVO> courseCategoryList();
}
