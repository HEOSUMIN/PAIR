package com.pro.pair.member.model.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MemberDTO {
	
	private String memberId;	//사용자아이디 
	private String memberPwd;	//사용자비밀번호  
	private String email;		//이메일 
	private String name;		//이름 
	private char status;		//계정상태(정상:Y,탈퇴:N) 
	private String address;		//주소  
	private String phone;		//연락처 
	
	private List<RoleDTO> roleList;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss SSS")
	private Date InsDt;			//가입일자
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss SSS")
	private Date UpDt;			//수정일자
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss SSS")
	private Date lastLoginDt;	//마지막로그인일시
	
}
