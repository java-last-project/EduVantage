package com.sist.web.domain.exam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.sist.web.domain.exam.mapper.ExamGradingMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamGradingServiceImpl implements ExamGradingService {
    private final ExamGradingMapper gradingMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPendingSubjectiveList(int graderId) {
        return gradingMapper.selectPendingSubjectiveList(graderId);
    }

    @Override
    @Transactional
    public boolean claimTask(int answerNo, int graderId) {
        // 선점 여부는 조건부 UPDATE 결과로 판단해 중복 채점 방지
        return gradingMapper.claimGradingTask(gradingParams(answerNo, graderId)) > 0;
    }

    @Override
    @Transactional
    public boolean releaseClaim(int answerNo, int graderId) {
        return gradingMapper.releaseGradingClaim(gradingParams(answerNo, graderId)) > 0;
    }

    @Override
    @Transactional
    public void gradeSubjective(int answerNo, int graderId, Integer score, Boolean correct) {
        Map<String, Object> params = gradingParams(answerNo, graderId);

        // 선점 여부와 채점자 일치 여부 DB 재검증
        Map<String,Object> claimedAnswer=gradingMapper.selectClaimedAnswer(params);
        if (claimedAnswer == null) {
            throw new IllegalStateException("선점하지 않았거나 이미 처리된 답안입니다.");
        }

		// Oracle/MyBatis 설정에 따라 Map 키 대소문자가 달라질 수 있음
		Object rawEnrollmentNo=claimedAnswer.get("ENROLLMENT_NO")!=null
				?claimedAnswer.get("ENROLLMENT_NO"):claimedAnswer.get("enrollment_no");
		Object rawMaxScore=claimedAnswer.get("MAX_SCORE")!=null
				?claimedAnswer.get("MAX_SCORE"):claimedAnswer.get("max_score");
		int enrollmentNo=Integer.parseInt(String.valueOf(rawEnrollmentNo));
		int maxScore=Integer.parseInt(String.valueOf(rawMaxScore));
		Object rawPracticeExam=claimedAnswer.get("PRACTICE_EXAM")!=null
				?claimedAnswer.get("PRACTICE_EXAM"):claimedAnswer.get("practice_exam");
		boolean practiceExam=Integer.parseInt(String.valueOf(rawPracticeExam))==1;
		if(practiceExam){
			if(correct==null || score!=null){
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"상시시험은 정답 또는 오답을 선택해야 합니다.");
			}
			// 상시시험은 판정만 저장하고 문항 점수는 총점에 사용하지 않음
			params.put("score",0);
			params.put("isCorrect",correct?"Y":"N");
		}else{
			if(score==null || correct!=null || score<0 || score>maxScore){
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"문항 배점 범위를 벗어났습니다.");
			}
			params.put("score",score);
			params.put("isCorrect",score>0?"Y":"N");
		}
		params.put("practiceExam",practiceExam?1:0);
		params.put("maxScore",maxScore);

        if (gradingMapper.gradeSubjectiveAnswer(params) == 0) {
            throw new IllegalStateException("선점하지 않았거나 이미 처리된 답안입니다.");
        }

        // 마지막 주관식 채점 후에만 총점 확정
        if (gradingMapper.countRemainingPending(enrollmentNo) == 0) {
			if(practiceExam){
				// 상시시험은 주관식까지 확정된 뒤 정답률로 최종 점수 계산
				gradingMapper.finalizePracticeEnrollmentScore(enrollmentNo);
			}else{
				gradingMapper.finalizeEnrollmentScore(enrollmentNo);
			}
        }
    }

    private Map<String, Object> gradingParams(int answerNo, int graderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("answerNo", answerNo);
        params.put("graderId", graderId);
        return params;
    }
}
