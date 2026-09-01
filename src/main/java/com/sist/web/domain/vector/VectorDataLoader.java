package com.sist.web.domain.vector;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * [WHAT] (최초 1회)벡터 DB에 Data를 insert하는 컴포넌트
 */
@Component
@RequiredArgsConstructor
public class VectorDataLoader {
    private final EmbeddingService embeddingService;

    //벡터 전용 JdbcTemplate 가져오기 (@Qualifier 명시 필요)

    //전체 강의 데이터 조회해서 임베딩 후 pgvector에 적재
    public void loadAllCourse(){

    }

    //[보조메서드] 강의제목+기술스택 텍스트 합치기
}
