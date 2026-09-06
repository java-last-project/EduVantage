package com.sist.web.domain.notification.service;

import com.sist.web.domain.notification.entity.NotificationType;
import com.sist.web.domain.notification.entity.Notifications;
import com.sist.web.domain.notification.mapper.NotificationMapper;
import com.sist.web.domain.notification.repository.NotificationRepository;
import com.sist.web.domain.notification.vo.NotificationVO;
import com.sist.web.domain.notification.vo.SubscribeExamVO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.snakeyaml.engine.v2.exceptions.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.rmi.AlreadyBoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService{
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationVO> findRecentNotifications(int memberId) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);
        Date targetDate = cal.getTime();
        List<NotificationVO> list = notificationMapper.findRecentNotifications(memberId, targetDate);
        list.forEach(n -> System.out.println("no=" + n.getNo() + ", read=" + n.isRead()));
        return list;
    }

    @Override
    public void markAllAsRead(List<Integer> nos) {
        notificationMapper.markAllAsRead(nos);
    }

    @Override
    @Transactional
    public void subscribeExam(int memberId, int examNo) {
        try{
            //1. 구독 정보 저장
            notificationMapper.subscribeExam(memberId, examNo);

            //2. 알림 내역 저장
            SubscribeExamVO exam = notificationMapper.subscribeExamInfo(examNo);

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
