package com.sist.web.domain.enrollment.mapper;

import com.sist.web.domain.enrollment.vo.CourseVideoProgressVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseVideoProgressMapper {
    public List<CourseVideoProgressVO> videoProgressList(Integer enrollment_no);
    public void videoProgressSave(CourseVideoProgressVO vo);
    public int videoTotalCount(Integer enrollment_no);
    public int videoCompletedCount(Integer enrollment_no);
    public int videoAccessCheck(@Param("enrollment_no")Integer enrollment_no,@Param("video_no")Integer video_no,@Param("member_id")Integer member_id);
    public int enrollmentAccessCheck(@Param("enrollment_no")Integer enrollment_no,@Param("member_id")Integer member_id);
}
