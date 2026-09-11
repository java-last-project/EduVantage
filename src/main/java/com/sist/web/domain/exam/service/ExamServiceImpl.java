package com.sist.web.domain.exam.service;

import java.time.LocalDateTime;
import java.util.*;

import com.sist.web.domain.exam.vo.ExamUserAnswerVO;
import com.sist.web.domain.notification.mapper.ScheduledExamMapper;
import org.springframework.stereotype.Service;

import com.sist.web.domain.exam.mapper.ExamMapper;
import com.sist.web.domain.exam.vo.ExamEnrollmentVO;
import com.sist.web.domain.exam.vo.ExamQuestionVO;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService{
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
	@Transactional
	public Map<String, Object> submitExam(Map<String, Object> params) {
		int enrollmentNo=(Integer)params.get("enrollmentNo");
		Map<String,Object> raw=(Map<String,Object>)params.get("answers");
		if(raw==null || raw.isEmpty()){
			throw new IllegalArgumentException("제출된 답안이 없습니다");
		}
		List<Integer> qno=new ArrayList<>();
		for(String key:raw.keySet()){
			qno.add(Integer.parseInt(key));
		}
		List<ExamQuestionVO> questions=eMapper.getQuestionForGrading(qno);

		int totalCount=questions.size();
		double pointPerQuestion=100.0/totalCount;

		// 채점
		List<ExamUserAnswerVO> answers=new ArrayList<>();
		double rawTotalScore=0.0;
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
					avo.setScore((int)Math.round(pointPerQuestion));
					rawTotalScore+=pointPerQuestion;
				}else{
					avo.setIs_correct("N");
					avo.setScore(0);
				}
			}else{
				avo.setIs_correct("W");
				avo.setScore(0);
				hasSubjective=true;
			}
			answers.add(avo);
		}

		if(!answers.isEmpty()){
			eMapper.insertUserAnswers(answers);
		}

		int finalScore=(int)Math.round(rawTotalScore);

		ExamEnrollmentVO evo=new ExamEnrollmentVO();
		evo.setNo(enrollmentNo);
		evo.setEndtime(LocalDateTime.now());
		evo.setTotalscore(finalScore);
		evo.setStatus(hasSubjective?"WAITING":"COMPLETE");

		eMapper.updateEnrollmentFinish(evo);

		Map<String,Object> result=new HashMap<>();
		result.put("status",evo.getStatus());
		result.put("enrollmentNo",enrollmentNo);
		result.put("totalscore",finalScore);
		return result;
	}

	@Override
	public Map<String, Object> getExamResultData(int enrollmentNo) {
		Map<String,Object> map=eMapper.selectExamResultMaster(enrollmentNo);
		if(map==null){
			throw new IllegalArgumentException("존재하지 않는 응시기록입니다.");
		}
		List<Map<String,Object>> details=eMapper.selectExamResultDetails(enrollmentNo);
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
	public List<Map<String, Object>> getMyExamList(int memberId) {
		return eMapper.selectMyExamList(memberId);
	}

}
