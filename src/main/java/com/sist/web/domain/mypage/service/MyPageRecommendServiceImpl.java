package com.sist.web.domain.mypage.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pgvector.PGvector;
import com.sist.web.domain.course.mapper.CourseMapper;
import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.exam.vo.RecommendCourseVO;
import com.sist.web.domain.mypage.pgmapper.CourseRecommandPGMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageRecommendServiceImpl implements MyPageRecommendService {
	private final CourseRecommandPGMapper pgMapper;
	private final CourseMapper cMapper;
	
	@Override
	public List<RecommendCourseVO> courseRecomandListData(List<Integer> courseNos) {
		// TODO Auto-generated method stub
		List<RecommendCourseVO> list=new ArrayList<RecommendCourseVO>();
		if(courseNos==null || courseNos.isEmpty()) {
			return Collections.emptyList();
		}
		List<String> embeddingTexts=pgMapper.findCourseEmbeddings(courseNos);
		if(embeddingTexts.isEmpty()) {
			return Collections.emptyList();
		}
		try {
			float[] average=averageVectors(embeddingTexts);
			String queryVector=new PGvector(average).toString();
			list=pgMapper.courseRecomandListData(queryVector,courseNos);
			if(list.isEmpty()) return list;
			attachCourseInfo(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	private float[] averageVectors(List<String> vectorStrings) {
		float[] sum=null;
		try {
			for(String text: vectorStrings) {
				float[] vec = new PGvector(text).toArray();
				if(sum==null) {
					sum=new float[vec.length];
				}
				for(int i=0; i<vec.length; i++) {
					sum[i]+=vec[i];
				}
			}
			for(int i=0; i<sum.length; i++) {
				sum[i]/=vectorStrings.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sum;
	}
	
    private void attachCourseInfo(List<RecommendCourseVO> list) {
        List<Integer> courseNos = list.stream()
                .map(RecommendCourseVO::getCourse_no)
                .distinct()
                .toList();
        List<CourseVO> courseList = cMapper.selectCoursesByNos(courseNos);
        Map<Integer, CourseVO> courseMap = courseList.stream()
                .collect(Collectors.toMap(CourseVO::getNo, course -> course));
        for (RecommendCourseVO recommend : list) {
            CourseVO course = courseMap.get(recommend.getCourse_no());
            if (course != null) {
                recommend.setTitle(course.getTitle());
                recommend.setThumbnail(course.getThumbnail());
            }
        }
    }

}
