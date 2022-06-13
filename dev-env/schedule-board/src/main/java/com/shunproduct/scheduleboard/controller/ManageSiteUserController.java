package com.shunproduct.scheduleboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.shunproduct.scheduleboard.entity.SiteUser;
import com.shunproduct.scheduleboard.repository.SiteUserRepository;
import com.shunproduct.scheduleboard.service.PullDownContentService;
import com.shunproduct.scheduleboard.util.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ManageSiteUserController {
	
	private final PullDownContentService pullDownContentService;
	private final SiteUserRepository siteUserRepository;
	
	@GetMapping("/admin/user-list")
	public String siteUserList(@ModelAttribute("siteUser") SiteUser siteUser, Model model) {

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
			model.addAttribute("siteUsers", siteUserRepository.findAllByGroupId(siteUser.getGroupId()));
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

}
