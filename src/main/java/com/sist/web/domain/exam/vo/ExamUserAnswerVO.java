package com.sist.web.domain.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;
//NO            NOT NULL NUMBER         
//ENROLLMENT_NO NOT NULL NUMBER         
//QUESTION_NO   NOT NULL NUMBER         
//USER_ANSWER            VARCHAR2(2000) 
//IS_CORRECT             CHAR(1)      
@Data
public class ExamUserAnswerVO {
	private Integer no,enrollment_no,question_no;
	private String user_answer,is_correct;
	private ExamQuestionVO qvo;

	private Integer score,grader_id;
	private LocalDateTime graded_at;
}
