package com.sist.web.domain.instructor.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.community.vo.QnaReplyVO;
import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.enrollment.vo.CourseQnaReplyVO;
import com.sist.web.domain.enrollment.vo.CourseQnaVO;
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
	
	// 강사페이지 수강생 QnA 목록 조회
	/*
	 * 	<select id="instQnaListData" parameterType="int" resultType="com.sist.web.domain.enrollment.vo.CourseQnaVO">
			SELECT Q.NO,Q.MEMBER_ID,Q.COURSE_NO,Q.SUBJECT,Q.STATUS,Q.HIT,Q.REGDATE
			FROM COURSE_QNA Q
			JOIN COURSE C
			ON Q.COURSE_NO=C.NO
			WHERE C.INSTRUCTOR_NO=#{member_id}
			ORDER BY Q.REGDATE DESC
		</select>
	 */
	public List<Map<String, Object>>instQnaListData(Map<String, Object> map);
	
	// 총 수강생 QnA 목록 수
	/*
	 * 	<select id="instCountQnaList" parameterType="int" resultType="int">
			SELECT COUNT(*)
			FROM COURSE_QNA Q
			JOIN COURSE C
			ON Q.COURSE_NO = C.NO
			WHERE C.INSTRUCTOR_NO=#{member_id}
		</select>
	 */
	public int instCountQnaList(Map<String, Object> map);
	
	// 수강생 QnA 상세 페이지 조회
	/*
	 * 	<select id="instQnaDetailData" parameterType="int" resultType="hashmap">
			SELECT Q.NO,Q.MEMBER_ID,M.NAME,Q.SUBJECT,Q.CONTENT,
			    Q.STATUS,Q.HIT,Q.REGDATE
			FROM COURSE_QNA Q
			JOIN MEMBER M
			ON Q.MEMBER_ID=M.MEMBER_ID
			WHERE Q.NO=#{no}
		</select>
	 */
	public Map<String, Object> instQnaDetailData(int no);
	
	// 수강생 QnA 필터링용 담당 강사의 강의 목록 출력
	/*
	 * 	<select id="instQnaFilterCourse" parameterType="int" resultType="string">
			SELECT title
			FROM course c
			JOIN member m
			ON member_id=instructor_no
			WHERE member_id=#{member_id}
			ORDER BY title ASC
		</select>
	 */
	public List<String> instQnaFilterCourse(int member_id);
	
	// 수강생 QnA 답변 출력
	/*
	 * 	<select id="instQnaAnswerData" parameterType="int" resultType="com.sist.web.domain.community.vo.QnaReplyVO">
			SELECT A.ANSWER,A.REGDATE
			FROM COURSE_QNA_REPLY A
			JOIN COURSE_QNA Q
			ON A.COURSE_QNA_NO=Q.NO
			WHERE Q.NO=#{no}
		</select>
	 */
	public CourseQnaReplyVO instQnaAnswerData(int no);
	
	// 수강생 QnA 답변 등록
	/*
		<insert id="instQnaAnswerInsert" parameterType="com.sist.web.domain.enrollment.vo.CourseQnaReplyVO">
			INSERT INTO COURSE_QNA_REPLY
			(no,course_qna_no,member_id,answer)
			VALUES (CQR_NO_SEQ.nextval,#{course_qna_no},#{member_id},#{answer});
		</insert>
	*/
	public void instQnaAnswerInsert(CourseQnaReplyVO vo);
	
	// 수강생 QnA 답변 달린 후 status 갱신
	/*
		<update id="instQnaUpdateStatus" parameterType="int">
			UPDATE COURSE_QNA
			SET STATUS='Y'
			WHERE NO=#{no};
		</update>
	 */
	public void instQnaUpdateStatus(int no);
	
}
