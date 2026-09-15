package com.sist.web.domain.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.member.vo.MemberVO;

import java.util.*;

@Mapper
@Repository
public interface AdminMapper 
{
	// 회원 리스트 조회
	/*
	 * 	<select id="adminMemberListData" resultType="hashmap" parameterType="int">
			SELECT m.member_id,m.username,m.name,TO_CHAR(m.regdate,'yyyy-mm-dd') as dbday,a.authority,m.enabled
			FROM member m
			JOIN authority a
			ON m.member_id=a.member_id
			ORDER BY m.member_id ASC
			OFFSET #{start} ROWS FETCH NEXT 10 ROWS ONLY
		</select>
	 */
	public List<Map<String,Object>> adminMemberListData(int start);
	
	//총페이지
	@Select("SELECT COUNT(*) FROM member")
	public int getCountMember();
	
	// 이름으로 검색
	/*
	 * 	<select id="adminMemberFindByName" resultType="hashmap" parameterType="string">
			SELECT m.member_id,m.username,m.name,TO_CHAR(m.regdate,'yyyy-mm-dd') as dbday,a.authority,m.enabled
			FROM member m
			JOIN authority a
			ON m.member_id=a.member_id
			WHERE m.name LIKE '%'||#{name}||'%'
			ORDER BY m.member_id ASC
		</select>
	 */
	public List<Map<String,Object>> adminMemberFindByName(String name);
	
	// 조건 필터링된 데이터 조회
	/*
	 * <select id="adminMemberFilterListData" resultType="hashmap" parameterType="int">
			SELECT m.member_id,m.username,m.name,TO_CHAR(m.regdate,'yyyy-mm-dd') as dbday,a.authority,m.enabled
			FROM member m
			JOIN authority a
			ON m.member_id=a.member_id
			
			<where>
				<if test="authority!='all'">
					AND authority=#{authority}
				</if>
				<if test="enabled!=-1">
					AND enabled=#{enabled}
				</if>
			</where>
			
			ORDER BY m.member_id ASC
			OFFSET #{start} ROWS FETCH NEXT 10 ROWS ONLY
		</select>
	 */
	public List<Map<String,Object>> adminMemberFilterListData(Map<String, Object> map);
	
	//필터링 된 총 회원 수
	/*
	 * 	<select id="getCountFilterMember" resultType="int" parameterType="hashmap">
			SELECT COUNT(*)
			FROM member m
			JOIN authority a
			ON m.member_id=a.member_id
			<where>
				<if test="authority!='all'">
					AND authority=#{authority}
				</if>
				<if test="enabled!=-1">
					AND enabled=#{enabled}
				</if>
			</where>
		</select>
	 */
	public int getCountFilterMember(Map<String, Object> map);
	
	// 회원 상세 정보 조회
	/*
	 * 	<select id="adminMemberDetailData" resultType="hashmap" parameterType="int">
			SELECT m.member_id,m.username,m.name,m.sex,TO_CHAR(m.birthdate,'yyyy.mm.dd') as birthday,m.phone,m.email,m.post,m.addr1,m.addr2,TO_CHAR(m.regdate,'yyyy.mm.dd') as dbday,TO_CHAR(m.profile_desc) as profiledesc,a.authority,m.enabled
			FROM member m
			JOIN authority a
			ON m.member_id=a.member_id
			WHERE m.member_id=#{member_id}
		</select>
	 */
	public Map<String, Object> adminMemberDetailData(int member_id);
	
	// 회원 enabled 상태 변경
	@Update("UPDATE member "
			+ "SET enabled=#{enabled} "
			+ "WHERE member_id=#{member_id} ")
	public void adminUpdateMemberEnabled(MemberVO vo);
	
	// 전체 강의 목록 조희
	/*
	 * 	<select id="adminCourseListData" resultType="hashmap" parameterType="int">
			SELECT c.no,c.title,m.name,TO_CHAR(c.pay_price, 'FM999,999,999') as payprice,c.thumbnail,c.student_count,c.star
			FROM course c
			JOIN member m
			ON c.instructor_no=m.member_id
			ORDER BY c.no desc
			OFFSET #{start} ROWS FETCH NEXT 10 ROWS ONLY
		</select>
	 */
	public List<Map<String, Object>> adminCourseListData(int start);
	
	// 강의 관리 총 갯수 얻기
	@Select("SELECT count(*) FROM course")
	public int adminGetCountCourse();
	
	// 강의명으로 검색
	/*
	 * 	<select id="adminFindCourseListData" resultType="hashmap" parameterType="hashmap">
			SELECT c.no,c.title,m.name,TO_CHAR(c.pay_price, 'FM999,999,999') as payprice,c.thumbnail,c.student_count,c.star
			FROM course c
			JOIN member m
			ON c.instructor_no=m.member_id
			WHERE c.title LIKE '%'||#{title}||'%'
			ORDER BY c.no desc
			OFFSET #{start} ROWS FETCH NEXT 10 ROWS ONLY
		</select>
	 */
	public List<Map<String, Object>> adminFindCourseListData(Map<String, Object> map);
	
	// 강의명으로 검색 시 해당 결과의 총 갯수
	@Select("SELECT count(*) FROM course WHERE title LIKE '%'||#{title}||'%'")
	public int adminGetCountFindCourse(String title);
	
	// 대시보드 출력용
	// 총 강사 수
	/*
	 * 	<select id="adminGetTotalInstCount" resultType="int">
			SELECT COUNT(*)
			FROM member m
			JOIN authority a
			ON m.member_id=a.member_id
			WHERE a.authority='ROLE_INSTRUCTOR'
		</select>
	 */
	public int adminGetTotalInstCount();
	
	// 수강생 top5 강의
	/*
	 * 	<select id="adminGetBest5Course" resultType="com.sist.web.domain.course.vo.CourseVO">
			SELECT rownum as no,title, student_count
			FROM (SELECT title, student_count
			        FROM course
			        ORDER BY student_count DESC)
			WHERE rownum &lt;= 5
		</select>
	 */
	public List<CourseVO> adminGetBest5Course();
	
	// 강의 결제 내역 조회
	/*
	 * 	<select id="adminCoursePaymentListData" resultType="hashmap" parameterType="hashmap">
			SELECT P.NO, P.MEMBER_ID, M.NAME, P.COURSE_NO, C.TITLE, TO_CHAR(P.PRICE,'999,999') as price, TO_CHAR(P.REGDATE,'YYYY.MM.DD') AS REGDATE, P.ORDER_STATUS
			FROM COURSE_PAYMENT P
			JOIN MEMBER M
			ON P.MEMBER_ID=M.MEMBER_ID
			JOIN COURSE C
			ON P.COURSE_NO=C.NO
			
			<where>
				<if test="order_status=='결제완료'">
					AND order_status='결제완료'
				</if>
				<if test="order_status=='환불대기'">
					AND order_status='환불대기'
				</if>
				<if test="order_status=='취소'">
					AND order_status='취소'
				</if>
				
			</where>
			
			ORDER BY P.REGDATE DESC, P.NO DESC
			OFFSET #{start} ROWS FETCH NEXT 10 ROWS ONLY
		</select>
	 */
	public List<Map<String, Object>> adminCoursePaymentListData(Map<String, Object> map);
	
	/*
	 * <!-- 강의결제내역 갯수 출력 -->
		<select id="adminCountCoursePayment" parameterType="string">
			SELECT count(*) 
			FROM COURSE_PAYMENT P 
			JOIN MEMBER M 
			ON P.MEMBER_ID=M.MEMBER_ID 
			JOIN COURSE C 
			ON P.COURSE_NO=C.NO
			<where>
				<if test="order_status=='결제완료'">
					AND order_status='결제완료'
				</if>
				<if test="order_status=='환불대기'">
					AND order_status='환불대기'
				</if>
				<if test="order_status=='취소'">
					AND order_status='취소'
				</if>
			</where>
		</select>
	 */
	public int adminCountCoursePayment(String order_status);
	
	/*
	 * <!-- 강의결제내역취소처리 -->
		<update id="adminPaymentCancleCourse" parameterType="int">
			UPDATE course_payment SET
			order_status='환불완료'
			WHERE no=#{no}
		</update>
	 */
	public void adminPaymentCancleCourse(int no);
	
	/*
	 * <!-- 도서 주문 목록 조회 -->
		<select id="adminOrderBookListData" parameterType="hashmap" resultType="hashmap">
			SELECT o.no,
			        o.member_id,
			        m.name,
			        TO_CHAR(o.total_price,'999,999') as total_price,
			        o.order_status,
			        TO_CHAR(o.regdate,'yyyy-mm-dd hh:mi:ss') as regdate
			FROM book_order o
			JOIN member m on o.member_id=m.member_id
			<where>
				<if test="order_status=='결제완료'">
					AND order_status='결제완료'
				</if>
				<if test="order_status=='환불대기'">
					AND order_status='환불대기'
				</if>
				<if test="order_status=='환불완료'">
					AND order_status='환불완료'
				</if>
			</where>
			ORDER BY o.regdate DESC , o.no DESC
			OFFSET #{start} ROWS FETCH NEXT 10 ROWS ONLY
		</select>
	*/
	public List<Map<String, Object>> adminOrderBookListData(Map<String, Object> map);
	
	/*
		<!-- 도서주문목록갯수 -->
		<select id="adminOrderBookCount" parameterType="hashmap" resultType="int">
			SELECT count(*)
			FROM book_order o
			JOIN member m on o.member_id=m.member_id
			<where>
				<if test="order_status=='결제완료'">
					AND order_status='결제완료'
				</if>
				<if test="order_status=='환불대기'">
					AND order_status='환불대기'
				</if>
				<if test="order_status=='환불완료'">
					AND order_status='환불완료'
				</if>
			</where>
		</select>
	 */
	public int adminOrderBookCount(String order_status);
	
	/*
	 * <!-- 도서결제내역취소처리 -->
		<update id="adminPaymentCancleBook" parameterType="int">
			UPDATE book_order SET
			order_status='환불완료'
			WHERE no=#{no}
		</update>
	 */
	public void adminPaymentCancleBook(int no);
	
	/*
	 * <!-- 도서주문내역상세 -->
		<select id="adminPaymentDetailListData" resultType="hashmap" parameterType="int">
		SELECT o.no,
		        b.title,
		        b.poster,
		        b.author,
		        d.quantity,
		        TO_CHAR(d.price, '999,999') AS price
		FROM book_order_detail d
		JOIN book_order o ON o.no=d.book_order_no
		JOIN book b ON b.no=d.book_no
		WHERE o.no=#{no}
	</select>
	 */
	public List<Map<String, Object>> adminPaymentDetailListData(int no);
	
	// 시험관리 - 결과 출력
	/*
	 * 	<select id="adminExamListData" resultType="hashmap" parameterType="int">
			SELECT m.name, NVL(se.title, '상시 모의고사') AS examtitle, e.totalscore, TO_CHAR(e.regdate) AS REGDATE,e.no
			FROM exam_enrollment e
			JOIN member m ON e.member_id = m.member_id
			LEFT JOIN scheduled_exam se ON e.exam_no = se.no
			WHERE e.totalscore IS NOT NULL
			ORDER BY e.regdate DESC
			OFFSET #{start} ROWS FETCH NEXT 10 ROWS ONLY
		</select>
	 */
	public List<Map<String, Object>> adminExamListData(int start);
	
	// 시험관리 - 갯수
	/*
	 * 	<select id="adminExamCount" resultType="int">
			SELECT count(*)
			FROM exam_enrollment e
			JOIN member m ON e.member_id = m.member_id
			LEFT JOIN scheduled_exam se ON e.exam_no = se.no
			WHERE e.totalscore IS NOT NULL
			ORDER BY e.regdate DESC
		</select>
	 */
	public int adminExamCount();
	
	// QnA 리스트 데이터
	/*
	 * 	<select id="adminQnaListData" resultType="hashmap" parameterType="hashmap">
			SELECT Q.NO,Q.MEMBER_ID,M.NAME,Q.SUBJECT,Q.CATEGORY_NO as categoryno,C.CATEGORY,
			    TO_CHAR(Q.REGDATE) AS REGDATE,Q.STATUS
			FROM QNABOARD Q
			JOIN MEMBER M
			ON Q.MEMBER_ID=M.MEMBER_ID
			JOIN QNA_CATEGORY C
			ON Q.CATEGORY_NO=C.NO
			<where>
				<if test="categoryno != null and categoryno!='-1'">
					AND category_no=#{categoryno}
				</if>
				<if test="status != null and status!='all'">
					AND status=#{status}
				</if>
			</where>
			ORDER BY Q.REGDATE DESC, Q.NO DESC
			OFFSET #{start} ROWS FETCH NEXT 10 ROWS ONLY
		</select>
	 */
	public List<Map<String, Object>> adminQnaListData(Map<String, Object> map);
	
	// QnA 리스트 갯수
	/*
	 * 	<select id="adminQnaCount" resultType="int" parameterType="hashmap">
			SELECT count(*)
			FROM QNABOARD Q
			JOIN MEMBER M
			ON Q.MEMBER_ID=M.MEMBER_ID
			JOIN QNA_CATEGORY C
			ON Q.CATEGORY_NO=C.NO
			<where>
				<if test="categoryno != null and categoryno!='-1'">
					AND category_no=#{categoryno}
				</if>
				<if test="status != null and status!='all'">
					AND status=#{status}
				</if>
			</where>
		</select>
	 */
	public int adminQnaCount(Map<String, Object> map);
	
	// QnA 상세 데이터
	/*
	 * 	<select id="adminQnaDetailData" resultType="hashmap" parameterType="int">
			SELECT Q.NO,Q.MEMBER_ID,M.NAME,Q.SUBJECT,Q.CATEGORY_NO,C.CATEGORY,
			    TO_CHAR(Q.REGDATE) AS REGDATE,Q.STATUS,TO_CHAR(Q.CONTENT) CONTENT,
			    TO_CHAR(R.CONTENT) ANSCONTENT, TO_CHAR(R.REGDATE) AS ANSDATE
			FROM QNABOARD Q
			JOIN MEMBER M
			ON Q.MEMBER_ID=M.MEMBER_ID
			JOIN QNA_CATEGORY C
			ON Q.CATEGORY_NO=C.NO
			LEFT JOIN QNAREPLY R
			ON Q.NO=R.QNA_NO
			WHERE Q.NO=#{no}
		</select>
	 */
	public Map<String, Object> adminQnaDetailData(int no);
	
	// QnA 답변 등록 - 트랜잭션
	/*
	 * 	<!-- QnA 답변 등록 Insert -->
		 <insert id="adminQnaAnswerInsert" parameterType="hashmap">
			INSERT INTO QNAREPLY
			(member_id,qna_no,content)
			VALUES
			(#{member_id},#{no},#{content})
		 </insert>
		 
		 <!-- 동시에 QnA 답변 상태를 Update -->
		 <update id="adminQnaStatusUpdate" parameterType="int">
		 	UPDATE QNABOARD
			SET STATUS='Y'
			WHERE NO=#{no}
		 </update>
	 */
	public void adminQnaAnswerInsert(Map<String, Object> map);
	public void adminQnaStatusUpdate(int no);
}
