package com.sist.web.domain.mypage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.sist.web.domain.mypage.vo.*;
import java.util.*;

@Mapper
@Repository
public interface MyPageMapper {
	public List<MyCourseEnrollmentVO> mypageCourseListData(int member_id);
	public List<MyCourseEnrollmentVO> lastAccessedCourse(int member_id);
	
	@Select("SELECT COUNT(*) FROM course_enrollment "
			+ "WHERE member_id=#{member_id} AND is_completed='N'")
	public int enrolledCount(int member_id);
	
	@Select("SELECT member_id,username,name,"
			+ "TO_CHAR(regdate,'yyyy-mm-dd') as dbRday,"
			+ "TO_CHAR(birthdate,'yyyy-mm-dd') as dbBday,"
			+ "phone,post,addr1,addr2,email,profile_desc,profile_image "
			+ "FROM member "
			+ "WHERE member_id=#{member_id}")
	public MyMemberVO memberProfileData(int member_id);
}
