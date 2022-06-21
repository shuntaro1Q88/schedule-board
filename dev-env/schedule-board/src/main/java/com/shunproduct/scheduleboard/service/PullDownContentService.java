package com.shunproduct.scheduleboard.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.shunproduct.scheduleboard.entity.Group;
import com.shunproduct.scheduleboard.entity.GroupMember;
import com.shunproduct.scheduleboard.entity.ScheduleCategory;
import com.shunproduct.scheduleboard.entity.ScheduleStatus;
import com.shunproduct.scheduleboard.repository.GroupMemberRepository;
import com.shunproduct.scheduleboard.repository.GroupRepository;
import com.shunproduct.scheduleboard.repository.ScheduleCategoryRepository;
import com.shunproduct.scheduleboard.repository.ScheduleStatusRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PullDownContentService {
	
	private final GroupRepository groupRepository;
	private final GroupMemberRepository groupMemberRepository;
	private final ScheduleStatusRepository scheduleStatusRepository;
	private final ScheduleCategoryRepository scheduleCategoryRepository;
	
	// グループのプルダウンの内容を準備
	public Map<Integer, String> prepareGroupMap(){
		
		List<Group> groupList = groupRepository.findByDisplayFlagIsTrueOrderByDisplayOrderAsc();
		
		Map<Integer, String> groupMap = new LinkedHashMap<>();
		for (int i = 0; i < groupList.size(); i++) {
			groupMap.put(groupList.get(i).getGroupId(), groupList.get(i).getGroupName());
		}
		return groupMap;
	}
	
	public String findGroupName(int groupId) {

		String groupName = groupRepository.findByGroupId(groupId).getGroupName();
		return groupName;
	}
	
	// 表示/非表示のプルダウンの内容を準備
	public Map<Boolean, String> prepareDisplayMap() {

		Map<Boolean, String> booleanMap = new LinkedHashMap<>();
		booleanMap.put(true, "表示");
		booleanMap.put(false, "非表示");

		return booleanMap;
	}
	
	// 表示期間のプルダウンを準備
	public Map<Integer, String> prepareDisplayTermMap() {

		Map<Integer, String> displayTermMap = new LinkedHashMap<>();
		displayTermMap.put(7, "7日間");
		displayTermMap.put(14, "14日間");
		displayTermMap.put(28, "28日間");

		return displayTermMap;
	}
	
	// 作業者一覧のプルダウンを準備
	public Map<String, String> prepareGroupMemberMap(int groupId){
		
		List<GroupMember> groupMemberList = groupMemberRepository.findByDisplayFlagIsTrueAndGroupIdOrderByDisplayOrder(groupId);
		Map<String, String> groupMemberMap = new LinkedHashMap<>();
		groupMemberMap.put("", "");
		for (int i = 0; i < groupMemberList.size(); i++) {
			groupMemberMap.put(groupMemberList.get(i).getMemberId(), groupMemberList.get(i).getFamilyName() + " " + groupMemberList.get(i).getFirstName());
		}
		return groupMemberMap;
	}
	
	// 状態 (確定 or 仮) のプルダウンを準備
	public Map<Integer, String> prepareScheduleStatusMap() {
		
		List<ScheduleStatus> scheduleStatusList = (List<ScheduleStatus>) scheduleStatusRepository.findAll();
		Map<Integer, String> scheduleStatusMap = new LinkedHashMap<>();
		
		for (int i = 0; i < scheduleStatusList.size(); i++) {
			scheduleStatusMap.put(scheduleStatusList.get(i).getStatusId(),scheduleStatusList.get(i).getStatusSymbol()+"-"+scheduleStatusList.get(i).getStatusName());
		}
		return scheduleStatusMap;
	}
	
	// 作業区分のプルダウンを準備
	public Map<Integer, String> prepareScheduleCategoryMap(){
		
		List<ScheduleCategory> scheduleCategoryList = (List<ScheduleCategory>) scheduleCategoryRepository.findAll();
		Map<Integer, String> scheduleCategoryMap = new LinkedHashMap<>();
		for (int i = 0; i < scheduleCategoryList.size(); i++) {
			scheduleCategoryMap.put(scheduleCategoryList.get(i).getCategoryId(),scheduleCategoryList.get(i).getCategoryName());	
		}
		return scheduleCategoryMap;
	}

}
