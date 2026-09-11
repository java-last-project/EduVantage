package com.sist.web.domain.vector;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;


/**
 * [WHAT] 임베딩(텍스트 데이터 -> 벡터) 로직 구현
 */
@Service
@RequiredArgsConstructor
public class EmbeddingService {
    private final EmbeddingModel embeddingModel;

    //텍스트 데이터 -> 벡터(float 배열)로 변환하는 래퍼 메서드
    // 1텍스트 1벡터
    public float[] embed(String text){
        float[] embedding = embeddingModel.embed(text);

        return embedding;
    }
}
