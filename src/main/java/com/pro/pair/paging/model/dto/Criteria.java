package com.pro.pair.paging.model.dto;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;

@Data
public class Criteria {
	
	private int currentPageNo;		//현제페이지 
	private int recordsPerPage;		//페이지당 출력할 데이터 개수 
	private String condition;
	private String keyword;
	
	//한페이지당 열개씩
	public Criteria() {
		this(1, 10);
	}
	
	public Criteria(int currentPageNo, int recordsPerPage) {
		this.currentPageNo = currentPageNo;
		this.recordsPerPage = recordsPerPage;
	}
	
	public String getListLink() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("currentPageNo", currentPageNo)
				.queryParam("recordsPerPage", recordsPerPage)
				.queryParam("category", this.getCondition())
				.queryParam("keyword", this.keyword);
		return builder.toUriString();
	}
}
