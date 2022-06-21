package com.shunproduct.scheduleboard.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.shunproduct.scheduleboard.entity.Schedule;
import com.shunproduct.scheduleboard.entity.ScheduleDisplayParam;
import com.shunproduct.scheduleboard.entity.SiteUser;
import com.shunproduct.scheduleboard.repository.CompanyCalendarRepository;
import com.shunproduct.scheduleboard.repository.SiteUserRepository;
import com.shunproduct.scheduleboard.service.PullDownContentService;
import com.shunproduct.scheduleboard.service.UserDayScheduleListMapService;

import lombok.RequiredArgsConstructor;

@SessionAttributes("displayParam")
@RequiredArgsConstructor
@Controller
public class ScheduleController {
	
	private final SiteUserRepository siteUserRepository;
	private final CompanyCalendarRepository companyCalendarRepository;
	private final UserDayScheduleListMapService userDayScheduleListMapService;
	private final PullDownContentService pullDownContentService;
	
	
	@GetMapping("/schedule-board")
	public String displaySchedule(Authentication loginUser, ScheduleDisplayParam scheduleDisplayParam,
			@RequestParam(required = false) String groupId, @RequestParam(required = false) String startDate, @RequestParam(required = false) String displayTerm,
			Model model, Principal principal)
			throws UnsupportedEncodingException {
		
		SiteUser user = siteUserRepository.findByUsername(principal.getName());
		
		groupId = String.valueOf(user.getGroupId());
		startDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
		displayTerm = "28";

		// スケジュール表示画面のパラメータ情報をsessionに保存
		scheduleDisplayParam.setGroupId(Integer.parseInt(groupId));
		scheduleDisplayParam.setStartDate(startDate);
		scheduleDisplayParam.setDisplayTerm(Integer.parseInt(displayTerm));
		// viewに渡す
		model.addAttribute("scheduleDisplayParam", scheduleDisplayParam);

		// 表示する期間の設定
		LocalDate ldStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate ldEndDate = ldStartDate.plusDays(Integer.parseInt(displayTerm));

		// スケジュール画面1行目、日付用データ
		List<String> dayOfWeekInfoList = companyCalendarRepository.findByMainDateBetween(ldStartDate, ldEndDate.minusDays(1));
		dayOfWeekInfoList.add(0,"YYYY-MM-DD,param");
		// viewに渡す
		model.addAttribute("displayDateList", dayOfWeekInfoList);

		// スケジュール画面に渡すスケジュールマップを取得
		Map<String, Map<String, List<Schedule>>> userDayScheduleListMap = userDayScheduleListMapService.prepareUserDayScheduleListMap(groupId, startDate, displayTerm);
		// viewに渡す
		model.addAttribute("userDayScheduleListMap", userDayScheduleListMap);
		
		// viewに渡す
		model.addAttribute("loginId", principal.getName()); // パスワード変更画面遷移のため
		model.addAttribute("role", siteUserRepository.findByUsername(principal.getName()).getRole()); // 権限設定のありページ表示のため role==管理の場合、管理者権限用ページを表示する
		model.addAttribute("pullDownContentService", pullDownContentService); // 画面上部条件変更用プルダウン、
//		model.addAttribute("workCategoryBgColorMap", viewStyleService.getWorkCategoryBgColorMap());

		return "schedule-board";

	}
	
	// 新規予定の追加
	@GetMapping("/add-schedule/{groupId}/{memberId}/{mainDate}")
	public String addNewSchedule(@PathVariable("groupId") String groupId, @PathVariable("memberId") String memberId, @PathVariable("mainDate") String mainDate, Model model, Principal principal) {

		// 新規入力フォームの初期値の設定
		Schedule schedule = new Schedule();
		
		schedule.setGroupId(Integer.parseInt(groupId));
		schedule.setMemberId(memberId);
		schedule.setAddMemberId1st(memberId);
		schedule.setMainDate(LocalDate.parse(mainDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		schedule.setAddDate1st(LocalDate.parse(mainDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		schedule.setStatusId(0);

		model.addAttribute("schedule", schedule);
		model.addAttribute("pullDownContentService", pullDownContentService);



		return "schedule-register-form";
	}

}
