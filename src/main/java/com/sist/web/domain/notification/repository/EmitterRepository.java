package com.sist.web.domain.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * [WHAT] sse emitter 저장소 : 컨슈머에서 알림을 클라이언트에게 보낼때 여기서 emitter를 찾음
 *              key: memberID / value: emitter
 */
@Repository
public class EmitterRepository {
    private final Map<Integer, SseEmitter> emitters = new HashMap<>();

    public SseEmitter save(Integer memberId, SseEmitter emitter){
        emitters.put(memberId,emitter);
        return emitter;
    }

    public Optional<SseEmitter> findByMemberId(Integer memberId){
        return Optional.ofNullable(emitters.get(memberId));
    }

    public void deleteByMemberId(Integer memberId){
        emitters.remove(memberId);
    }
}
