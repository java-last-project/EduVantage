package com.sist.web.domain.exam.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.sist.web.domain.exam.mapper.InstructorScheduledExamMapper;
import com.sist.web.domain.exam.vo.ExamQuestionVO;
import com.sist.web.domain.exam.vo.ScheduledExamMapVO;
import com.sist.web.domain.exam.vo.ScheduledExamVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstructorScheduledExamServiceImpl implements InstructorScheduledExamService {
	private final InstructorScheduledExamMapper scheduledExamMapper;

	@Override
	@Transactional(readOnly=true)
	public List<ScheduledExamVO> getScheduledExamList(int instructorId){
		return scheduledExamMapper.selectInstructorScheduledExamList(instructorId);
	}

	@Override
	@Transactional(readOnly=true)
	public ScheduledExamVO getScheduledExamDetail(int instructorId,int examNo){
		Map<String,Object> map=examParams(instructorId,examNo);
		ScheduledExamVO vo=scheduledExamMapper.selectInstructorScheduledExamDetail(map);
		if(vo==null){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 정기시험입니다.");
		}
		vo.setQuestion_nos(scheduledExamMapper.selectScheduledQuestionNos(map));
		return vo;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ExamQuestionVO> getQuestionBank(Integer theme){
		return scheduledExamMapper.selectQuestionBank(theme);
	}

	@Override
	@Transactional
	public int insertScheduledExam(int instructorId,ScheduledExamVO vo){
		validateExam(vo);
		List<Integer> questionNos=validateQuestions(vo.getQuestion_nos());
		vo.setInstructor_id(instructorId);
		vo.setQuestion_nos(questionNos);
		scheduledExamMapper.insertScheduledExam(vo);
		insertExamMaps(vo.getNo(),questionNos);
		return vo.getNo();
	}

	@Override
	@Transactional
	public void updateScheduledExam(int instructorId,int examNo,ScheduledExamVO vo){
		ScheduledExamVO saved=getScheduledExamDetail(instructorId,examNo);
		LocalDateTime now=LocalDateTime.now();
		if(now.isAfter(saved.getClose_date())){
			throw new ResponseStatusException(HttpStatus.CONFLICT,"종료된 시험은 수정할 수 없습니다.");
		}
		vo.setNo(examNo);
		vo.setInstructor_id(instructorId);
		if(!now.isBefore(saved.getOpen_date())){
			if(vo.getClose_date()==null || !vo.getClose_date().isAfter(now)){
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"종료 일시는 현재보다 이후여야 합니다.");
			}
			if(scheduledExamMapper.updateStartedExamCloseDate(vo)==0){
				throw new ResponseStatusException(HttpStatus.CONFLICT,"시험 종료 일시를 수정하지 못했습니다.");
			}
			return;
		}

		validateExam(vo);
		List<Integer> questionNos=validateQuestions(vo.getQuestion_nos());
		if(scheduledExamMapper.updateScheduledExam(vo)==0){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 정기시험입니다.");
		}
		scheduledExamMapper.deleteScheduledExamMaps(examNo);
		insertExamMaps(examNo,questionNos);
	}

	@Override
	@Transactional
	public void deleteScheduledExam(int instructorId,int examNo){
		ScheduledExamVO saved=getScheduledExamDetail(instructorId,examNo);
		if(!LocalDateTime.now().isBefore(saved.getOpen_date())){
			throw new ResponseStatusException(HttpStatus.CONFLICT,"시작된 시험은 삭제할 수 없습니다.");
		}
		if(scheduledExamMapper.countScheduledExamEnrollments(examNo)>0){
			throw new ResponseStatusException(HttpStatus.CONFLICT,"응시기록이 있는 시험은 삭제할 수 없습니다.");
		}
		scheduledExamMapper.deleteScheduledExamSubscriptions(examNo);
		scheduledExamMapper.deleteScheduledExamMaps(examNo);
		if(scheduledExamMapper.deleteScheduledExam(examParams(instructorId,examNo))==0){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 정기시험입니다.");
		}
	}

	private void validateExam(ScheduledExamVO vo){
		if(vo.getTitle()==null || vo.getTitle().isBlank()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"시험명을 입력해주세요.");
		}
		vo.setTitle(vo.getTitle().trim());
		if(vo.getTitle().length()>1000){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"시험명은 1000자 이하로 입력해주세요.");
		}
		if(vo.getOpen_date()==null || vo.getClose_date()==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"응시 시작일과 종료일을 입력해주세요.");
		}
		if(!vo.getClose_date().isAfter(vo.getOpen_date())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"종료 일시는 시작 일시보다 이후여야 합니다.");
		}
		if(!vo.getOpen_date().isAfter(LocalDateTime.now())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"시작 일시는 현재보다 이후여야 합니다.");
		}
		if(vo.getTime_limit()==null || vo.getTime_limit()<10 || vo.getTime_limit()>300){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"제한시간은 10분부터 300분까지 설정할 수 있습니다.");
		}
	}

	private List<Integer> validateQuestions(List<Integer> questionNos){
		if(questionNos==null || questionNos.isEmpty()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"시험 문제를 선택해주세요.");
		}
		Set<Integer> uniqueNos=new LinkedHashSet<>(questionNos);
		List<Integer> list=new ArrayList<>(uniqueNos);
		List<ExamQuestionVO> questions=scheduledExamMapper.selectQuestionsByNos(list);
		if(questions.size()!=list.size()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"존재하지 않는 문제가 포함되어 있습니다.");
		}
		int totalScore=questions.stream().mapToInt(ExamQuestionVO::getScore).sum();
		if(totalScore!=100){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"선택한 문제의 배점 합계는 100점이어야 합니다.");
		}
		return list;
	}

	private void insertExamMaps(int examNo,List<Integer> questionNos){
		List<ScheduledExamMapVO> list=new ArrayList<>();
		for(int questionNo:questionNos){
			ScheduledExamMapVO vo=new ScheduledExamMapVO();
			vo.setExam_no(examNo);
			vo.setQuestion_no(questionNo);
			list.add(vo);
		}
		scheduledExamMapper.insertScheduledExamMaps(list);
	}

	private Map<String,Object> examParams(int instructorId,int examNo){
		return Map.of("instructorId",instructorId,"examNo",examNo);
	}
}
