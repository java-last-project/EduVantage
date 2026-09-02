package com.sist.web.domain.course.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sist.web.domain.course.mapper.CourseMapper;
import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.course.vo.TechStackVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
	private final CourseMapper cMapper;
	private final int ROW=15;
	private final int BLOCK=10;
	@Override
	public List<CourseVO> courseMainList() {
		return cMapper.courseMainList();
	}

	@Override
	public List<CourseVO> courseListData(int page, String column,String category,String fd) {
		Map<String,Object> map=new HashMap<>();
		int start=(page*ROW)-ROW;
		map.put("start", start);
		map.put("column", column);
		if(category!=null && !"".equals(category)) {
	        List<String> catList = Arrays.asList(category.split(","));
	        map.put("categories", catList);
	    }
		if(fd!=null && !"".equals(fd)) {
			map.put("fd", fd);
		}
		List<CourseVO> list = cMapper.courseListData(map);
		for (CourseVO vo : list) {
	        if (vo.getTechList()!=null) {
	            List<String> uniqueCategories=vo.getTechList().stream()
	                .map(TechStackVO::getCategory)
	                .filter(cat->cat!=null && !cat.isEmpty() && !"Unclassified".equals(cat))
	                .distinct()
	                .collect(Collectors.toList());
	            vo.setCategoryList(uniqueCategories);
	        }
	    }
		return list;
	}

	@Override
	public int[] coursePageData(int page, String category,String fd) {
		Map<String,Object> map=new HashMap<>();
		if(category != null && !"".equals(category)) {
	        List<String> catList = Arrays.asList(category.split(","));
	        map.put("categories", catList);
	    }
		if(fd!=null && !"".equals(fd)) {
			map.put("fd", fd);
		}
		int start=(page*ROW)-ROW;
		map.put("start", start);
		int count=cMapper.courseCount(map);
		
		int totalpage=(int)Math.ceil(count/(double)ROW);
		int startPage=((page-1)/BLOCK*BLOCK)+1;
		int endPage=((page-1)/BLOCK*BLOCK)+BLOCK;
		if(endPage>totalpage) endPage=totalpage;
		
		int[] pages= {page,totalpage,startPage,endPage,count};
		return pages;
	}

	@Override
	public List<TechStackVO> courseCategoryList() {
		List<TechStackVO> list=cMapper.courseCategoryList();
		for (TechStackVO vo : list) {
	        if (vo.getCategory()!=null && "Unclassified".equals(vo.getCategory())) {
	            vo.setCategory("기타");
	        }
	    }
		return list;
	}

}
