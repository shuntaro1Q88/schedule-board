package com.shunproduct.scheduleboard.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.shunproduct.scheduleboard.repository.SiteUserRepository;
import com.shunproduct.scheduleboard.util.ScheduleDisplayParamDefaultConst;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class LoginController {
	
	private final SiteUserRepository siteUserRepository;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/")
	public String displayScheduleDefaultLogin(Principal principal)
			throws UnsupportedEncodingException {
		
		String param = 
				"?groupId=" + siteUserRepository.findByUsername(principal.getName()).getGroupId()
				+ "&startDate=" + LocalDate.now()
				+ "&displayTerm=" + ScheduleDisplayParamDefaultConst.DISPLAYTERM;
		
		return "redirect:/schedule-board" + param;

	}

}
