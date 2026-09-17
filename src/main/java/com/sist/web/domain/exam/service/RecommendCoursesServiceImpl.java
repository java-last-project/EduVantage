package com.sist.web.domain.exam.service;

import com.pgvector.PGvector;
import com.sist.web.domain.course.mapper.CourseMapper;
import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.exam.mapper.ExamMapper;
import com.sist.web.domain.exam.pgmapper.ExamPGMapper;
import com.sist.web.domain.exam.vo.ExamEnrollmentVO;
import com.sist.web.domain.exam.vo.ExamQuestionVO;
import com.sist.web.domain.exam.vo.RecommendCourseVO;
import com.sist.web.domain.vector.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendCoursesServiceImpl implements RecommendCoursesService {
    private final ExamMapper eMapper;
    private final ExamPGMapper pMapper;
    private final CourseMapper cMapper;
	private final EmbeddingService embeddingService;

    @Override
    public List<RecommendCourseVO> getRecommentCourses(List<Integer> wrongQuestionNos) {
        if (wrongQuestionNos == null || wrongQuestionNos.isEmpty()) {
            return Collections.emptyList();
        }
        List<RecommendCourseVO> list = pMapper.findRecommendedCourses(wrongQuestionNos);

        if (list.isEmpty()) {
            return list;
        }
		attachCourseInfo(list);
		return list;
	}

	@Override
	public List<RecommendCourseVO> getAiRecommendedCourses(int memberId,int enrollmentNo) {
		Map<String,Object> params=new HashMap<>();
		params.put("memberId",memberId);
		params.put("enrollmentNo",enrollmentNo);

		ExamEnrollmentVO enrollment=eMapper.getEnrollmentForMember(params);
		if(enrollment==null){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 응시기록입니다.");
		}
		if(enrollment.getExam_no()!=null || enrollment.getTheme()!=null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"AI 시험 응시기록이 아닙니다.");
		}
		if(enrollment.getEndtime()==null){
			throw new ResponseStatusException(HttpStatus.CONFLICT,"아직 제출되지 않은 시험입니다.");
		}

		// 제출된 본인 응시 기록의 오답만 추천 검색에 사용
		List<ExamQuestionVO> wrongQuestions=eMapper.selectAiWrongQuestionMetadata(params);
		if(wrongQuestions.isEmpty()){
			return Collections.emptyList();
		}

		// 오답의 실제 문제 내용과 학습 개념으로 검색 문맥 구성
		LinkedHashSet<String> metadata=new LinkedHashSet<>();
		for(ExamQuestionVO question:wrongQuestions){
			addMetadata(metadata,question.getTitle());
			addMetadata(metadata,question.getDescription());
			addMetadata(metadata,question.getAi_topic());
			if(question.getAi_keywords()!=null && !question.getAi_keywords().isBlank()){
				for(String keyword:question.getAi_keywords().split(",")){
					addMetadata(metadata,keyword);
				}
			}
		}
		if(metadata.isEmpty()){
			return Collections.emptyList();
		}

		// 오답 문맥을 하나의 벡터로 바꿔 강의 벡터와 유사도 비교
		String searchText=String.join(" ",metadata);
		float[] embedding=embeddingService.embed(searchText);
		List<RecommendCourseVO> recommendations=pMapper.findAiRecommendedCourses(new PGvector(embedding).toString());
		if(recommendations.isEmpty()){
			return recommendations;
		}

		// 벡터 검색 결과의 강의 번호로 원본 정보를 붙여 응답 보강
		attachCourseInfo(recommendations);
		// 원본 강의가 더 이상 조회되지 않으면 화면에 노출하지 않음
		return recommendations.stream()
				.filter(course->course.getTitle()!=null)
				.toList();
	}

	private void attachCourseInfo(List<RecommendCourseVO> list) {
        List<Integer> courseNos=list.stream()
                .map(RecommendCourseVO::getCourse_no)
				.distinct()
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
    }

	private void addMetadata(Set<String> metadata,String value) {
		if(value!=null && !value.isBlank()){
			metadata.add(value.trim());
		}
	}
}
