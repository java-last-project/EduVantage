package com.sist.web.domain.enrollment.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import com.sist.web.domain.enrollment.service.YoutubeService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;

import com.sist.web.domain.course.service.CourseService;
import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.course.vo.TechStackVO;
import com.sist.web.domain.enrollment.service.EnrollmentService;

class EnrollmentControllerTest {

	@Test
	void videoPageLoadsCourseWithTechStacksAndCategories() {
		EnrollmentService enrollmentService=mock(EnrollmentService.class);
		CourseService courseService=mock(CourseService.class);
		YoutubeService youtubeService=mock(YoutubeService.class);
		EnrollmentController controller=new EnrollmentController(enrollmentService,courseService,youtubeService);
		ConcurrentModel model=new ConcurrentModel();

		TechStackVO tech=new TechStackVO();
		tech.setTech("Spring Boot");
		tech.setCategory("백엔드");
		CourseVO course=new CourseVO();
		course.setNo(35);
		course.setTechList(List.of(tech));
		course.setCategoryList(List.of("백엔드"));
		when(courseService.courseDetail(35)).thenReturn(course);

		String view=controller.enrollment_video(35,model);

		assertEquals("enrollment/layout/main",view);
		assertSame(course,model.getAttribute("course"));
		assertEquals(List.of("백엔드"),course.getCategoryList());
		assertEquals("Spring Boot",course.getTechList().get(0).getTech());
		assertEquals("video",model.getAttribute("menu"));
		assertEquals("enrollment/video",model.getAttribute("enrollment_html"));
		verify(courseService).courseDetail(35);
	}
}
