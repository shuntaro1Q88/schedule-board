package com.shunproduct.scheduleboard.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.shunproduct.scheduleboard.repository.SiteUserRepository;
import com.shunproduct.scheduleboard.service.ScheduleDisplayParamService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class LoginController {
	
	private final SiteUserRepository siteUserRepository;
	private final ScheduleDisplayParamService scheduleDisplayParamService;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	// ログイン後、schedule-boardページへの遷移
	@GetMapping("/")
	public String displayScheduleDefaultLogin(Principal principal, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 表示期間はデバイスに応じて変更
		String param = 
				"?groupId=" + siteUserRepository.findByUsername(principal.getName()).getGroupId()
				+ "&startDate=" + LocalDate.now()
				+ "&displayTerm=" + scheduleDisplayParamService.findGoodScheduleDisplayTerm(request, response);
		
		return "redirect:/schedule-board" + param;

	}

}
