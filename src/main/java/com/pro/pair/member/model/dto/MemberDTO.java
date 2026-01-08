package com.pro.pair.member.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MemberDTO {
	
	private String memberId;
	private String memberPwd;
	private String name;
	private String phone;
	private String email;
	private String address;
	private String nickname;
	private char status;
	
	private String role;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss SSS")
	private Date InsDt;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss SSS")
	private Date UpDt;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss SSS")
	private Date latestLoginDate;
	
}
