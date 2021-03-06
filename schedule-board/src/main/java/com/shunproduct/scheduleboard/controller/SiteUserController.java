package com.shunproduct.scheduleboard.controller;

import java.security.Principal;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.shunproduct.scheduleboard.entity.ScheduleDisplayParam;
import com.shunproduct.scheduleboard.entity.SiteUser;
import com.shunproduct.scheduleboard.repository.SiteUserRepository;
import com.shunproduct.scheduleboard.service.PullDownContentService;
import com.shunproduct.scheduleboard.util.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class SiteUserController {
	
	private final PullDownContentService pullDownContentService;
	private final PasswordEncoder passwordEncoder;
	private final SiteUserRepository siteUserRepository;
	
	@ModelAttribute("scheduleDisplayParam") // セッションに保存するオブジェクトの本体はメソッドに@ModelAttributeアノテーションを付けて作成する。
	public ScheduleDisplayParam scheduleDisplayParam() {
		return new ScheduleDisplayParam();
	}
	
	@GetMapping("/user-register")
	public String siteUserRegister(@ModelAttribute("siteUser") SiteUser siteUser, Model model) {

		// グループ名のプルダウンの内容を保存
		model.addAttribute("pullDownContentService", pullDownContentService);

		return "user-register-form";
	}
	
	@PostMapping("/user-register")
	public String register(@Validated @ModelAttribute("siteUser") SiteUser siteUser, BindingResult result, Model model) {

		// 失敗の場合
		if (result.hasErrors()) {

			// グループ名のプルダウンの内容を保存
			model.addAttribute("pullDownContentService", pullDownContentService);

			return "user-register-form";
		}

		/*
		 * 新規登録時のRoleの設定 基本はUSERにしておく
		 */
		// ハスワードのハッシュ化
		siteUser.setPassword(passwordEncoder.encode(siteUser.getPassword()));
		// 権限の設定
		siteUser.setRole(Role.ADMIN.getRoleName());

		siteUserRepository.save(siteUser);

		return "redirect:/login?register";
	}
	
	@GetMapping("/admin/user-list")
	public String siteUserList(@ModelAttribute("siteUser") SiteUser siteUser, Model model, ScheduleDisplayParam scheduleDisplayParam) {

		// 表示データを抽出するためのプルダウン
		model.addAttribute("pullDownContentService", pullDownContentService);
		// 表示データ
		model.addAttribute("siteUsers", siteUserRepository.findAll());

		return "user-list";
	}
	
	@PostMapping("/admin/user-list")
	private String filteredSiteUserList(@ModelAttribute("siteUser") SiteUser siteUser, Model model) {
		
		// 選択条件に一致するユーザーを保存
		// 表示データ
		if(siteUser.getGroupId()==0) {
			model.addAttribute("siteUsers", siteUserRepository.findAllByFamilyNameContaining(siteUser.getFamilyName()));
		}else {
			model.addAttribute("siteUsers", siteUserRepository.findAllByGroupIdAndFamilyNameContaining(siteUser.getGroupId(),siteUser.getFamilyName()));
		}

		// 表示データを抽出するためのプルダウン
		model.addAttribute("pullDownContentService", pullDownContentService);
		// グループの初期値を保存
		model.addAttribute("selectedGroup", siteUser.getGroupId());

		return "user-list";
	}
	
	@GetMapping("/edit-user/{id}")
	public String editEmployee(@PathVariable Long id, Model model) {

		model.addAttribute("siteUser", siteUserRepository.findById(id));

		// 部署のプルダウンの内容を保存
		// グループ名のプルダウンの内容を保存
		model.addAttribute("pullDownContentService", pullDownContentService);

		// グループの初期値を保存
		model.addAttribute("selectedGroup", siteUserRepository.findById(id).get().getGroupId());

		// 権限のプルダウンの内容を保存
		model.addAttribute("roles", Role.values());
		model.addAttribute("selectedRole", siteUserRepository.findById(id).get().getRole());
		
		return "user-edit-form";
	}
	
	@PostMapping("/update-user")
	public String update(@ModelAttribute SiteUser siteUser, BindingResult result, Model model) {

		siteUserRepository.save(siteUser);
		
		return "redirect:/admin/user-list";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteSiteuser(@PathVariable Long id) {
		
		siteUserRepository.deleteById(id);
		
		return "redirect:/admin/user-list";
	}
	
	@GetMapping("/edit-user-password/{loginId}")
	public String editSiteUerPassword(@ModelAttribute("user") SiteUser user, @PathVariable String loginId, Model model, Principal principal) {

		model.addAttribute("user", siteUserRepository.findByUsername(loginId));
		
		// ログインIDと編集するユーザーのIDと一致するか判定
		if(principal.getName().equals(siteUserRepository.findByUsername(loginId).getUsername())) {
			return "user-password-edit-form";
		}
		return "redirect:/schedule-board";
	}
	
	@PostMapping("/update-password")
	public String updatePassword(@Validated @ModelAttribute("user") SiteUser user, BindingResult result) {
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		siteUserRepository.save(user);
		
		return "redirect:/schedule-board";
	}

}
