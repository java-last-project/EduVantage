package com.sist.web.domain.mypage.vo;

import lombok.Data;
import java.util.*;

/*
MEMBER_ID     NOT NULL NUMBER         
USERNAME      NOT NULL VARCHAR2(50)   
PASSWORD      NOT NULL VARCHAR2(100)  
NAME          NOT NULL VARCHAR2(100)  
SEX           NOT NULL CHAR(1)        
REGDATE                DATE           
BIRTHDATE     NOT NULL DATE           
PHONE         NOT NULL VARCHAR2(15)   
POST          NOT NULL VARCHAR2(8)    
ADDR1         NOT NULL VARCHAR2(500)  
ADDR2                  VARCHAR2(1000) 
EMAIL                  VARCHAR2(500)  
PROFILE_DESC           CLOB           
PROFILE_IMAGE          VARCHAR2(500)  
ENABLED                NUMBER
 */
@Data
public class MyMemberVO {
	private int member_id,enabled;
	private String username,password,name,sex,phone,post,addr1,addr2,email,
		profile_desc,profile_image,dbRday,dbBday;
	private Date regdate, birthdate;
}
