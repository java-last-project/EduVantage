package com.sist.web.domain.notification.service;

import com.sist.web.domain.exam.vo.ScheduledExamVO;
import com.sist.web.domain.notification.entity.NotificationType;
import com.sist.web.domain.notification.entity.Notifications;
import com.sist.web.domain.notification.mapper.NotificationMapper;
import com.sist.web.domain.notification.mapper.ScheduledExamMapper;
import com.sist.web.domain.notification.repository.NotificationRepository;
import com.sist.web.domain.notification.vo.SubscribeExamVO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.snakeyaml.engine.v2.exceptions.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledExamServiceImpl implements ScheduledExamService {
    private final ScheduledExamMapper scheduledExamMapper;
    private final NotificationRepository notificationRepository;

    public Page<ScheduledExamVO> getExamByMonth(int year, int month, Pageable pageable){
        int offset = pageable.getPageNumber()*pageable.getPageSize();
        int size = pageable.getPageSize();
        List<ScheduledExamVO> list = scheduledExamMapper.getScheduledExamByMonth(year, month, offset, size);
        int totalCount = scheduledExamMapper.countScheduledExamByMonth(year, month);

        return new PageImpl<>(list, pageable, totalCount);
    }

    @Override
    @Transactional
    public void subscribeExam(int memberId, int examNo) {
        try{
            //1. 구독 정보 저장
            scheduledExamMapper.subscribeExam(memberId, examNo);

            //2. 알림 내역 저장
            SubscribeExamVO exam = scheduledExamMapper.subscribeExamInfo(examNo);

            Notifications noti = Notifications.builder()
                    .memberId(memberId)
                    .type(NotificationType.EXAM_SUBSCRIBED)
                    .title("정기 시험 알림 구독")
                    .content(exam.getExamTitle()+ " 시험이 다가오면 알려드릴게요")
                    .relatedId(examNo)
                    .eventKey(UUID.randomUUID().toString())
                    .build();

            notificationRepository.save(noti);
        }catch (DuplicateKeyException e){
            log.error("already subscribed exam");
        }
    }
}
