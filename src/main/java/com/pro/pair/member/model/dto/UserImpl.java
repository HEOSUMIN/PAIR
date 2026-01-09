package com.pro.pair.member.model.dto;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


import lombok.Getter;
import lombok.ToString;

/* 메소드 통해 값을 꺼내거나 출력해서 확인할 용도로 Getter, ToString 선언 */
@Getter
@ToString
public class UserImpl extends User {
	
	private String memberId;
	private String memberPwd;
	private String email;
	private String name;
	private char status;
	private String address;
	private String phone;

	private Date InsDt;				//가입일자
	private Date UpDt;				//수정일자
	private Date lastLoginDt;	//최근로그인일시

	private List<RoleDTO> roleList;
	
	
	public UserImpl(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		
		super(username, password, authorities);
	}
	
	/**
	 * 상세 데이터 호출용 메소드
	 */
	public void setDetails(MemberDTO member) {
		this.memberId = member.getMemberId();
		this.memberPwd = member.getMemberPwd();
		this.name = member.getName();
		this.phone = member.getPhone();
		this.email = member.getEmail();
		this.address = member.getAddress();
		this.status = member.getStatus();
		this.InsDt = member.getInsDt();
		this.UpDt = member.getUpDt();
		this.lastLoginDt = member.getLastLoginDt();
	}


	
}
