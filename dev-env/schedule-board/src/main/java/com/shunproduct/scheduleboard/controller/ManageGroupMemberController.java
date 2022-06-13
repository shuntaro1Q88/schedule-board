package com.shunproduct.scheduleboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.shunproduct.scheduleboard.entity.GroupMember;
import com.shunproduct.scheduleboard.repository.GroupMemberRepository;
import com.shunproduct.scheduleboard.service.PullDownContentService;
import com.shunproduct.scheduleboard.util.DisplayFlag;
import com.shunproduct.scheduleboard.util.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ManageGroupMemberController {
	
	private final PullDownContentService pullDownContentService;
	private final GroupMemberRepository groupMemberRepository;
	
	@GetMapping("/admin/member-list")
	public String groupMemberList(@ModelAttribute("groupMember") GroupMember groupMember, Model model) {

		// 表示データを抽出するためのプルダウン
		model.addAttribute("pullDownContentService", pullDownContentService);
		// 表示データ
		model.addAttribute("groupMembers", groupMemberRepository.findAll());

		return "member-list";
	}
	
	@PostMapping("/admin/member-list")
	private String filteredSiteUserList(@ModelAttribute("groupMember") GroupMember groupMember, Model model) {
		
		// 選択条件に一致するユーザーを保存
		// 表示データ
		if(groupMember.getGroupId()==0) {
			model.addAttribute("groupMembers", groupMemberRepository.findAllByFamilyNameContaining(groupMember.getFamilyName()));
		}else {
			model.addAttribute("groupMembers", groupMemberRepository.findAllByGroupId(groupMember.getGroupId()));
		}
		// 表示データを抽出するためのプルダウン
		model.addAttribute("pullDownContentService", pullDownContentService);
		// グループの初期値を保存
		model.addAttribute("selectedGroup", groupMember.getGroupId());

		return "member-list";
	}
	
	@GetMapping("/edit-member/{id}")
	public String editEmployee(@PathVariable Long id, Model model) {
		
		// idと対応するメンバーを保存
		model.addAttribute("groupMember", groupMemberRepository.findById(id).get());
		// グループ名と表示フラグのプルダウンの内容を保存
		model.addAttribute("pullDownContentService", pullDownContentService);
		
		// グループの初期値を保存
		model.addAttribute("selectedGroup", groupMemberRepository.findById(id).get().getGroupId());
		// 表示フラグの初期値を保存
		model.addAttribute("selectedDisplayFlag", groupMemberRepository.findById(id).get().getDisplayFlag());
		// 表示順の初期値を保存
		model.addAttribute("selectedDisplayOrder", groupMemberRepository.findById(id).get().getDisplayOrder());
		// 社員番号とグループ名は編集不可にする
		model.addAttribute("editControl", true);
		
		return "member-edit-form";
	}
	
	@PostMapping("/update-member")
	public String update(@ModelAttribute GroupMember groupMember, BindingResult result, Model model) {
		
		groupMemberRepository.save(groupMember);
		
		return "redirect:/admin/member-list";
	}
	
	@GetMapping("/delete-member/{id}")
	public String deleteSiteuser(@PathVariable Long id) {
		
		groupMemberRepository.deleteById(id);
		
		return "redirect:/admin/member-list";
	}

}
