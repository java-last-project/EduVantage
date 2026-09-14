package com.sist.web.domain.exam.service;

import com.sist.web.domain.exam.dto.AiExamCreateRequest;
import com.sist.web.domain.exam.dto.AiExamCreateResponse;
import com.sist.web.domain.exam.dto.AiExamQuestionResponse;
import com.sist.web.domain.exam.mapper.AiExamMapper;
import com.sist.web.domain.exam.vo.ExamEnrollmentVO;
import com.sist.web.domain.exam.vo.ExamOptionVO;
import com.sist.web.domain.exam.vo.ExamQuestionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiExamServiceImpl implements AiExamService{
	private static final int SUBJECT_MIN_LENGTH=2;
	private static final int SUBJECT_MAX_LENGTH=50;
	private static final int KEYWORD_MIN_LENGTH=2;
	private static final int KEYWORD_MAX_LENGTH=30;
	private static final Set<String> GARBAGE_INPUTS=Set.of(
			"아무거나","몰라","모름","기타","테스트","test","asdf","qwer","ㅋㅋㅋ"
	);
    private final ChatClient.Builder builder;
    private final AiExamMapper aiMapper;
    private final ExamService examService;

    @Override
    @Transactional
    public AiExamCreateResponse createExam(AiExamCreateRequest request,Integer memberId) {
        validateRequest(request);

        String difficultyText=getDifficultyText(request.getDifficulty());
        String keywords=String.join(", ",request.getKeywords());

        String prompt="""
				당신은 대한민국의 IT 시험 문제 전문 출제자입니다.
				교육용 객관식 시험을 생성하세요.

				시험명: %s
				주제: %s
				세부 키워드: %s
				난이도: %s
				문제 수: %d

				다음 조건을 반드시 지켜라.

				1. 모든 문제는 객관식 4지선다 문제로 생성한다.
				2. 각 문제의 options는 반드시 정확히 4개이다.
				3. answer는 정답 보기 번호이며 1, 2, 3, 4 중 하나이다.
				4. description에는 정답과 이유를 이해할 수 있는 해설을 작성한다.
				5. topic에는 해당 문제가 평가하는 핵심 개념 하나를 작성한다.
				6. keywords에는 해당 문제와 관련된 핵심 키워드를 2개 이상 5개 이하로 작성한다.
				7. 단순 암기형 문제만 반복하거나 동일한 개념을 표현만 바꿔 중복 출제하지 않는다.
				8. 오답 선택지도 학습자가 혼동할 수 있는 현실적인 내용으로 만들고, 정답만 지나치게 명백해지는 구성을 피한다.
				9. 문제와 선택지 사이에 모순이 없어야 하며 정답은 정확히 하나여야 한다.
				10. 사용자가 선택한 난이도를 실제 문제의 사고 수준과 선택지 구성에 반영한다.
				11. 사용자 키워드 중 주제와 직접 관련이 없거나 학습 개념으로 보기 어려운 항목은 문제에 억지로 포함하지 않는다.
				12. 주제와 키워드가 일관되지 않으면 주제를 우선하여 유효한 학습 범위에서 문제를 생성하고, 관련성이 낮은 키워드를 문제 문장에 끼워 넣지 않는다.
				13. 각 문제는 독립적인 학습·평가 가치가 있어야 하고 요청한 문제 수를 정확히 생성한다.
				14. topic과 keywords는 사용자 입력을 복사하지 말고 실제 생성된 문제와 문제 해결에 필요한 핵심 학습 개념을 기준으로 작성한다.
				""".formatted(
                request.getExamName(),
                request.getSubject(),
                keywords,
                difficultyText,
                request.getQuestionCount()
        );

        AiExamCreateResponse response=builder.build()
                .prompt()
                .user(prompt)
                .call()
                .entity(
                        AiExamCreateResponse.class,
                        spec->spec.validateSchema()
                );
        validateResponse(response,request.getQuestionCount());

        response.setExamName(request.getExamName());
        response.setSubject(request.getSubject());
        response.setDifficulty(request.getDifficulty());

        List<Integer> questionNos=saveQuestions(response,request);
		ExamEnrollmentVO enrollment=examService.createAiEnrollment(memberId,questionNos);

        response.setQuestionNos(questionNos);
        response.setEnrollmentNo(enrollment.getNo());

        return response;
    }

    private List<Integer> saveQuestions(AiExamCreateResponse response, AiExamCreateRequest request) {
        List<Integer> questionNos=new ArrayList<>();

        for(int i=0;i<response.getQuestions().size();i++) {

            AiExamQuestionResponse question=response.getQuestions().get(i);

            ExamQuestionVO questionVO=new ExamQuestionVO();

            questionVO.setTitle(question.getTitle());
            questionVO.setAnswer(String.valueOf(question.getAnswer()));
            questionVO.setDescription(question.getDescription());
			questionVO.setAi_topic(question.getTopic());
			questionVO.setAi_keywords(limitLength(
					String.join(",",question.getKeywords()),
					1000
			));

            // 객관식
            questionVO.setType(1);

            questionVO.setDifficulty(request.getDifficulty());
            questionVO.setScore(getQuestionScore(i,request.getQuestionCount()));

            aiMapper.insertAiQuestion(questionVO);

            ExamOptionVO optionVO=new ExamOptionVO();

            optionVO.setQuestion_no(questionVO.getNo());
            optionVO.setOption1(question.getOptions().get(0));
            optionVO.setOption2(question.getOptions().get(1));
            optionVO.setOption3(question.getOptions().get(2));
            optionVO.setOption4(question.getOptions().get(3));

            aiMapper.insertAiOption(optionVO);

            questionNos.add(questionVO.getNo());
        }

        return questionNos;
    }

    private void validateRequest(AiExamCreateRequest request) {
        if(request==null) {
            throw new IllegalArgumentException("시험 생성 요청이 없습니다.");
        }

        if(request.getExamName()==null || request.getExamName().isBlank()) {
            throw new IllegalArgumentException("시험명을 입력해주세요.");
        }
		request.setExamName(request.getExamName().trim());

		String subject=request.getSubject()!=null?request.getSubject().trim():"";
		if(subject.length()<SUBJECT_MIN_LENGTH || subject.length()>SUBJECT_MAX_LENGTH) {
			throw new IllegalArgumentException("주제는 2~50자로 입력해주세요.");
		}
		if(!hasMeaningfulCharacter(subject) || isGarbageInput(subject)) {
			throw new IllegalArgumentException("학습 주제를 구체적으로 입력해주세요.");
		}
		request.setSubject(subject);

		List<String> normalizedKeywords=normalizeRequestKeywords(request.getKeywords());
		if(normalizedKeywords.isEmpty() || normalizedKeywords.size()>5) {
			throw new IllegalArgumentException("키워드는 1~5개 입력해주세요.");
		}
		for(String keyword:normalizedKeywords){
			if(keyword.length()<KEYWORD_MIN_LENGTH || keyword.length()>KEYWORD_MAX_LENGTH){
				throw new IllegalArgumentException("키워드는 2~30자로 입력해주세요.");
			}
			if(!hasMeaningfulCharacter(keyword) || isGarbageInput(keyword)){
				throw new IllegalArgumentException("학습 주제와 관련된 구체적인 키워드를 입력해주세요.");
			}
		}
		request.setKeywords(normalizedKeywords);

        if(request.getDifficulty()==null ||
                request.getDifficulty()<1 ||
                request.getDifficulty()>3) {
            throw new IllegalArgumentException("올바르지 않은 난이도입니다.");
        }

        if(request.getQuestionCount()==null ||
                (request.getQuestionCount()!=10 &&
                        request.getQuestionCount()!=20 &&
                        request.getQuestionCount()!=30)) {
            throw new IllegalArgumentException("문제 수는 10, 20, 30 중 하나여야 합니다.");
        }
    }

    private String getDifficultyText(Integer difficulty) {
        return switch(difficulty) {
            case 1 -> "하";
            case 2 -> "중";
            case 3 -> "상";
            default -> throw new IllegalArgumentException("올바르지 않은 난이도입니다.");
        };
    }

    private void validateResponse(AiExamCreateResponse response, Integer questionCount) {
        if(response==null || response.getQuestions()==null) {
            throw new IllegalStateException("AI 시험 생성 결과가 없습니다.");
        }
        if(response.getQuestions().size()!=questionCount) {
            throw new IllegalStateException("AI가 요청한 문제 수와 다르게 생성했습니다.");
        }

        for(AiExamQuestionResponse question:response.getQuestions()) {
            if(question.getTitle()==null || question.getTitle().isBlank()) {
                throw new IllegalStateException("문제 내용이 비어 있습니다.");
            }
            if(question.getOptions()==null || question.getOptions().size()!=4) {
                throw new IllegalStateException("객관식 보기는 반드시 4개여야 합니다.");
            }
            if(question.getAnswer()==null ||
                    question.getAnswer()<1 ||
                    question.getAnswer()>4) {
                throw new IllegalStateException("정답 번호가 올바르지 않습니다.");
            }
            if(question.getDescription()==null ||
                    question.getDescription().isBlank()) {
                throw new IllegalStateException("문제 해설이 비어 있습니다.");
            }
			if(question.getTopic()==null || question.getTopic().isBlank()) {
                throw new IllegalStateException("문제 주제가 비어 있습니다.");
            }
			question.setTopic(limitLength(question.getTopic().trim(),300));

			List<String> normalizedKeywords=normalizeKeywords(question.getKeywords());
			if(normalizedKeywords.size()<2) {
                throw new IllegalStateException("문제 키워드는 2~5개여야 합니다.");
            }
			question.setKeywords(normalizedKeywords);
        }
    }
    private int getQuestionScore(int index,Integer questionCount) {

        if(questionCount==10) {
            return 10;
        }

        if(questionCount==20) {
            return 5;
        }

        if(questionCount==30) {
            return index<10?4:3;
        }

        throw new IllegalArgumentException("올바르지 않은 문제 수입니다.");
    }

	private String limitLength(String value,int maxLength) {
		return value.length()<=maxLength?value:value.substring(0,maxLength);
	}

	private List<String> normalizeKeywords(List<String> keywords) {
		if(keywords==null){
			return Collections.emptyList();
		}
		return keywords.stream()
				.filter(Objects::nonNull)
				.map(String::trim)
				.filter(keyword->!keyword.isBlank())
				.map(keyword->limitLength(keyword,100))
				.distinct()
				.limit(5)
				.collect(Collectors.toList());
	}

	private List<String> normalizeRequestKeywords(List<String> keywords) {
		if(keywords==null){
			return Collections.emptyList();
		}
		Map<String,String> normalized=new LinkedHashMap<>();
		for(String keyword:keywords){
			if(keyword==null || keyword.isBlank()){
				continue;
			}
			String trimmed=keyword.trim();
			normalized.putIfAbsent(trimmed.toLowerCase(Locale.ROOT),trimmed);
		}
		return new ArrayList<>(normalized.values());
	}

	private boolean hasMeaningfulCharacter(String value) {
		return value.codePoints().anyMatch(codePoint->
				(codePoint>='a' && codePoint<='z') ||
				(codePoint>='A' && codePoint<='Z') ||
				(codePoint>='0' && codePoint<='9') ||
				(codePoint>=0xAC00 && codePoint<=0xD7A3)
		);
	}

	private boolean isGarbageInput(String value) {
		return GARBAGE_INPUTS.contains(value.trim().toLowerCase(Locale.ROOT));
	}
}
