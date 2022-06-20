package com.shunproduct.scheduleboard.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.shunproduct.scheduleboard.entity.Group;
import com.shunproduct.scheduleboard.repository.GroupRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PullDownContentService {
	
	private final GroupRepository groupRepository;
	
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

}
