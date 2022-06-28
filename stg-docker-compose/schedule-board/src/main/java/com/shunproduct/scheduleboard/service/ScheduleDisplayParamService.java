package com.shunproduct.scheduleboard.service;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.shunproduct.scheduleboard.repository.SiteUserRepository;
import com.shunproduct.scheduleboard.util.ScheduleDisplayParamDefaultConst;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScheduleDisplayParamService {
	
	private final SiteUserRepository siteUserRepository;
	
	public String findScheduleDisplayParam(String groupId,String startDate,String displayTerm,Principal principal) {
		
		// groupIdの割り当て
		if (siteUserRepository.findByUsername(principal.getName()).getGroupId() > 50) {
			groupId = ScheduleDisplayParamDefaultConst.GROUPID;
		} else {
			groupId = String.valueOf(siteUserRepository.findByUsername(principal.getName()).getGroupId());
		}
		
		String param = "?groupId=" + groupId + "&startDate=" + LocalDate.now() + "&displayTerm=" + ScheduleDisplayParamDefaultConst.DISPLAYTERM;
		
		return param;
	}

}
