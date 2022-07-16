package com.shunproduct.scheduleboard.service;

import java.security.Principal;
import java.time.LocalDate;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.shunproduct.scheduleboard.repository.SiteUserRepository;
import com.shunproduct.scheduleboard.util.ScheduleDisplayParamDefaultConst;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScheduleDisplayParamService extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final SiteUserRepository siteUserRepository;

	public String findScheduleDisplayParam(String groupId, String startDate, String displayTerm, Principal principal) {

		// groupIdの割り当て
		if (siteUserRepository.findByUsername(principal.getName()).getGroupId() > 50) {
			groupId = ScheduleDisplayParamDefaultConst.GROUPID;
		} else {
			groupId = String.valueOf(siteUserRepository.findByUsername(principal.getName()).getGroupId());
		}

		String param = "?groupId=" + groupId + "&startDate=" + LocalDate.now() + "&displayTerm="
				+ ScheduleDisplayParamDefaultConst.DISPLAYTERM;

		return param;
	}

	private enum DEVICE_TYPE {
		SMART_PHONE, TABLET, PC
	};

	public String findGoodScheduleDisplayTerm(HttpServletRequest request, HttpServletResponse response) {

		switch (getDeviceType(request.getHeader("user-agent"))) {
		case SMART_PHONE:
			return ScheduleDisplayParamDefaultConst.DISPLAYTERM_SP;
		case TABLET:
			return ScheduleDisplayParamDefaultConst.DISPLAYTERM;
		case PC:
			return ScheduleDisplayParamDefaultConst.DISPLAYTERM;
		}
		return ScheduleDisplayParamDefaultConst.DISPLAYTERM;
	}

	private DEVICE_TYPE getDeviceType(String userAgent) {
		//
		// スマートフォン系
		//

		// iPhone / iPod
		if (userAgent.indexOf("iPhone") != -1) {
			return DEVICE_TYPE.SMART_PHONE;
		}

		// Android
		if (userAgent.indexOf("Android") != -1 && userAgent.indexOf("Mobile") != -1) {
			return DEVICE_TYPE.SMART_PHONE;
		}

		//
		// タブレット系
		//

		// iPad
		if (userAgent.indexOf("iPad") != -1) {
			return DEVICE_TYPE.TABLET;
		}

		// Android
		if (userAgent.indexOf("Android") != -1) {
			return DEVICE_TYPE.TABLET;
		}

		// その他なので、とりあえずPC扱い
		return DEVICE_TYPE.PC;
	}

}
