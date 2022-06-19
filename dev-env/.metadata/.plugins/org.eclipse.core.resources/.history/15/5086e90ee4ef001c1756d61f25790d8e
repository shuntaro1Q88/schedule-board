package com.shunproduct.scheduleboard.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.shunproduct.scheduleboard.repository.GroupMemberRepository;
import com.shunproduct.scheduleboard.repository.ScheduleRepository;
import com.shunproduct.scheduleboard.entity.GroupMember;
import com.shunproduct.scheduleboard.entity.Schedule;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDayScheduleListMapService {
	
	private final GroupMemberRepository groupMemberRepository;
	private final ScheduleRepository scheduleRepository;
	private final PullDownContentService pullDownContentService;

	public Map<String, Map<String, List<Schedule>>> prepareUserDayScheduleListMap(int groupId,
			String startDate, String displayTerm) {

		Map<String, Map<String, List<Schedule>>> userDayScheduleListMap = new LinkedHashMap<>();

		// 表示するメンバーのリストを取得
		List<GroupMember> groupMemberList = groupMemberRepository.findByDisplayFlagIsTrueAndGroupIdOrderByDisplayOrder(groupId);

		// 表示する期間の設定
		LocalDate ldStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate ldEndDate = ldStartDate.plusDays(Integer.parseInt(displayTerm));

		List<String> displayDateList = new ArrayList<String>();
		displayDateList.add(null);

		for (int i = 0; i < Integer.parseInt(displayTerm); i++) {
			displayDateList.add(ldStartDate.plusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		}

		// 指定したグループカテゴリ名と期間に存在するスケジュールを取得
		List<Schedule> allScheduleList = scheduleRepository.findAllByGroupCategoryIdAndWorkedOnBetween(groupCategoryId, ldStartDate, ldEndDate);

		for (int i = 0; i < employeeList.size(); i++) {

			Map<String, List<Schedule>> dayScheduleListMap = new LinkedHashMap<>();

			// displayDateListは0番目にnullを追加するため、j=1とした
			for (int j = 1; j < displayDateList.size(); j++) {

				// System.out.println("test-testDisplayDateList : " + displayDateList.get(j));

				List<Schedule> scheduleList = new ArrayList<Schedule>();

				for (int k = 0; k < allScheduleList.size(); k++) {

					// System.out.println(scheduleList.get(k).getWorkedOn());
					if (employeeList.get(i).getEmployeeId().equals(allScheduleList.get(k).getEmployeeId())
							&& displayDateList.get(j).equals(allScheduleList.get(k).getWorkedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {

						scheduleList.add(allScheduleList.get(k));
						// System.out.println("既存予定の数" + testUserDayScheduleList.size());

					}

				}

				// スケジュールがない場合、ダミーのスケジュールオブジェクトを生成
				if (scheduleList.size() == 0) {
					// System.out.println("既存予定なし");

					Schedule schedule = new Schedule();
					
					schedule.setGroupId(employeeList.get(i).getGroupId());
					schedule.setEmployeeId(employeeList.get(i).getEmployeeId());
					schedule.setWorkedOn(LocalDate.parse(displayDateList.get(j), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
					schedule.setScheduleSubject("");
					schedule.setWorkCategoryId(0);

					scheduleList.add(schedule);

				}

				dayScheduleListMap.put(displayDateList.get(j), scheduleList);

			}
			userDayScheduleListMap.put(employeeList.get(i).getEmployeeId() + "_" + employeeList.get(i).getFamilyName()
					+ " " + employeeList.get(i).getFirstName(), dayScheduleListMap);
		}

		return userDayScheduleListMap;

	}

}
