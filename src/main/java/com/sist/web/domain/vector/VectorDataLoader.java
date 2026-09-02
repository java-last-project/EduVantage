package com.sist.web.domain.vector;

import com.pgvector.PGvector;
import com.sist.web.domain.notification.entity.Course;
import com.sist.web.domain.notification.entity.TechStack;
import com.sist.web.domain.notification.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * [WHAT] (최초 1회)벡터 DB에 Data를 insert하는 컴포넌트
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class VectorDataLoader {
    private final EmbeddingService embeddingService;
    private final CourseRepository courseRepository;
    //벡터 전용 JdbcTemplate 가져오기 (@Qualifier 명시 필요)
    @Qualifier("vectorJdbcTemplate")
    private final JdbcTemplate vectorJdbcTemplate;

    private static final String INSERT_SQL = """
            INSERT INTO course_vector (course_no, chunk_text, embedding_data)
                                          VALUES (?, ?, ?::vector)
            """;

    //전체 강의 데이터 조회해서 임베딩 후 pgvector에 적재
    public void loadAllCourse(){
        List<Course> courses = courseRepository.findAll();

        int successCnt = 0;
        int failCnt = 0;

        for (Course course:courses){
            try{
                String chunkText = buildChunkText(course);
                float[] embedding = embeddingService.embed(chunkText);

                vectorJdbcTemplate.update(INSERT_SQL,
                        course.getNo(),
                        chunkText,
                        new PGvector(embedding).toString()
                );

                successCnt++;
            }catch (Exception e) {
                failCnt++;
                log.error("embedding fail - course_no:" + course.getNo() + " - " + e.getMessage() + ", reason:" + e.getMessage());
            }
        }
        log.info("embedding complete:"+successCnt+", fail:"+failCnt);
    }

    //[보조메서드] 강의제목+기술스택 텍스트 합치기
    private String buildChunkText(Course course){
        String techStackText = course.getTechStacks().stream()
                .map(TechStack::getTech)
                .collect(Collectors.joining(" "));
        return course.getTitle()+ " " + techStackText;
    }
}
