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
	private String memberId;		//회원 id
	private String memberPwd;		//회원비밀번호 
	private String name;			//이름 
	private String phone;			//전화번호 
	private String email;			//이메일 
	private String address;			//주소 
	private String nickname;		//닉네임
	private char status;			//계정탈퇴여부상태
	
	private Date InsDt;				//가입일자
	private Date UpDt;				//수정일자
	private Date latestLoginDate;	//최근로그인일시

	
	//private List<RoleDTO> roleList;
	
	/*
	 * User로부터 상속 받은 생성자 
	 */
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
		this.nickname = member.getNickname();
		this.status = member.getStatus();
		this.InsDt = member.getInsDt();
		this.UpDt = member.getUpDt();
		this.latestLoginDate = member.getLatestLoginDate();
	}


	
}
