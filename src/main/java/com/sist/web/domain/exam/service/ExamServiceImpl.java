package com.sist.web.domain.exam.service;

import java.time.LocalDateTime;
import java.util.*;

import com.sist.web.domain.exam.vo.ExamUserAnswerVO;
import com.sist.web.domain.exam.vo.ScheduledExamVO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sist.web.domain.exam.mapper.ExamMapper;
import com.sist.web.domain.exam.vo.ExamEnrollmentVO;
import com.sist.web.domain.exam.vo.ExamQuestionVO;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService{
	private static final int EXAM_LIMIT_MINUTES=120;
	private static final int SUBMIT_GRACE_SECONDS=30;
	private final ExamMapper eMapper;
	
	@Override
	public List<ExamQuestionVO> examDetailData(Integer examNo,Integer theme,int count) {
		Map<String, Object> map=new HashMap<>();
		map.put("exam_no", examNo);
		map.put("theme", theme);
		map.put("count", count);
		List<ExamQuestionVO> list=eMapper.examDetailData(map);
		return list;
	}

	@Override
	@Transactional
	public ExamEnrollmentVO getOrCreateEnrollment(int memberId, Integer examNo, Integer theme) {
		Map<String,Object> map=new HashMap<>();
		map.put("member_id",memberId);
		map.put("exam_no",examNo);
		map.put("theme",theme!=null&&theme!=0?theme:null);

		if(examNo!=null && examNo>0){
			ScheduledExamVO svo=eMapper.getScheduledExam(examNo);
			if(svo==null){
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 시험입니다.");
			}
			LocalDateTime now=LocalDateTime.now();
			if(now.isBefore(svo.getOpen_date())){
				throw new ResponseStatusException(HttpStatus.CONFLICT,"아직 응시 기간이 아닙니다.");
			}
			if(now.isAfter(svo.getClose_date())){
				throw new ResponseStatusException(HttpStatus.CONFLICT,"응시 기간이 종료되었습니다.");
			}
			if(eMapper.countScheduledExamQuestions(examNo)==0){
				throw new ResponseStatusException(HttpStatus.CONFLICT,"등록된 시험 문제가 없습니다.");
			}
			if(eMapper.findCompletedEnrollment(map)!=null){
				throw new ResponseStatusException(HttpStatus.CONFLICT,"이미 응시를 완료한 시험입니다.");
			}
		}

		// 제한 시간 내 재접속 시 기존 응시 기록 유지
		ExamEnrollmentVO activeVo=eMapper.findActiveEnrollment(map);
		if(activeVo!=null){
			return activeVo;
		}

		ExamEnrollmentVO vo=new ExamEnrollmentVO();
		vo.setMember_id(memberId);
		vo.setExam_no(examNo);
		vo.setTheme(theme!=null&&theme!=0?theme:null);
		vo.setStarttime(LocalDateTime.now());
		eMapper.insertEnrollment(vo);
		return vo;
	}

	@Override
	public String getExamTitle(Integer examNo){
		return eMapper.getScheduledExamTitle(examNo);
	}

	@Override
	public int getExamLimitMinutes(Integer examNo){
		if(examNo!=null && examNo>0){
			ScheduledExamVO svo=eMapper.getScheduledExam(examNo);
			if(svo!=null && svo.getTime_limit()!=null){
				return svo.getTime_limit();
			}
		}
		return EXAM_LIMIT_MINUTES;
	}

	@Override
	@Transactional
	public Map<String, Object> submitExam(int memberId,Map<String, Object> params) {
		Object rawEnrollmentNo=params.get("enrollmentNo");
		if(rawEnrollmentNo==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"응시기록 번호가 없습니다.");
		}
		int enrollmentNo;
		try{
			enrollmentNo=Integer.parseInt(String.valueOf(rawEnrollmentNo));
		}catch(NumberFormatException ex){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"잘못된 응시기록 번호입니다.");
		}
		Map<String,Object> enrollmentParams=new HashMap<>();
		enrollmentParams.put("enrollmentNo",enrollmentNo);
		enrollmentParams.put("memberId",memberId);
		ExamEnrollmentVO enrollment=eMapper.getEnrollmentForMember(enrollmentParams);
		if(enrollment==null){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 응시기록입니다.");
		}
		if(enrollment.getEndtime()!=null){
			throw new ResponseStatusException(HttpStatus.CONFLICT,"이미 제출이 완료된 시험입니다.");
		}
		int limitMinutes=getExamLimitMinutes(enrollment.getExam_no());
		if(enrollment.getStarttime()==null || LocalDateTime.now().isAfter(
				enrollment.getStarttime().plusMinutes(limitMinutes).plusSeconds(SUBMIT_GRACE_SECONDS))){
			throw new ResponseStatusException(HttpStatus.CONFLICT,"시험 제한 시간이 종료되었습니다.");
		}
		if(enrollment.getExam_no()!=null && enrollment.getExam_no()>0){
			ScheduledExamVO svo=eMapper.getScheduledExam(enrollment.getExam_no());
			if(svo==null || LocalDateTime.now().isAfter(svo.getClose_date())){
				throw new ResponseStatusException(HttpStatus.CONFLICT,"응시 기간이 종료되었습니다.");
			}
		}

		Map<String,Object> raw=(Map<String,Object>)params.get("answers");
		if(raw==null || raw.isEmpty()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"제출된 답안이 없습니다.");
		}
		List<Integer> qno=new ArrayList<>();
		try{
			for(String key:raw.keySet()){
				qno.add(Integer.parseInt(key));
			}
		}catch(NumberFormatException ex){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"잘못된 문제 번호가 포함되어 있습니다.");
		}
		Map<String,Object> questionParams=new HashMap<>();
		questionParams.put("exam_no",enrollment.getExam_no());
		questionParams.put("theme",enrollment.getTheme());
		questionParams.put("qno",qno);
		List<ExamQuestionVO> questions=eMapper.getQuestionForGrading(questionParams);
		if(questions.isEmpty()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"채점할 시험 문제가 없습니다.");
		}

		Set<Integer> questionNoSet=new HashSet<>();
		for(ExamQuestionVO qvo:questions){
			questionNoSet.add(qvo.getNo());
		}
		if(!questionNoSet.containsAll(qno)){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"시험에 포함되지 않은 문제가 있습니다.");
		}

		// 채점
		List<ExamUserAnswerVO> answers=new ArrayList<>();
		int totalScore=0;
		boolean hasSubjective=false;

		for(ExamQuestionVO qvo:questions){
			ExamUserAnswerVO avo=new ExamUserAnswerVO();
			avo.setEnrollment_no(enrollmentNo);
			avo.setQuestion_no(qvo.getNo());

			Object userAnsObj=raw.get(String.valueOf(qvo.getNo()));
			String userAns=userAnsObj!=null?String.valueOf(userAnsObj).trim():"";
			avo.setUser_answer(userAns);
			if(qvo.getType()==1){ // 객관식
				boolean isCorrect=qvo.getAnswer()!=null && qvo.getAnswer().trim().equals(userAns);
				if(isCorrect){
					avo.setIs_correct("Y");
					avo.setScore(qvo.getScore());
					totalScore+=qvo.getScore();
				}else{
					avo.setIs_correct("N");
					avo.setScore(0);
				}
			}else if(qvo.getType()==2 && userAns.isBlank()){
				avo.setIs_correct("N");
				avo.setScore(0);
			}else{
				avo.setIs_correct("W");
				avo.setScore(0);
				hasSubjective=true;
			}
			answers.add(avo);
		}

		// 답안 저장 + 응시 상태 함께 반영
		if(!answers.isEmpty()){
			eMapper.insertUserAnswers(answers);
		}

		ExamEnrollmentVO evo=new ExamEnrollmentVO();
		evo.setNo(enrollmentNo);
		evo.setMember_id(memberId);
		evo.setEndtime(LocalDateTime.now());
		evo.setTotalscore(totalScore);
		evo.setStatus(hasSubjective?"WAITING":"COMPLETE");

		if(eMapper.updateEnrollmentFinish(evo)==0){
			throw new ResponseStatusException(HttpStatus.CONFLICT,"이미 제출이 완료된 시험입니다.");
		}

		Map<String,Object> result=new HashMap<>();
		result.put("status",evo.getStatus());
		result.put("enrollmentNo",enrollmentNo);
		result.put("totalscore",totalScore);
		return result;
	}

	@Override
	public Map<String, Object> getExamResultData(int memberId,int enrollmentNo) {
		Map<String,Object> params=new HashMap<>();
		params.put("memberId",memberId);
		params.put("enrollmentNo",enrollmentNo);
		Map<String,Object> map=eMapper.selectExamResultMaster(params);
		if(map==null){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 응시기록입니다.");
		}
		List<Map<String,Object>> details=eMapper.selectExamResultDetails(enrollmentNo);
		// MyBatis Map key 대소문자 차이 대응
		Object rawExamNo = map.get("EXAM_NO") != null ? map.get("EXAM_NO") : map.get("exam_no");
		Integer examNo = null;
		if (rawExamNo != null && !String.valueOf(rawExamNo).isEmpty()) {
			examNo = Integer.parseInt(String.valueOf(rawExamNo));
		}

		String examTitle="상시 모의고사";
		if (examNo!=null && examNo>0) {
			String sTitle=eMapper.getScheduledExamTitle(examNo);
			if (sTitle!=null) examTitle=sTitle;
		}
		Map<String, Object> response = new HashMap<>();
		response.put("master", map);
		response.put("examTitle", examTitle);
		response.put("details", details);
		return response;
	}

	@Override
	public Integer getScheduledExamResult(int memberId,int examNo){
		Map<String,Object> map=new HashMap<>();
		map.put("member_id",memberId);
		map.put("exam_no",examNo);
		ExamEnrollmentVO vo=eMapper.findCompletedEnrollment(map);
		return vo!=null?vo.getNo():null;
	}

	@Override
	public List<Map<String, Object>> getMyExamList(int memberId) {
		return eMapper.selectMyExamList(memberId);
	}

}
