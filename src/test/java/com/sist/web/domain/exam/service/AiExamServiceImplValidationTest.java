package com.sist.web.domain.exam.service;

import com.sist.web.domain.exam.dto.AiExamCreateRequest;
import com.sist.web.domain.exam.mapper.AiExamMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AiExamServiceImplValidationTest {
    @Mock
    private ChatClient.Builder chatClientBuilder;
    @Mock
    private AiExamMapper aiExamMapper;
    @Mock
    private ExamService examService;

    private AiExamServiceImpl service;

    @BeforeEach
    void setUp() {
        service=new AiExamServiceImpl(chatClientBuilder,aiExamMapper,examService);
    }

    @ParameterizedTest
    @MethodSource("validInputs")
    void acceptsValidTechnologyInputs(String subject,List<String> keywords) {
        AiExamCreateRequest request=request(subject,keywords);

        ReflectionTestUtils.invokeMethod(service,"validateRequest",request);

        assertThat(request.getSubject()).isEqualTo(subject.trim());
        assertThat(request.getKeywords()).containsExactlyElementsOf(keywords);
    }

    private static Stream<Arguments> validInputs() {
        return Stream.of(
                Arguments.of("Java 컬렉션",List.of("ArrayList","HashMap")),
                Arguments.of("C++ 메모리 관리",List.of("pointer","RAII")),
                Arguments.of("Spring Boot",List.of("REST API","JPA")),
                Arguments.of("프론트엔드",List.of("C#","vue.js","try-catch","Spring-Boot"))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidInputs")
    void rejectsLowQualityInputs(String subject,List<String> keywords,String expectedMessage) {
        AiExamCreateRequest request=request(subject,keywords);

        assertThatThrownBy(()->ReflectionTestUtils.invokeMethod(service,"validateRequest",request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }

    private static Stream<Arguments> invalidInputs() {
        return Stream.of(
                Arguments.of("",List.of("Java"),"주제는 2~50자"),
                Arguments.of("J",List.of("Java"),"주제는 2~50자"),
                Arguments.of("Java",List.of(),"키워드는 1~5개"),
                Arguments.of("Java",List.of("a"),"키워드는 2~30자"),
                Arguments.of("Java",List.of("!!!"),"구체적인 키워드"),
                Arguments.of("Java",List.of("아무거나"),"구체적인 키워드"),
                Arguments.of("테스트",List.of("Java"),"학습 주제를 구체적")
        );
    }

    @Test
    void normalizesKeywordsCaseInsensitivelyAndKeepsFirstValue() {
        AiExamCreateRequest request=request(
                " Java ",
                java.util.Arrays.asList("ArrayList"," arraylist ",null," ","HashMap"," hashmap ")
        );

        ReflectionTestUtils.invokeMethod(service,"validateRequest",request);

        assertThat(request.getSubject()).isEqualTo("Java");
        assertThat(request.getKeywords()).containsExactly("ArrayList","HashMap");
    }

    private static AiExamCreateRequest request(String subject,List<String> keywords) {
        AiExamCreateRequest request=new AiExamCreateRequest();
        request.setExamName("입력 검증 시험");
        request.setSubject(subject);
        request.setKeywords(keywords);
        request.setDifficulty(2);
        request.setQuestionCount(10);
        return request;
    }
}
