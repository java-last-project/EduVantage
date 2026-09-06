package com.sist.web.domain.notification.scheduler;

import com.sist.web.domain.notification.entity.NotificationType;
import com.sist.web.domain.notification.entity.Notifications;
import com.sist.web.domain.notification.mapper.ScheduledExamMapper;
import com.sist.web.domain.notification.repository.EmitterRepository;
import com.sist.web.domain.notification.repository.NotificationRepository;
import com.sist.web.domain.notification.vo.NotificationVO;
import com.sist.web.domain.notification.vo.SubscribeExamVO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ScheduledExamDdayScheduler {
    private final ScheduledExamMapper scheduledExamMapper;
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    
    //@Scheduled(fixedRate = 30000)
    @Scheduled(cron = "0 0 7 * * *")
    public void scheduledExamDday() {
        checkDday(3, "notified_d3", NotificationType.EXAM_SUBSCRIBED);
        checkDday(0, "notified_dd", NotificationType.EXAM_SUBSCRIBED);

    }
    
    private void checkDday(int targetDate, String column, NotificationType type) {
        List<SubscribeExamVO> exams = scheduledExamMapper.findScheduledExamByDday(targetDate);
        
        for(SubscribeExamVO exam : exams) {
            List<Integer> memberIds = scheduledExamMapper.findUnnotifiedSubscribers(exam.getExamNo(), column);

            for(Integer memberId: memberIds) {
                String title = targetDate == 0 ? "시험 당일입니다!":"시험이 "+targetDate+"일 남았습니다!";
                String content = exam.getExamTitle()+ " 시험을 확인하세요";

                Notifications noti = Notifications.builder()
                        .memberId(memberId)
                        .type(type)
                        .title(title)
                        .content(content)
                        .relatedId(exam.getExamNo())
                        .eventKey(UUID.randomUUID().toString())
                        .build();
                notificationRepository.save(noti);

                SseEmitter emitter = emitterRepository.findByMemberId(memberId).orElse(null);
                if(emitter != null){
                    try{
                        emitter.send(NotificationVO.from(noti));
                    }catch(IOException e){
                        emitterRepository.deleteByMemberId(memberId);
                    }
                }

                scheduledExamMapper.markNotified(memberId, exam.getExamNo(), column);
            }
        }
    }
}
