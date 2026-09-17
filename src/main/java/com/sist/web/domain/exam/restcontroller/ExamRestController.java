package com.sist.web.domain.exam.restcontroller;

import com.sist.web.domain.exam.service.ExamService;
import com.sist.web.domain.exam.service.RecommendCoursesService;
import com.sist.web.domain.exam.vo.ExamEnrollmentVO;
import com.sist.web.domain.exam.vo.ExamQuestionVO;
import com.sist.web.domain.exam.vo.RecommendCourseVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class ExamRestController {
    private final ExamService eService;
    private final RecommendCoursesService rService;

    @PostMapping("/exam/detail_vue")
    public ResponseEntity<Map<String,Object>> exam_detail_vue(@RequestBody Map<String,Object> params, HttpSession session){
        Map<String,Object> map=new HashMap<>();
        try{
            int count=params.get("count")!=null?Integer.parseInt(String.valueOf(params.get("count"))):20;
            Integer theme=0;
            if(params.containsKey("theme")){
                theme=(Integer)params.get("theme");
                map.put("theme", theme);
            }
            Integer examNo=(Integer)params.get("examNo");
            Object sessionMid=session.getAttribute("member_id");
            if(sessionMid==null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            int mid=Integer.parseInt(String.valueOf(sessionMid));
			boolean ai=Boolean.parseBoolean(String.valueOf(params.getOrDefault("ai",false)));
			if(ai){
				Object rawEnrollmentNo=params.get("enrollmentNo");
				if(rawEnrollmentNo==null){
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"AI 시험 응시기록 번호가 없습니다.");
				}
				int enrollmentNo;
				try{
					enrollmentNo=Integer.parseInt(String.valueOf(rawEnrollmentNo));
				}catch(NumberFormatException ex){
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"잘못된 AI 시험 응시기록 번호입니다.");
				}
				map.putAll(eService.getAiExamDetailData(mid,enrollmentNo));
				Object examName=params.get("examName");
				map.put("title",examName!=null && !String.valueOf(examName).isBlank()
						?String.valueOf(examName):"AI 맞춤시험");
				return ResponseEntity.ok(map);
			}

            ExamEnrollmentVO vo=eService.getOrCreateEnrollment(mid,examNo,theme,count);
            map.put("enrollmentNo",vo.getNo());
            map.put("startTime",vo.getStarttime());

            String title=null;
            if(examNo!=null && examNo>0){
                title=eService.getExamTitle(examNo);
            }
            boolean practiceExam=vo.getExam_no()==null && vo.getTheme()!=null;
            List<ExamQuestionVO> list=practiceExam
                    ?eService.getPracticeExamQuestions(mid,vo.getNo())
                    :eService.examDetailData(examNo,theme,count);
            map.put("enrollmentNo",vo.getNo());
            map.put("title",title);
            map.put("count",practiceExam?list.size():count);
            map.put("list",list);
			map.put("timeLimitMinutes",eService.getExamLimitMinutes(examNo));
		}catch(ResponseStatusException ex){
			throw ex;
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(map);
    }

    @PostMapping("/exam/submit_vue")
    public ResponseEntity<?> examSubmit(@RequestBody Map<String, Object> params, HttpSession session){
        try{
            Object sessionMid=session.getAttribute("member_id");
            if(sessionMid==null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
			int mid=Integer.parseInt(String.valueOf(sessionMid));
            Map<String,Object> map=eService.submitExam(mid,params);
            return ResponseEntity.ok(map);
		}catch(ResponseStatusException ex){
			throw ex;
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/exam/result_vue")
    public ResponseEntity<?> exam_result_vue(@RequestParam("no") int enrollmentNo, HttpSession session) {
        try {
            Object sessionMid=session.getAttribute("member_id");
            if (sessionMid==null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
			int mid=Integer.parseInt(String.valueOf(sessionMid));
            Map<String, Object> resultData=eService.getExamResultData(mid,enrollmentNo);
            return ResponseEntity.ok(resultData);
		} catch(ResponseStatusException ex) {
			throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

	@GetMapping("/exam/scheduled_result_vue")
	public ResponseEntity<?> scheduledExamResult(@RequestParam("examNo") int examNo,HttpSession session){
		Object sessionMid=session.getAttribute("member_id");
		if(sessionMid==null){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		int memberId=Integer.parseInt(String.valueOf(sessionMid));
		Integer enrollmentNo=eService.getScheduledExamResult(memberId,examNo);
		if(enrollmentNo==null){
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(Map.of("enrollmentNo",enrollmentNo));
	}

    @GetMapping("/exam/my_result_list_vue")
    public ResponseEntity<?> myResultList(HttpSession session) {
        Object sessionMid = session.getAttribute("member_id");
        if (sessionMid == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        int memberId = Integer.parseInt(String.valueOf(sessionMid));
        return ResponseEntity.ok(eService.getMyExamList(memberId));
    }

    @PostMapping("/exam/result/recommend")
    public ResponseEntity<List<RecommendCourseVO>> recommendCourses(@RequestBody Map<String,List<Integer>> map){
        try{
            List<Integer> questionNos=map.get("question_nos");
            List<RecommendCourseVO> list=rService.getRecommentCourses(questionNos);
            return ResponseEntity.ok(list);
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

	@PostMapping("/exam/result/ai-recommend")
	public ResponseEntity<List<RecommendCourseVO>> recommendAiCourses(
			@RequestBody Map<String,Object> params,HttpSession session){
		Object sessionMid=session.getAttribute("member_id");
		if(sessionMid==null){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		Object rawEnrollmentNo=params.get("enrollmentNo");
		if(rawEnrollmentNo==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"응시기록 번호가 없습니다.");
		}

		try{
			int memberId=Integer.parseInt(String.valueOf(sessionMid));
			int enrollmentNo=Integer.parseInt(String.valueOf(rawEnrollmentNo));
			return ResponseEntity.ok(rService.getAiRecommendedCourses(memberId,enrollmentNo));
		}catch(NumberFormatException ex){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"잘못된 응시기록 번호입니다.");
		}
	}
}
