package com.shunproduct.scheduleboard.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.shunproduct.scheduleboard.entity.Schedule;
import com.shunproduct.scheduleboard.entity.ScheduleDisplayParam;
import com.shunproduct.scheduleboard.repository.CompanyCalendarRepository;
import com.shunproduct.scheduleboard.repository.ScheduleRepository;
import com.shunproduct.scheduleboard.repository.SiteUserRepository;
import com.shunproduct.scheduleboard.service.PullDownContentService;
import com.shunproduct.scheduleboard.service.ScheduleSaveService;
import com.shunproduct.scheduleboard.service.UserDayScheduleListMapService;
import com.shunproduct.scheduleboard.service.ViewStyleService;
import com.shunproduct.scheduleboard.util.ScheduleDisplayParamDefaultConst;

import lombok.RequiredArgsConstructor;

@SessionAttributes("scheduleDisplayParam")
@RequiredArgsConstructor
@Controller
public class ScheduleController {
	
	private final SiteUserRepository siteUserRepository;
	private final ScheduleRepository scheduleRepository;
	private final CompanyCalendarRepository companyCalendarRepository;
	private final UserDayScheduleListMapService userDayScheduleListMapService;
	private final PullDownContentService pullDownContentService;
	private final ScheduleSaveService scheduleSaveService;
	private final ViewStyleService viewStyleService;
	
	@ModelAttribute("scheduleDisplayParam") // セッションに保存するオブジェクトの本体はメソッドに@ModelAttributeアノテーションを付けて作成する。
	public ScheduleDisplayParam scheduleDisplayParam() {
		return new ScheduleDisplayParam();
	}
	
	@GetMapping("/schedule-board")
	public String displaySchedule(Authentication loginUser, ScheduleDisplayParam scheduleDisplayParam,
			@RequestParam(required = false) String groupId, @RequestParam(required = false) String startDate, @RequestParam(required = false) String displayTerm,
			Model model, Principal principal)
			throws UnsupportedEncodingException {
		
		// スケジュール表示画面のパラメータ情報をsessionに保存
		if (Objects.isNull(groupId) || startDate == null || displayTerm == null) {

			if (siteUserRepository.findByUsername(principal.getName()).getGroupId() > 50) {
				groupId = ScheduleDisplayParamDefaultConst.GROUPID;
			} else {
				groupId = String.valueOf(siteUserRepository.findByUsername(principal.getName()).getGroupId());
			}
			startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			displayTerm = ScheduleDisplayParamDefaultConst.DISPLAYTERM;
		}
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
		// viewに渡す YYYY-MM-DD,param形式のデータになっており、","で区切って、平日or休日を判定
		model.addAttribute("displayDateList", dayOfWeekInfoList);

		// スケジュール画面に渡すスケジュールマップを取得
		Map<String, Map<String, List<Schedule>>> userDayScheduleListMap = userDayScheduleListMapService.prepareUserDayScheduleListMap(groupId, startDate, displayTerm);
		// viewに渡す
		model.addAttribute("userDayScheduleListMap", userDayScheduleListMap);
		
		// その他、viewに渡す情報
		model.addAttribute("loginId", principal.getName()); // パスワード変更画面遷移のため
		model.addAttribute("role", siteUserRepository.findByUsername(principal.getName()).getRole()); // 権限設定のありページ表示のため role==管理の場合、管理者権限用ページを表示する
		model.addAttribute("pullDownContentService", pullDownContentService); // 画面上部条件変更用プルダウン、
		model.addAttribute("scheduleCategoryMap", viewStyleService.prepareScheduleCategoryMap());
		model.addAttribute("scheduleStatusMap", viewStyleService.prepareScheduleStatusMap());

		return "schedule-board";

	}
	
	// 新規予定の追加
	@GetMapping("/add-schedule/{groupId}/{memberId}/{mainDate}")
	public String addSchedule(
			@PathVariable("groupId") String groupId, @PathVariable("memberId") String memberId, @PathVariable("mainDate") String mainDate,
			Model model, Principal principal) {

		// 新規入力フォームの初期値の設定
		Schedule schedule = new Schedule();
		
		schedule.setGroupId(Integer.parseInt(groupId));
		// 作業者
		schedule.setMemberId(memberId);
		schedule.setAddMemberId1st(memberId);
		// 作業日
		schedule.setMainDate(LocalDate.parse(mainDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		schedule.setAddDate1st(LocalDate.parse(mainDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		schedule.setAddDate2nd(LocalDate.parse(mainDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		schedule.setAddDate3rd(LocalDate.parse(mainDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		schedule.setAddDate4th(LocalDate.parse(mainDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		schedule.setAddDate5th(LocalDate.parse(mainDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

		model.addAttribute("schedule", schedule);
		model.addAttribute("pullDownContentService", pullDownContentService);

		return "schedule-register-form";
	}
	
	// 既存予定の編集
	@GetMapping("/add-schedule/{groupId}/{memberId}/{mainDate}/{scheduleId}")
	public String updateSchedule(
			@PathVariable("groupId") String groupId, @PathVariable("memberId") String memberId, @PathVariable("mainDate") String mainDate, @PathVariable("scheduleId") String scheduleId,
			Model model, Principal principal) {
		
		model.addAttribute("schedule", scheduleRepository.findById(Long.parseLong(scheduleId)).get());
		model.addAttribute("pullDownContentService", pullDownContentService);
		model.addAttribute("updateInfo", scheduleRepository.findUpdateInfoByScheduleId(Long.parseLong(scheduleId)));

		return "schedule-register-form";
	}
	
	// 既存予定のコピー
	@GetMapping("/add-schedule/{groupId}/{scheduleId}")
	public String copySchedule(
			@PathVariable("groupId") String groupId, @PathVariable("scheduleId") String scheduleId,
			Model model, Principal principal) {
		
		Schedule schedule = this.scheduleRepository.findById(Long.parseLong(scheduleId)).get();
		schedule.setScheduleId(null);
		schedule.setScheduleGroupId(null);
		model.addAttribute("schedule", schedule);
		model.addAttribute("pullDownContentService", pullDownContentService);

		return "schedule-register-form";
	}
	
	// 入力された予定の処理
	@PostMapping("/process-add-schedule")
	public String process(@Validated @ModelAttribute Schedule schedule, BindingResult result, ScheduleDisplayParam scheduleDisplayParam,
			Principal principal) throws Exception {
		
		// sessionに保存された設定情報を取得し、getパラメータを準備
		String param = 
				"?groupId=" + scheduleDisplayParam.getGroupId()
				+ "&startDate=" + scheduleDisplayParam.getStartDate()
				+ "&displayTerm=" + scheduleDisplayParam.getDisplayTerm();

		// 登録失敗時の処理
		if (result.hasErrors()) {
			System.out.println("登録失敗");

			return "redirect:/schedule-board" + param;
		}
		
		scheduleSaveService.addOrUpdate(schedule, principal);

		return "redirect:/schedule-board" + param;
	}
	
	// 削除処理
	@GetMapping("/delete-schedule/{scheduleId}")
	public String deleteSchedule(@PathVariable("scheduleId") String scheduleId, @ModelAttribute Schedule schedule, ScheduleDisplayParam scheduleDisplayParam,
			Principal principal) throws Exception {

		System.out.println("--削除処理-------------------------");
		System.out.println("確認　ID " + schedule.getScheduleId());
		System.out.println("確認　グループID " + scheduleRepository.findById(Long.parseLong(scheduleId)).get().getScheduleGroupId());
		System.out.println("----------------------------------");

		// グループIDがない場合
		if (StringUtils.isEmpty(scheduleRepository.findById(Long.parseLong(scheduleId)).get().getScheduleGroupId())) {
			scheduleRepository.deleteById(Long.parseLong(scheduleId));
			// グループIDがある場合、同じグループIDを持つscheduleを削除
		} else {
			scheduleRepository.deleteByScheduleGroupId(
					scheduleRepository.findById(Long.parseLong(scheduleId)).get().getScheduleGroupId());
		}

		// sessionに保存された設定情報を取得し、getパラメータを準備
		String param = "?groupId=" + scheduleDisplayParam.getGroupId() + "&startDate="
				+ scheduleDisplayParam.getStartDate() + "&displayTerm=" + scheduleDisplayParam.getDisplayTerm();

		return "redirect:/schedule-board" + param;
	}

}
