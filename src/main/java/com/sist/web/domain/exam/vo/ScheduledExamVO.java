package com.sist.web.domain.exam.vo;

import java.time.LocalDateTime;

import lombok.Data;

//NO         NOT NULL NUMBER         
//TITLE      NOT NULL VARCHAR2(1000) 
//OPEN_DATE  NOT NULL TIMESTAMP(6)   
//CLOSE_DATE NOT NULL TIMESTAMP(6) 
@Data
public class ScheduledExamVO {
	private int no;
	private String title;
	private LocalDateTime open_date,close_date;
	//시험 알림 구독 여부 판단용
	private boolean subscribed;

	public String getStatus() {
		if (open_date == null || close_date == null) {
			return "CLOSED";
		}
		LocalDateTime now = LocalDateTime.now();
		if (now.isBefore(open_date)) {
			return "READY";
		} else if (now.isAfter(close_date)) {
			return "CLOSED";
		} else {
			return "OPEN";
		}
	}
}
