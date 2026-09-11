package com.sist.web.domain.vector;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// [WHAT] SpringAI 임베딩 모델의 임베딩 호출 테스트
@RestController
@RequiredArgsConstructor
public class EmbeddingController {
    private final EmbeddingService embeddingService;

    @GetMapping("/embed")
    public ResponseEntity<String> test(@RequestParam String text){
        float[] vector = embeddingService.embed(text);

        return ResponseEntity.ok("차원 수"+vector.length);
    }
}
