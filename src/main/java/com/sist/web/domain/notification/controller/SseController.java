package com.sist.web.domain.notification.controller;

import com.sist.web.domain.member.mapper.MemberMapper;
import com.sist.web.domain.notification.producer.CourseCompletedProducer;
import com.sist.web.domain.notification.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

/**
 * SSE는 실시간이므로 REST로 처리 불가
 */
@Controller
@RequiredArgsConstructor
public class SseController {
    private final MemberMapper memberMapper;
    private final EmitterRepository emitterRepository;
    private final CourseCompletedProducer  courseCompletedProducer;

    /**
     * [WHAT] 클라이언트가 EventSource로 연결할 포인트
     *
     * @return SseEmitter
     *          :연결을 끊지 않고 유지되는 데이터 통로 객체
     */
    @GetMapping("/sse")
    public SseEmitter getSseEmitter(Authentication authentication){
        //유저 정보 꺼내기
        String username = authentication.getName();
        Integer memberId = memberMapper.memberInfoData(username).getMember_id();

        //타임아웃 설정 -> 무제한: 무한 재연결 사이클 제거
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        //emitter 저장
        emitterRepository.save(memberId, emitter);

        //연결이 끊기면 저장소에서 emitter 삭제 콜백 설정
        emitter.onCompletion(()->emitterRepository.deleteByMemberId(memberId));
        emitter.onTimeout(()->emitterRepository.deleteByMemberId(memberId));
        emitter.onError((e)->emitterRepository.deleteByMemberId(memberId));

        //테스트
        /*
        try{
            emitter.send(SseEmitter.event()
                    .data(Map.of("title","테스트", "content","서버 연결이 완료됐습니다.")));

        }catch (IOException e){
            emitterRepository.deleteByMemberId(memberId);
        }

        courseCompletedProducer.publishCompletion(memberId, 2, "JAVA 기초");
        */

        return emitter;
    }
}
