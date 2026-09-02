package com.sist.web.domain.course.service;

import java.util.List;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.course.vo.TechStackVO;

public interface CourseService {
	public List<CourseVO> courseMainList();
	public List<CourseVO> courseListData(int page,String column,String category,String fd);
	public int[] coursePageData(int page,String category,String fd);
	public List<TechStackVO> courseCategoryList();
}
