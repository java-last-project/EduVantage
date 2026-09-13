package com.sist.web.domain.enrollment.service;

import com.sist.web.domain.enrollment.mapper.CourseVideoProgressMapper;
import com.sist.web.domain.enrollment.mapper.EnrollmentMapper;
import com.sist.web.domain.enrollment.vo.CourseVideoProgressVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class VideoProgressServiceImpl implements VideoProgressService{
    private final CourseVideoProgressMapper pMapper;
    private final EnrollmentMapper eMapper;

    @Override
    @Transactional
    public void saveProgress(CourseVideoProgressVO vo,Integer member_id) {
        // enrollment_no·video_no 변조 방지를 위한 수강 영상 권한 확인
        int check=pMapper.videoAccessCheck(vo.getEnrollment_no(),vo.getVideo_no(),member_id);
        if(check==0){
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        // 재생 종료 오차를 고려한 완료 기준 90%
        String completed=vo.getProgress()>=90?"Y":"N";
        vo.setCompleted(completed);

        // 영상 진도 + 강좌 진도 함께 반영
        pMapper.videoProgressSave(vo);
        int totalCount=pMapper.videoTotalCount(vo.getEnrollment_no());
        int completedCount=pMapper.videoCompletedCount(vo.getEnrollment_no());
        int progress=0;
        if(totalCount>0){
            // 전체 진도는 완료 영상 수 기준
            progress=(int)Math.round((double)completedCount/totalCount*100);
        }
        String isCompleted=progress>=100?"Y":"N";
        eMapper.videoProgressUpdate(vo.getEnrollment_no(),progress,isCompleted);
    }

    @Override
    public List<CourseVideoProgressVO> progressList(Integer enrollment_no,Integer member_id) {
        int check=pMapper.enrollmentAccessCheck(enrollment_no,member_id);
        if(check==0){
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
        return pMapper.videoProgressList(enrollment_no);
    }
}
