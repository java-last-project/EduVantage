package com.sist.web.domain.enrollment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.enrollment.vo.CourseQnaReplyVO;
import com.sist.web.domain.enrollment.vo.CourseQnaVO;

@Mapper
@Repository
public interface EnrollmentQnaMapper {
	public List<CourseQnaVO> courseQnaListData(
			@Param("course_no") int course_no,
			@Param("start") int start
	);
	
	@Select("SELECT COUNT(*) "
			+ "FROM course_qna "
			+ "WHERE course_no=#{course_no}")
	public int courseQnaRowCount(int course_no);
	
	@Select("SELECT no,subject,content,status "
			+ "FROM course_qna "
			+ "WHERE no=#{no}")
	public CourseQnaVO courseQnaDetailData(int no);
	
	public CourseQnaReplyVO courseQnaReplyData(int course_qna_no);
	
	@Insert("INSERT INTO course_qna(no,member_id,course_no,subject,content) "
			+ "VALUES("
			+ "    cq_no_seq.nextval,#{member_id},#{course_no},#{subject},#{content}"
			+ ")")
	public void courseQnaInsert(CourseQnaVO vo);
	
	@Delete("DELETE FROM course_qna WHERE no=#{no} AND member_id=#{member_id} AND status='N'")
	public int courseQnaDelete(@Param("no")int no, @Param("member_id")int member_id);
	
	@Update("UPDATE course_qna "
			+ "SET subject=#{subject},"
			+ "content=#{content} "
			+ "WHERE no=#{no} "
			+ "AND member_id=#{member_id} AND status='N'")
	public int courseQnaUpdate(CourseQnaVO vo);
}
