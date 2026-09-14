package com.sist.web.domain.exam.service;

import com.sist.web.domain.course.mapper.CourseMapper;
import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.exam.mapper.ExamMapper;
import com.sist.web.domain.exam.pgmapper.ExamPGMapper;
import com.sist.web.domain.exam.vo.RecommendCourseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendCoursesServiceImpl implements RecommendCoursesService {
    private final ExamMapper eMapper;
    private final ExamPGMapper pMapper;
    private final CourseMapper cMapper;

    @Override
    public List<RecommendCourseVO> getRecommentCourses(List<Integer> wrongQuestionNos) {
        if (wrongQuestionNos == null || wrongQuestionNos.isEmpty()) {
            return Collections.emptyList();
        }
        List<RecommendCourseVO> list = pMapper.findRecommendedCourses(wrongQuestionNos);

        if (list.isEmpty()) {
            return list;
        }
        List<Integer> courseNos=list.stream()
                .map(RecommendCourseVO::getCourse_no)
                .toList();
        List<CourseVO> courseList=cMapper.selectCoursesByNos(courseNos);
        Map<Integer, CourseVO> courseMap=courseList.stream()
                .collect(Collectors.toMap(
                        CourseVO::getNo,
                        course->course
                ));
        for (RecommendCourseVO recommend : list) {
            CourseVO course=courseMap.get(recommend.getCourse_no());

            if(course!=null){
                recommend.setTitle(course.getTitle());
                recommend.setThumbnail(course.getThumbnail());
            }
        }
        return list;
    }
}
