package com.shunproduct.scheduleboard.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.shunproduct.scheduleboard.entity.ScheduleCategory;
import com.shunproduct.scheduleboard.entity.ScheduleStatus;
import com.shunproduct.scheduleboard.repository.ScheduleCategoryRepository;
import com.shunproduct.scheduleboard.repository.ScheduleStatusRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ViewStyleService {

	private final ScheduleCategoryRepository scheduleCategoryRepository;
	private final ScheduleStatusRepository scheduleStatusRepository;

	// "key:categoryId value:categoryBgColor"のMapを準備
	public Map<Integer, String> prepareScheduleCategoryMap() {
		List<ScheduleCategory> scheduleCategoryList = (List<ScheduleCategory>) scheduleCategoryRepository.findAll();

		Map<Integer, String> scheduleCategoryMap = new LinkedHashMap<>();

		for (int i = 0; i < scheduleCategoryList.size(); i++) {
			scheduleCategoryMap.put(scheduleCategoryList.get(i).getCategoryId(),
					scheduleCategoryList.get(i).getCategoryBgColor());
		}
		return scheduleCategoryMap;
	}

	// "key:statusId value:statusSymbol"のMapを準備
	public Map<Integer, String> prepareScheduleStatusMap() {

		List<ScheduleStatus> scheduleStatusList = (List<ScheduleStatus>) scheduleStatusRepository.findAll();
		Map<Integer, String> scheduleStatusMap = new LinkedHashMap<>();

		for (int i = 0; i < scheduleStatusList.size(); i++) {
			scheduleStatusMap.put(scheduleStatusList.get(i).getStatusId(), scheduleStatusList.get(i).getStatusSymbol());
		}
		return scheduleStatusMap;
	}

}
