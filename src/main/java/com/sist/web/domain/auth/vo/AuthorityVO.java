package com.sist.web.domain.auth.vo;

import lombok.Data;

/*
 *  NO        NOT NULL NUMBER       
	MEMBER_ID NOT NULL NUMBER       
	AUTHORITY NOT NULL VARCHAR2(20)
 */
@Data
public class AuthorityVO {
	private int no;
	private String member_id;
	private String authority; // 권한 => ROLE_USER, ROLE_ADMIN, ROLE_INSTRUCTOR
}
