package com.shunproduct.scheduleboard.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.shunproduct.scheduleboard.entity.SiteUser;
import com.shunproduct.scheduleboard.repository.SiteUserRepository;
import com.shunproduct.scheduleboard.service.PullDownContentService;
import com.shunproduct.scheduleboard.util.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class RegisterSiteUserController {
	
	private final PullDownContentService pullDownContentService;
	private final PasswordEncoder passwordEncoder;
	private final SiteUserRepository userRepository;

	
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

		userRepository.save(siteUser);

		return "redirect:/login?register";
	}

}
