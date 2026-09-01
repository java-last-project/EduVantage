package com.sist.web.domain.exam.vo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

//NO         NOT NULL NUMBER       
//MEMBER_ID  NOT NULL NUMBER       
//EXAM_NO             NUMBER       
//REGDATE    NOT NULL DATE         
//STARTTIME  NOT NULL TIMESTAMP(6) 
//ENDTIME             TIMESTAMP(6) 
//TOTALSCORE          NUMBER(3)   
@Data
public class ExamEnrollmentVO {
	private int no,member_id,exam_no,totalscore;
	private LocalDateTime starttime,endtime,regdate;
	private String dbday;
	private ExamOptionVO ovo;
	private ExamQuestionVO qvo;
	private ScheduledExamVO svo;
	private List<ExamUserAnswerVO> answerList;
}
