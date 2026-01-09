package com.pro.pair.member.model.dto;

import lombok.Data;

@Data
public class RoleDTO{
	
	private String memberId;
	private int authorityCode;
	
	private AuthorityDTO authority;
}
