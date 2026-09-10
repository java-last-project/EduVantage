package com.sist.web.domain.instructor.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.member.vo.MemberVO;
import java.util.*;

@Mapper
@Repository
public interface InstructorMapper {
	
	// 프로필 내용 조회
	@Select("SELECT member_id,username,name,TO_CHAR(regdate,'yyyy.mm.dd') as dbday,TO_CHAR(profile_desc) as profile_desc "
			+ "FROM member "
			+ "WHERE member_id=#{member_id}")
	public MemberVO InstProfileData(int member_id);
	
	// 강의 정보 데이터
	/*
	 * 	<select id="InstCourseDataList" resultType="hashmap" parameterType="int">
			SELECT no,title,star,student_count,pay_price,regular_price,content,images,thumbnail,name,email,TO_CHAR(profile_desc)
			FROM course c
			JOIN member m
			ON member_id=instructor_no
			WHERE member_id=#{member_id}
			ORDER BY star DESC
		</select>
	 */
	public List<Map<String, Object>> InstCourseDataList(int member_id);
	
	// 강의 상세 데이터 조회 - 수강생 목록
	/*
	 * 	<select id="InstCourseEnrollStudList" resultType="hashmap" parameterType="int">
			SELECT c.member_id,c.course_no,c.is_completed,c.progress,c.regdate,c.last_accessed,m.name
			FROM course_enrollment c
			JOIN member m
			ON c.member_id=m.member_id
			WHERE c.course_no=#{course_no}
		</select>
	 */
	public List<Map<String, Object>> InstCourseEnrollStudList(int course_no);
	
	// 강의 상세 데이터 조회 - 강의 정보
	@Select("SELECT title, thumbnail, student_count, star, no, content, pay_price, regular_price "
			+ "FROM course "
			+ "WHERE no=#{no}")
	public CourseVO InstCourseDetailData(int no);
	
	// 강의 상세 데이터 수정
	/*
	 * 	<update id="instUpdateCourseData" parameterType="com.sist.web.domain.course.vo.CourseVO">
			UPDATE course 
			SET title=#{title}, pay_price=#{pay_price}, regular_price=#{regular_price}, content=#{content}, thumbnail=#{thumbnail}
			WHERE no=#{no}
		</update>
	 */
	public void instUpdateCourseData(CourseVO vo);
	
	// 새 강의 등록
	// <insert id="instInsertNewCourse" parameterType="com.sist.web.domain.course.vo.CourseVO">
	public void instInsertNewCourse(CourseVO vo);
	
}
