package com.shunproduct.scheduleboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.shunproduct.scheduleboard.entity.GroupMember;
import com.shunproduct.scheduleboard.repository.GroupMemberRepository;
import com.shunproduct.scheduleboard.service.PullDownContentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class GroupMemberController {
	
	private final PullDownContentService pullDownContentService;
	private final GroupMemberRepository groupMemberRepository;
	
	
	/* 
	 * 表示メンバーリスト画面の制御
	 * 
	 */
	
	@GetMapping("/admin/member-list")
	public String groupMemberList(@ModelAttribute("groupMember") GroupMember groupMember, Model model) {

		// 表示データを抽出するためのプルダウン
		model.addAttribute("pullDownContentService", pullDownContentService);
		// 表示データ
		model.addAttribute("groupMembers", groupMemberRepository.findAllOrderByGroupIdASCAndDisplayOrderAsc());

		return "member-list";
	}
	
	@PostMapping("/admin/member-list")
	private String postedGroupMemberList(@ModelAttribute("groupMember") GroupMember groupMember, Model model) {
		
		// 選択条件に一致するユーザーを保存
		// 表示データ
		if(groupMember.getGroupId()==0) {
			model.addAttribute("groupMembers", groupMemberRepository.findAllByFamilyNameContainingOrderByDisplayOrderAsc(groupMember.getFamilyName()));
		}else {
			model.addAttribute("groupMembers", groupMemberRepository.findAllByGroupIdAndFamilyNameContainingOrderByDisplayOrderAsc(groupMember.getGroupId(),groupMember.getFamilyName()));
		}
		// 表示データを抽出するためのプルダウン
		model.addAttribute("pullDownContentService", pullDownContentService);
		// グループの初期値を保存
		model.addAttribute("selectedGroup", groupMember.getGroupId());

		return "member-list";
	}
	
	/* 
	 * 表示メンバーリストの追加、編集、削除
	 * 
	 */
	
	// GroupMemberの新規入力
	@GetMapping("/register-member")
	public String register(@ModelAttribute("groupMember") GroupMember groupMember, Model model) {
		
		// グループ名と表示フラグのプルダウンの内容を保存
		model.addAttribute("pullDownContentService", pullDownContentService);
		
		return "member-edit-form";
	}
	
	// GroupMemberの新規登録
	@PostMapping("/update-member")
	public String newUpdateGroupMember(@Validated @ModelAttribute("groupMember") GroupMember groupMember, BindingResult result, Model model) {
		
		// 失敗の場合
		if (result.hasErrors()) {
			
			// グループ名のプルダウンの内容を保存
			model.addAttribute("pullDownContentService", pullDownContentService);

			return "member-edit-form";
		}
		
		groupMemberRepository.save(groupMember);
		
		return "redirect:/admin/member-list";
	}
	
	// 登録済みのGroupMember情報を編集
	@GetMapping("/edit-member/{id}")
	public String edit(@PathVariable Long id, Model model) {
		
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
	
	// 登録済みのGroupMember情報を更新
	@PostMapping("/update-member/{id}")
	public String update(@PathVariable Long id, @Validated @ModelAttribute("groupMember") GroupMember groupMember, BindingResult result, Model model) {
		
		// 失敗の場合
		if (result.hasErrors()) {

			// idと対応するメンバーを保存
			model.addAttribute("groupMember", groupMemberRepository.findById(id).get());
			
			// グループ名のプルダウンの内容を保存
			model.addAttribute("pullDownContentService", pullDownContentService);
			// グループの初期値を保存
			model.addAttribute("selectedGroup", groupMemberRepository.findById(id).get().getGroupId());
			// 表示フラグの初期値を保存
			model.addAttribute("selectedDisplayFlag", groupMemberRepository.findById(id).get().getDisplayFlag());
			// 社員番号とグループ名は編集不可にする
			model.addAttribute("editControl", true);

			return "redirect:/edit-member/" + id + "?error";
		}
		
		groupMemberRepository.save(groupMember);
		
		return "redirect:/admin/member-list";
	}
	
	@GetMapping("/delete-member/{id}")
	public String delete(@PathVariable Long id) {
		
		groupMemberRepository.deleteById(id);
		
		return "redirect:/admin/member-list";
	}

}
