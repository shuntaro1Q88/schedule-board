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

	public Map<String, Map<String, List<Schedule>>> prepareUserDayScheduleListMap(String groupId, String startDate, String displayTerm) {

		Map<String, Map<String, List<Schedule>>> userDayScheduleListMap = new LinkedHashMap<>();

		// 表示するメンバーのリストを取得
		List<GroupMember> groupMemberList = groupMemberRepository.findByDisplayFlagIsTrueAndGroupIdOrderByDisplayOrder(Integer.parseInt(groupId));

		// 表示する期間の設定
		LocalDate ldStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate ldEndDate = ldStartDate.plusDays(Integer.parseInt(displayTerm));

		List<String> displayDateList = new ArrayList<String>();
		displayDateList.add(null);

		for (int i = 0; i < Integer.parseInt(displayTerm); i++) {
			displayDateList.add(ldStartDate.plusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		}

		// 指定したグループカテゴリ名と期間に存在するスケジュールを取得
		List<Schedule> allScheduleList = scheduleRepository.findByGroupIdAndMainDateBetween(Integer.parseInt(groupId), ldStartDate, ldEndDate);

		for (int i = 0; i < groupMemberList.size(); i++) {

			Map<String, List<Schedule>> dayScheduleListMap = new LinkedHashMap<>();

			// displayDateListは0番目にnullを追加するため、j=1とした
			for (int j = 1; j < displayDateList.size(); j++) {

				// System.out.println("test-testDisplayDateList : " + displayDateList.get(j));

				List<Schedule> scheduleList = new ArrayList<Schedule>();

				for (int k = 0; k < allScheduleList.size(); k++) {

					// System.out.println(scheduleList.get(k).getWorkedOn());
					if (groupMemberList.get(i).getMemberId().equals(allScheduleList.get(k).getMemberId())
							&& displayDateList.get(j).equals(allScheduleList.get(k).getMainDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {

						scheduleList.add(allScheduleList.get(k));
						// System.out.println("既存予定の数" + testUserDayScheduleList.size());

					}

				}

				// スケジュールがない場合、ダミーのスケジュールオブジェクトを生成
				if (scheduleList.size() == 0) {
					// System.out.println("既存予定なし");

					Schedule schedule = new Schedule();
					
					schedule.setGroupId(groupMemberList.get(i).getGroupId());
					schedule.setMemberId(groupMemberList.get(i).getMemberId());
					schedule.setMainDate(LocalDate.parse(displayDateList.get(j), DateTimeFormatter.ofPattern("yyyy-MM-dd")));

					scheduleList.add(schedule);

				}

				dayScheduleListMap.put(displayDateList.get(j), scheduleList);

			}
			userDayScheduleListMap.put(groupMemberList.get(i).getMemberId() + "_" + groupMemberList.get(i).getFamilyName()
					+ " " + groupMemberList.get(i).getFirstName(), dayScheduleListMap);
		}

		return userDayScheduleListMap;

	}

}
