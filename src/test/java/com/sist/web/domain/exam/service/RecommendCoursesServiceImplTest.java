package com.sist.web.domain.exam.service;

import com.sist.web.domain.course.mapper.CourseMapper;
import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.exam.mapper.ExamMapper;
import com.sist.web.domain.exam.pgmapper.ExamPGMapper;
import com.sist.web.domain.exam.vo.ExamEnrollmentVO;
import com.sist.web.domain.exam.vo.ExamQuestionVO;
import com.sist.web.domain.exam.vo.RecommendCourseVO;
import com.sist.web.domain.vector.EmbeddingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendCoursesServiceImplTest {
    @Mock
    private ExamMapper examMapper;
    @Mock
    private ExamPGMapper examPGMapper;
    @Mock
    private CourseMapper courseMapper;
    @Mock
    private EmbeddingService embeddingService;

    private RecommendCoursesServiceImpl service;

    @BeforeEach
    void setUp() {
        service=new RecommendCoursesServiceImpl(examMapper,examPGMapper,courseMapper,embeddingService);
    }

    @Test
    void aiRecommendationEmbedsAggregatedMetadataOnlyOnce() {
        ExamEnrollmentVO enrollment=new ExamEnrollmentVO();
        enrollment.setEndtime(LocalDateTime.now());
        when(examMapper.getEnrollmentForMember(any())).thenReturn(enrollment);

        ExamQuestionVO first=new ExamQuestionVO();
		first.setTitle("ArrayList와 LinkedList의 차이는?");
		first.setDescription("자료구조별 접근과 삽입 성능을 비교한다.");
        first.setAi_topic("Java 예외처리");
		first.setAi_keywords("try-catch,Exception,finally");
        ExamQuestionVO second=new ExamQuestionVO();
		second.setTitle("List 구현체 선택 문제");
		second.setDescription("상황에 적합한 컬렉션 구현체를 선택한다.");
        second.setAi_topic("Java 컬렉션");
		second.setAi_keywords("List,ArrayList,Collection");
        when(examMapper.selectAiWrongQuestionMetadata(any())).thenReturn(List.of(first,second));
        when(embeddingService.embed(anyString())).thenReturn(new float[]{0.1f,0.2f});

        RecommendCourseVO recommendation=new RecommendCourseVO();
        recommendation.setCourse_no(7);
        recommendation.setSimilarity(0.91);
        when(examPGMapper.findAiRecommendedCourses(anyString())).thenReturn(List.of(recommendation));

        CourseVO course=new CourseVO();
        course.setNo(7);
        course.setTitle("Java 핵심 강의");
        when(courseMapper.selectCoursesByNos(List.of(7))).thenReturn(List.of(course));

        List<RecommendCourseVO> result=service.getAiRecommendedCourses(10,20);

        ArgumentCaptor<String> searchText=ArgumentCaptor.forClass(String.class);
        verify(embeddingService,times(1)).embed(searchText.capture());
        verify(examPGMapper,times(1)).findAiRecommendedCourses(anyString());
        verify(courseMapper,times(1)).selectCoursesByNos(List.of(7));
		assertThat(searchText.getValue()).contains(
				"ArrayList와 LinkedList의 차이는?",
				"자료구조별 접근과 삽입 성능을 비교한다.",
				"Java 예외처리","try-catch","Java 컬렉션","ArrayList"
		);
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTitle()).isEqualTo("Java 핵심 강의");
    }

    @Test
    void noWrongMetadataSkipsEmbeddingAndVectorSearch() {
        ExamEnrollmentVO enrollment=new ExamEnrollmentVO();
        enrollment.setEndtime(LocalDateTime.now());
        when(examMapper.getEnrollmentForMember(any())).thenReturn(enrollment);
        when(examMapper.selectAiWrongQuestionMetadata(any())).thenReturn(List.of());

        assertThat(service.getAiRecommendedCourses(10,20)).isEmpty();

        verifyNoInteractions(embeddingService,examPGMapper,courseMapper);
    }

	@Test
	void unknownOrOtherMembersEnrollmentIsRejectedBeforeRecommendation() {
		when(examMapper.getEnrollmentForMember(any())).thenReturn(null);

		assertThatThrownBy(()->service.getAiRecommendedCourses(10,999))
				.isInstanceOf(org.springframework.web.server.ResponseStatusException.class)
				.hasMessageContaining("존재하지 않는 응시기록");

		verify(examMapper,never()).selectAiWrongQuestionMetadata(any());
		verifyNoInteractions(embeddingService,examPGMapper,courseMapper);
	}
}
