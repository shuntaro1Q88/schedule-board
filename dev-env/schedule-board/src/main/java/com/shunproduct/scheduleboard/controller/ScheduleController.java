package com.shunproduct.scheduleboard.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.shunproduct.scheduleboard.entity.ScheduleDisplayParam;
import com.shunproduct.scheduleboard.entity.SiteUser;
import com.shunproduct.scheduleboard.repository.CompanyCalendarRepository;
import com.shunproduct.scheduleboard.repository.SiteUserRepository;

import lombok.RequiredArgsConstructor;

@SessionAttributes("displayParam")
@RequiredArgsConstructor
@Controller
public class ScheduleController {
	
	private final SiteUserRepository siteUserRepository;
	private final CompanyCalendarRepository companyCalendarRepository;
	
	@GetMapping("/schedule")
	public String displaySchedule(Authentication loginUser, ScheduleDisplayParam scheduleDisplayParam,
			@RequestParam(required = false) int groupId, @RequestParam(required = false) String startDate, @RequestParam(required = false) int displayTerm,
			Model model, Principal principal)
			throws UnsupportedEncodingException {
		
		SiteUser user = siteUserRepository.findByUsername(principal.getName());
		
		groupId = user.getGroupId();
		startDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
		displayTerm = 31;

		// スケジュール表示画面のパラメータ情報をsessionに保存
		scheduleDisplayParam.setId(groupId);
		scheduleDisplayParam.setStartDate(startDate);
		scheduleDisplayParam.setDisplayTerm(displayTerm);
		// viewに渡す
		model.addAttribute("scheduleDisplayParam", scheduleDisplayParam);

		// 表示する期間の設定
		LocalDate ldStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate ldEndDate = ldStartDate.plusDays(displayTerm);

		// スケジュール画面1行目、日付用データ
		List<String> dayOfWeekInfoList = companyCalendarRepository.findByMainDateBetween(ldStartDate, ldEndDate.minusDays(1));
		dayOfWeekInfoList.add(0,"YYYY-MM-DD,param");
		// viewに渡す
		model.addAttribute("displayDateList", dayOfWeekInfoList);

		// スケジュール画面に渡すスケジュールマップを取得
		Map<String, Map<String, List<Schedule>>> userDayScheduleListMap
		= prepareUserDayScheduleListMap.prepareUserDayScheduleListMap(Integer.parseInt(groupCategoryId), startDate, displayTerm);
		// viewに渡す
		model.addAttribute("userDayScheduleListMap", userDayScheduleListMap);
		
		// viewに渡す
		model.addAttribute("loginId", loginId);
		model.addAttribute("role", userRepository.findByUsername(loginId).getRole());
		model.addAttribute("viewContentService", viewContentService);
		model.addAttribute("workCategoryBgColorMap", viewStyleService.getWorkCategoryBgColorMap());

		return "schedule/schedule";

	}

}
