package com.pro.pair.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
@SessionAttributes("loginMember")
public class AdminController {
	
	@GetMapping("/dashboard")
	public String getDashBoard(Model model) {
		
		return "admin/dashboard";
	}
}
