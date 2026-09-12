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
    public void saveProgress(CourseVideoProgressVO vo) {
        String completed=vo.getProgress()>=90?"Y":"N";
        vo.setCompleted(completed);

        pMapper.videoProgressSave(vo);
        int totalCount=pMapper.videoTotalCount(vo.getEnrollment_no());
        int completedCount=pMapper.videoCompletedCount(vo.getEnrollment_no());
        int progress=0;
        if(totalCount>0){
            progress=(int)Math.round((double)completedCount/totalCount*100);
        }
        String isCompleted=progress>=100?"Y":"N";
        eMapper.videoProgressUpdate(vo.getEnrollment_no(),progress,isCompleted);
    }

    @Override
    public List<CourseVideoProgressVO> progressList(Integer enrollment_no) {
        return pMapper.videoProgressList(enrollment_no);
    }
}
