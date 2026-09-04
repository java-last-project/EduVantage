package com.sist.web.domain.mypage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.sist.web.domain.enrollment.vo.*;
import com.sist.web.domain.member.vo.MemberVO;
import com.sist.web.domain.mypage.vo.*;
import java.util.*;

@Mapper
@Repository
public interface MyPageMapper {
	public List<CourseEnrollmentVO> mypageCourseListData(int member_id);
	public List<CourseEnrollmentVO> lastAccessedCourse(int member_id);
	
	@Select("SELECT COUNT(*) FROM course_enrollment "
			+ "WHERE member_id=#{member_id} AND is_completed='N'")
	public int enrolledCount(int member_id);
	
	@Select("SELECT member_id,username,name,sex,"
			+ "TO_CHAR(regdate,'yyyy-mm-dd') as dbRday,"
			+ "TO_CHAR(birthdate,'yyyy-mm-dd') as dbBday,"
			+ "phone,post,addr1,addr2,email,profile_desc,profile_image "
			+ "FROM member "
			+ "WHERE member_id=#{member_id}")
	public MyMemberVO memberProfileData(int member_id);
	public void memberUpdateData(MemberVO vo);
	public List<CoursePaymentVO> coursePaymentListData(
			@Param("start") int start,
			@Param("member_id") int member_id);
	
	@Select("SELECT COUNT(*) FROM course_payment "
			+ "WHERE member_id=#{member_id}")
	public int coursePaymentRowCount(int member_id);
	
	public List<CourseCartVO> courseCartListData(
			@Param("start") int start,
			@Param("member_id") int member_id);
	@Select("SELECT COUNT(*) FROM course_cart "
			+ "WHERE member_id=#{member_id}")
	public int courseCartRowCount(int member_id);
}
