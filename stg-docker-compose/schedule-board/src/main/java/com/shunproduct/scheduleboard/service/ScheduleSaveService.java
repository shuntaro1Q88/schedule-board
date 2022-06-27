package com.shunproduct.scheduleboard.service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shunproduct.scheduleboard.entity.Schedule;
import com.shunproduct.scheduleboard.entity.SiteUser;
import com.shunproduct.scheduleboard.repository.ScheduleRepository;
import com.shunproduct.scheduleboard.repository.SiteUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleSaveService {

	private final ScheduleRepository scheduleRepository;
	private final SiteUserRepository siteUserRepository;

	Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	/* idの有無から、"新規登録" or "既存予定の更新" の判定 */
	public void addOrUpdate(Schedule schedule, Principal principal) {

		// 作業者リストを取得
		List<String> memberList = prepareMemberList(schedule.getMemberId(), schedule.getAddMemberId1st(),
				schedule.getAddMemberId2nd(), schedule.getAddMemberId3rd(), schedule.getAddMemberId4th(),
				schedule.getAddMemberId5th());
		// 作業日リストを取得
		List<LocalDate> workDateList = prepareWorkDateList(schedule.getMainDate(), schedule.getAddDate1st(),
				schedule.getAddDate2nd(), schedule.getAddDate3rd(), schedule.getAddDate4th(), schedule.getAddDate5th());

		System.out.println("--入力内容-------------------------");
		System.out.println("確認　ID " + schedule.getScheduleId());
		System.out.println("確認　グループID " + schedule.getScheduleGroupId());
		System.out.println("確認　作業者リスト " + memberList.size());
		System.out.println("確認　作業日リスト " + workDateList.size());
		System.out.println("----------------------------------");

		// idがない⇒新規登録、idがある⇒既存予定の更新
		if (schedule.getScheduleId() == null) {
			addProcess(schedule, memberList, workDateList, principal);
		} else {
			updateProcess(schedule, memberList, workDateList, principal);
		}
	}

	// ログインユーザーの姓名の取得メソッド、updatedByに更新者をsetするため
	public SiteUser findLoginUser(Principal principal) {

		return siteUserRepository.findByUsername(principal.getName());

	}

	// 作業者リストの取得メソッド
	public List<String> prepareMemberList(
			String memberId, String addMemberId1st, String addMemberId2nd, String addMemberId3rd, String addMemberId4th, String addMemberId5th) {

		List<String> memberList = new ArrayList<String>();

		if (StringUtils.isNotBlank(addMemberId1st)) {
			memberList.add(addMemberId1st);
		}
		if (StringUtils.isNotBlank(addMemberId2nd)) {
			memberList.add(addMemberId2nd);
		}
		if (StringUtils.isNotBlank(addMemberId3rd)) {
			memberList.add(addMemberId3rd);
		}
		if (StringUtils.isNotBlank(addMemberId4th)) {
			memberList.add(addMemberId4th);
		}
		if (StringUtils.isNotBlank(addMemberId5th)) {
			memberList.add(addMemberId5th);
		}

		// リスト内の重複削除 参考 https://www.sejuku.net/blog/15704
		memberList = memberList.stream().distinct().collect(Collectors.toList());

		if (memberList.size() == 0) {
			memberList.add(memberId);
		}
		return memberList;
	}

	// 作業日リストの取得メソッド
	public List<LocalDate> prepareWorkDateList(
			LocalDate mainDate, LocalDate addDate1st, LocalDate addDate2nd, LocalDate addDate3rd, LocalDate addDate4th, LocalDate addDate5th) {

		List<LocalDate> workDateList = new ArrayList<LocalDate>();

		if (addDate1st != null) {
			workDateList.add(addDate1st);
		}
		if (addDate2nd != null) {
			workDateList.add(addDate2nd);
		}
		if (addDate3rd != null) {
			workDateList.add(addDate3rd);
		}
		if (addDate4th != null) {
			workDateList.add(addDate4th);
		}
		if (addDate5th != null) {
			workDateList.add(addDate5th);
		}

		// リスト内の重複削除 参考 https://www.sejuku.net/blog/15704
		workDateList = workDateList.stream().distinct().collect(Collectors.toList());
		Collections.sort(workDateList);

		return workDateList;
	}

	/*
	 * 新規登録に対する処理 -start-
	 */
	public void addProcess(Schedule schedule, List<String> memberList, List<LocalDate> workDateList,
			Principal principal) {

		// 更新者情報をセット
		schedule.setCreatedBy(findLoginUser(principal).getUsername());
		schedule.setUpdatedBy(findLoginUser(principal).getUsername());

		// 1-1 新規登録に対する処理 グループID：ナシ、作業者リスト：ナシ、作業日リスト：ナシ
		if (StringUtils.isBlank(schedule.getScheduleGroupId()) && memberList.size() <= 1 && workDateList.size() == 1) {

			System.out.println("新規登録に対する処理 グループID：ナシ、作業者リスト：ナシ、作業日リスト：ナシ");

			// 作業者
			schedule.setMemberId(memberList.get(0));
			schedule.setAddMemberId1st(memberList.get(0));
			// 作業日
			schedule.setMainDate(workDateList.get(0));
			schedule.setAddDate1st(workDateList.get(0));

			scheduleRepository.save(schedule);

			// 1-2 新規登録に対する処理 グループID：ナシ、作業者リスト：アリ、作業日リスト：ナシ
		} else if (StringUtils.isBlank(schedule.getScheduleGroupId()) && memberList.size() > 1 && workDateList.size() == 1) {

			System.out.println("新規登録に対する処理 グループID：ナシ、作業者リスト：アリ、作業日リスト：ナシ");

			// "scheduleGroupId"の用意
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

			// 作業者リストの人数分、Scheduleを生成
			for (int i = 0; i < memberList.size(); i++) {

				Schedule addSchedule = new Schedule();

				// 定番
				addSchedule.setGroupId(schedule.getGroupId());
				addSchedule.setCategoryId(schedule.getCategoryId());
				addSchedule.setStatusId(schedule.getStatusId());
				addSchedule.setSubjectLine(schedule.getSubjectLine());
				addSchedule.setContent(schedule.getContent());
				addSchedule.setMemo(schedule.getMemo());
				addSchedule.setCreatedBy(schedule.getCreatedBy());
				addSchedule.setUpdatedBy(schedule.getUpdatedBy());
				// グループIDの付与
				addSchedule.setScheduleGroupId(schedule.getMemberId() + dtformat.format(now));
				// 作業日
				addSchedule.setMainDate(workDateList.get(0));
				addSchedule.setAddDate1st(workDateList.get(0));
				// 作業者
				addSchedule.setMemberId(memberList.get(i));
				switch (memberList.size()) {
				case 2:
					addSchedule.setAddMemberId1st(memberList.get(0));
					addSchedule.setAddMemberId2nd(memberList.get(1));
					break;
				case 3:
					addSchedule.setAddMemberId1st(memberList.get(0));
					addSchedule.setAddMemberId2nd(memberList.get(1));
					addSchedule.setAddMemberId3rd(memberList.get(2));
					break;
				case 4:
					addSchedule.setAddMemberId1st(memberList.get(0));
					addSchedule.setAddMemberId2nd(memberList.get(1));
					addSchedule.setAddMemberId3rd(memberList.get(2));
					addSchedule.setAddMemberId4th(memberList.get(3));
					break;
				case 5:
					addSchedule.setAddMemberId1st(memberList.get(0));
					addSchedule.setAddMemberId2nd(memberList.get(1));
					addSchedule.setAddMemberId3rd(memberList.get(2));
					addSchedule.setAddMemberId4th(memberList.get(3));
					addSchedule.setAddMemberId5th(memberList.get(4));
					break;
				}
				scheduleRepository.save(addSchedule);
			}

			// 1-3 新規登録に対する処理 グループID：ナシ、作業者リスト：ナシ、作業日リスト：アリ
		} else if (StringUtils.isBlank(schedule.getScheduleGroupId()) && memberList.size() <= 1 && workDateList.size() > 1) {

			System.out.println("処理内容 : 新規登録に対する処理 グループID：ナシ、作業者リスト：ナシ、作業日リスト：アリ");

			// "scheduleGroupId"の用意
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

			// 作業日リストの日数分、Scheduleを生成
			for (int i = 0; i < workDateList.size(); i++) {

				Schedule addSchedule = new Schedule();

				// 定番
				addSchedule.setGroupId(schedule.getGroupId());
				addSchedule.setCategoryId(schedule.getCategoryId());
				addSchedule.setStatusId(schedule.getStatusId());
				addSchedule.setSubjectLine(schedule.getSubjectLine());
				addSchedule.setContent(schedule.getContent());
				addSchedule.setMemo(schedule.getMemo());
				addSchedule.setCreatedBy(schedule.getCreatedBy());
				addSchedule.setUpdatedBy(schedule.getUpdatedBy());
				// グループIDの付与
				addSchedule.setScheduleGroupId(schedule.getMemberId() + dtformat.format(now));
				// 作業者
				addSchedule.setMemberId(memberList.get(0));
				addSchedule.setAddMemberId1st(memberList.get(0));
				// 作業日
				addSchedule.setMainDate(workDateList.get(i));
				switch (workDateList.size()) {
				case 2:
					addSchedule.setAddDate1st(workDateList.get(0));
					addSchedule.setAddDate2nd(workDateList.get(1));
					break;
				case 3:
					addSchedule.setAddDate1st(workDateList.get(0));
					addSchedule.setAddDate2nd(workDateList.get(1));
					addSchedule.setAddDate3rd(workDateList.get(2));
					break;
				case 4:
					addSchedule.setAddDate1st(workDateList.get(0));
					addSchedule.setAddDate2nd(workDateList.get(1));
					addSchedule.setAddDate3rd(workDateList.get(2));
					addSchedule.setAddDate4th(workDateList.get(3));
					break;
				case 5:
					addSchedule.setAddDate1st(workDateList.get(0));
					addSchedule.setAddDate2nd(workDateList.get(1));
					addSchedule.setAddDate3rd(workDateList.get(2));
					addSchedule.setAddDate4th(workDateList.get(3));
					addSchedule.setAddDate5th(workDateList.get(4));
					break;
				}
				scheduleRepository.save(addSchedule);
			}

			// 1-4 新規登録に対する処理 グループID：ナシ、作業者リスト：アリ、作業日リスト：アリ
		} else if (StringUtils.isBlank(schedule.getScheduleGroupId()) && memberList.size() > 1 && workDateList.size() > 1) {

			System.out.println("1-4 新規登録に対する処理 グループID：ナシ、作業者リスト：アリ、作業日リスト：アリ");
			System.out.println(schedule.getCreatedBy());
			System.out.println(schedule.getUpdatedBy());

			// "scheduleGroupId"の用意
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

			// 作業者リストの人数分、回す
			for (int i = 0; i < memberList.size(); i++) {

				// 作業日リストの日数分、回す
				for (int j = 0; j < workDateList.size(); j++) {

					Schedule addSchedule = new Schedule();

					// 定番
					addSchedule.setGroupId(schedule.getGroupId());
					addSchedule.setCategoryId(schedule.getCategoryId());
					addSchedule.setStatusId(schedule.getStatusId());
					addSchedule.setSubjectLine(schedule.getSubjectLine());
					addSchedule.setContent(schedule.getContent());
					addSchedule.setMemo(schedule.getMemo());
					addSchedule.setCreatedBy(schedule.getCreatedBy());
					addSchedule.setUpdatedBy(schedule.getUpdatedBy());
					// グループIDの付与
					addSchedule.setScheduleGroupId(schedule.getMemberId() + dtformat.format(now));
					// 作業者
					addSchedule.setMemberId(memberList.get(i));
					switch (memberList.size()) {
					case 2:
						addSchedule.setAddMemberId1st(memberList.get(0));
						addSchedule.setAddMemberId2nd(memberList.get(1));
						break;
					case 3:
						addSchedule.setAddMemberId1st(memberList.get(0));
						addSchedule.setAddMemberId2nd(memberList.get(1));
						addSchedule.setAddMemberId3rd(memberList.get(2));
						break;
					case 4:
						addSchedule.setAddMemberId1st(memberList.get(0));
						addSchedule.setAddMemberId2nd(memberList.get(1));
						addSchedule.setAddMemberId3rd(memberList.get(2));
						addSchedule.setAddMemberId4th(memberList.get(3));
						break;
					case 5:
						addSchedule.setAddMemberId1st(memberList.get(0));
						addSchedule.setAddMemberId2nd(memberList.get(1));
						addSchedule.setAddMemberId3rd(memberList.get(2));
						addSchedule.setAddMemberId4th(memberList.get(3));
						addSchedule.setAddMemberId5th(memberList.get(4));
						break;
					}
					// 作業日
					addSchedule.setMainDate(workDateList.get(j));
					switch (workDateList.size()) {
					case 2:
						addSchedule.setAddDate1st(workDateList.get(0));
						addSchedule.setAddDate2nd(workDateList.get(1));
						break;
					case 3:
						addSchedule.setAddDate1st(workDateList.get(0));
						addSchedule.setAddDate2nd(workDateList.get(1));
						addSchedule.setAddDate3rd(workDateList.get(2));
						break;
					case 4:
						addSchedule.setAddDate1st(workDateList.get(0));
						addSchedule.setAddDate2nd(workDateList.get(1));
						addSchedule.setAddDate3rd(workDateList.get(2));
						addSchedule.setAddDate4th(workDateList.get(3));
						break;
					case 5:
						addSchedule.setAddDate1st(workDateList.get(0));
						addSchedule.setAddDate2nd(workDateList.get(1));
						addSchedule.setAddDate3rd(workDateList.get(2));
						addSchedule.setAddDate4th(workDateList.get(3));
						addSchedule.setAddDate5th(workDateList.get(4));
						break;
					}
					scheduleRepository.save(addSchedule);
				}
			}
		}
	}
	/*
	 * 新規登録に対する処理 -end-
	 * 
	 */

	/*
	 * 既存予定の更新に対する処理 -start-
	 * 
	 */
	public void updateProcess(
			Schedule schedule, List<String> memberList, List<LocalDate> workDateList, Principal principal) {

		schedule.setUpdatedBy(findLoginUser(principal).getUsername());

		// 2-1 既存予定の更新に対する処理 グループID：ナシ、作業者リスト：ナシ、作業日リスト：ナシ
		if (StringUtils.isBlank(schedule.getScheduleGroupId()) && memberList.size() <= 1 && workDateList.size() == 1) {

			System.out.println("2-1 既存予定の更新に対する処理 グループID：ナシ、作業者リスト：ナシ、作業日リスト：ナシ");
			// 作業者
			schedule.setMemberId(memberList.get(0));
			schedule.setAddMemberId1st(memberList.get(0));
			// 作業日
			schedule.setMainDate(workDateList.get(0));
			schedule.setAddDate1st(workDateList.get(0));
			schedule.setAddDate2nd(workDateList.get(0));
			schedule.setAddDate3rd(workDateList.get(0));
			schedule.setAddDate4th(workDateList.get(0));
			schedule.setAddDate5th(workDateList.get(0));
			
			scheduleRepository.save(schedule);

			// 2-2 既存予定の更新に対する処理
			// グループID：ナシ、作業者リスト：アリ、作業日リスト：ナシ(グループ化されていない予定を、作業者リスト：アリのグループ化した予定に変更)
		} else if (StringUtils.isBlank(schedule.getScheduleGroupId()) && memberList.size() > 1 && workDateList.size() == 1) {

			System.out.println("2-2 既存予定の更新に対する処理 グループID：ナシ、作業者リスト：アリ、作業日リスト：ナシ(グループ化されていない予定を、作業者リスト：アリのグループ化した予定に変更)");

			// "scheduleGroupId"の用意
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

			// 作業者リストの人数分、Scheduleを生成
			for (int i = 0; i < memberList.size(); i++) {

				Schedule addSchedule = new Schedule();

				// 定番
				addSchedule.setGroupId(schedule.getGroupId());
                addSchedule.setCategoryId(schedule.getCategoryId());
				addSchedule.setStatusId(schedule.getStatusId());
				addSchedule.setSubjectLine(schedule.getSubjectLine());
				addSchedule.setContent(schedule.getContent());
				addSchedule.setMemo(schedule.getMemo());
				addSchedule.setCreatedBy(schedule.getCreatedBy());
				addSchedule.setUpdatedBy(schedule.getUpdatedBy());
				// グループIDの付与
				addSchedule.setScheduleGroupId(schedule.getMemberId() + dtformat.format(now));
				// 作業日
				addSchedule.setMainDate(workDateList.get(0));
				addSchedule.setAddDate1st(workDateList.get(0));
				// 作業者
				addSchedule.setMemberId(memberList.get(i));
				switch (memberList.size()) {
				case 2:
					addSchedule.setAddMemberId1st(memberList.get(0));
					addSchedule.setAddMemberId2nd(memberList.get(1));
					break;
				case 3:
					addSchedule.setAddMemberId1st(memberList.get(0));
					addSchedule.setAddMemberId2nd(memberList.get(1));
					addSchedule.setAddMemberId3rd(memberList.get(2));
					break;
				case 4:
					addSchedule.setAddMemberId1st(memberList.get(0));
					addSchedule.setAddMemberId2nd(memberList.get(1));
					addSchedule.setAddMemberId3rd(memberList.get(2));
					addSchedule.setAddMemberId4th(memberList.get(3));
					break;
				case 5:
					addSchedule.setAddMemberId1st(memberList.get(0));
					addSchedule.setAddMemberId2nd(memberList.get(1));
					addSchedule.setAddMemberId3rd(memberList.get(2));
					addSchedule.setAddMemberId4th(memberList.get(3));
					addSchedule.setAddMemberId5th(memberList.get(4));
					break;
				}
				scheduleRepository.save(addSchedule);
			}
			scheduleRepository.deleteById(schedule.getScheduleId());

			// 2-3 既存予定の更新に対する処理
			// グループID：ナシ、作業者リスト：ナシ、作業日リスト：アリ(グループ化されていない予定を、作業日リスト：アリのグループ化された予定に変更)
		} else if (StringUtils.isBlank(schedule.getScheduleGroupId()) && memberList.size() <= 1
				&& workDateList.size() > 1) {

			System.out.println("2-3 既存予定の更新に対する処理 グループID：ナシ、作業者リスト：ナシ、作業日リスト：アリ(グループ化されていない予定を、作業日リスト：アリのグループ化された予定に変更)");

			// "scheduleGroupId"の用意
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

			// 作業日リストの日数分、Scheduleを生成
			for (int i = 0; i < workDateList.size(); i++) {

				Schedule addSchedule = new Schedule();

				// 定番
				addSchedule.setGroupId(schedule.getGroupId());
				addSchedule.setCategoryId(schedule.getCategoryId());
				addSchedule.setStatusId(schedule.getStatusId());
				addSchedule.setSubjectLine(schedule.getSubjectLine());
				addSchedule.setContent(schedule.getContent());
				addSchedule.setMemo(schedule.getMemo());
				addSchedule.setCreatedBy(schedule.getCreatedBy());
				addSchedule.setUpdatedBy(schedule.getUpdatedBy());
				// グループIDの付与
				addSchedule.setScheduleGroupId(schedule.getMemberId() + dtformat.format(now));
				// 作業者
				addSchedule.setMemberId(memberList.get(0));
				addSchedule.setAddMemberId1st(memberList.get(0));
				// 作業日リストの処理
				addSchedule.setMainDate(workDateList.get(i));
				switch (workDateList.size()) {
				case 2:
					addSchedule.setAddDate1st(workDateList.get(0));
					addSchedule.setAddDate2nd(workDateList.get(1));
					break;
				case 3:
					addSchedule.setAddDate1st(workDateList.get(0));
					addSchedule.setAddDate2nd(workDateList.get(1));
					addSchedule.setAddDate3rd(workDateList.get(2));
					break;
				case 4:
					addSchedule.setAddDate1st(workDateList.get(0));
					addSchedule.setAddDate2nd(workDateList.get(1));
					addSchedule.setAddDate3rd(workDateList.get(2));
					addSchedule.setAddDate4th(workDateList.get(3));
					break;
				case 5:
					addSchedule.setAddDate1st(workDateList.get(0));
					addSchedule.setAddDate2nd(workDateList.get(1));
					addSchedule.setAddDate3rd(workDateList.get(2));
					addSchedule.setAddDate4th(workDateList.get(3));
					addSchedule.setAddDate5th(workDateList.get(4));
					break;
				}
				scheduleRepository.save(addSchedule);
			}
			scheduleRepository.deleteById(schedule.getScheduleId());

			// 2-4 既存予定の更新に対する処理
			// グループID：ナシ、作業者リスト：アリ、作業日リスト：アリ(グループ化されていない予定を、作業者リスト：アリ&&作業日リスト：アリのグループ化された予定に変更)
		} else if (StringUtils.isBlank(schedule.getScheduleGroupId()) && memberList.size() > 1 && workDateList.size() > 1) {

			System.out.println("2-4 既存予定の更新に対する処理 グループID：ナシ、作業者リスト：アリ、作業日リスト：アリ(グループ化されていない予定を、作業者リスト：アリ&&作業日リスト：アリのグループ化された予定に変更)");

			// "scheduleGroupId"の用意
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

			// 作業者リストの人数分、回す
			for (int i = 0; i < memberList.size(); i++) {

				// 作業日リストの日数分、回す
				for (int j = 0; j < workDateList.size(); j++) {

					Schedule addSchedule = new Schedule();

					// 定番
					addSchedule.setGroupId(schedule.getGroupId());
					addSchedule.setCategoryId(schedule.getCategoryId());
					addSchedule.setStatusId(schedule.getStatusId());
					addSchedule.setSubjectLine(schedule.getSubjectLine());
					addSchedule.setContent(schedule.getContent());
					addSchedule.setMemo(schedule.getMemo());
					addSchedule.setCreatedBy(schedule.getCreatedBy());
					addSchedule.setUpdatedBy(schedule.getUpdatedBy());
					// グループIDの付与
					addSchedule.setScheduleGroupId(schedule.getMemberId() + dtformat.format(now));
					// 作業者リストの処理
					addSchedule.setMemberId(memberList.get(i));
					switch (memberList.size()) {
					case 2:
						addSchedule.setAddMemberId1st(memberList.get(0));
						addSchedule.setAddMemberId2nd(memberList.get(1));
						break;
					case 3:
						addSchedule.setAddMemberId1st(memberList.get(0));
						addSchedule.setAddMemberId2nd(memberList.get(1));
						addSchedule.setAddMemberId3rd(memberList.get(2));
						break;
					case 4:
						addSchedule.setAddMemberId1st(memberList.get(0));
						addSchedule.setAddMemberId2nd(memberList.get(1));
						addSchedule.setAddMemberId3rd(memberList.get(2));
						addSchedule.setAddMemberId4th(memberList.get(3));
						break;
					case 5:
						addSchedule.setAddMemberId1st(memberList.get(0));
						addSchedule.setAddMemberId2nd(memberList.get(1));
						addSchedule.setAddMemberId3rd(memberList.get(2));
						addSchedule.setAddMemberId4th(memberList.get(3));
						addSchedule.setAddMemberId5th(memberList.get(4));
						break;
					}
					addSchedule.setMainDate(workDateList.get(j));
					switch (workDateList.size()) {
					case 2:
						addSchedule.setAddDate1st(workDateList.get(0));
						addSchedule.setAddDate2nd(workDateList.get(1));
						break;
					case 3:
						addSchedule.setAddDate1st(workDateList.get(0));
						addSchedule.setAddDate2nd(workDateList.get(1));
						addSchedule.setAddDate3rd(workDateList.get(2));
						break;
					case 4:
						addSchedule.setAddDate1st(workDateList.get(0));
						addSchedule.setAddDate2nd(workDateList.get(1));
						addSchedule.setAddDate3rd(workDateList.get(2));
						addSchedule.setAddDate4th(workDateList.get(3));
						break;
					case 5:
						addSchedule.setAddDate1st(workDateList.get(0));
						addSchedule.setAddDate2nd(workDateList.get(1));
						addSchedule.setAddDate3rd(workDateList.get(2));
						addSchedule.setAddDate4th(workDateList.get(3));
						addSchedule.setAddDate5th(workDateList.get(4));
						break;
					}
					scheduleRepository.save(addSchedule);
				}
			}
			scheduleRepository.deleteById(schedule.getScheduleId());

			// 3-1 既存予定の更新に対する処理 グループID：アリ、作業者リスト：ナシ、作業日リスト：ナシ
			// (グループ化された予定を、作業者リスト：ナシ&&作業日リスト：ナシのグループ化しない予定に変更)
		} else if (StringUtils.isNotBlank(schedule.getScheduleGroupId()) && memberList.size() <= 1 && workDateList.size() == 1) {

			System.out.println("3-1 既存予定の更新に対する処理 グループID：アリ、作業者リスト：ナシ、作業日リスト：ナシ(グループ化された予定を、作業者リスト：ナシ&&作業日リスト：ナシのグループ化しない予定に変更)");

			Schedule addSchedule = new Schedule();

			// 定番
			addSchedule.setGroupId(schedule.getGroupId());
			addSchedule.setCategoryId(schedule.getCategoryId());
			addSchedule.setStatusId(schedule.getStatusId());
			addSchedule.setSubjectLine(schedule.getSubjectLine());
			addSchedule.setContent(schedule.getContent());
			addSchedule.setMemo(schedule.getMemo());
			addSchedule.setCreatedBy(schedule.getCreatedBy());
			addSchedule.setUpdatedBy(schedule.getUpdatedBy());
			// 作業者
			addSchedule.setMemberId(memberList.get(0));
			addSchedule.setAddMemberId1st(memberList.get(0));
			// 作業日
			addSchedule.setMainDate(workDateList.get(0));
			addSchedule.setAddDate1st(workDateList.get(0));

			scheduleRepository.save(addSchedule);
			scheduleRepository.deleteByScheduleGroupId(schedule.getScheduleGroupId());

			// 3-2 既存予定の更新に対する処理 グループID：アリ、作業者リスト：アリ、作業日リスト：ナシ
			// (グループ化された予定を、作業者リスト：アリ&&作業日リスト：ナシのグループ化された予定に変更)
		} else if (StringUtils.isNotBlank(schedule.getScheduleGroupId()) && memberList.size() > 1 && workDateList.size() == 1) {

			System.out.println("3-2 既存予定の更新に対する処理 グループID：アリ、作業者リスト：アリ、作業日リスト：ナシ(グループ化された予定を、作業者リスト：アリ&&作業日リスト：ナシのグループ化された予定に変更)");

			// "scheduleGroupId"の用意
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

			// 作業者リストの人数分、Scheduleを生成
			for (int i = 0; i < memberList.size(); i++) {

				Schedule addSchedule = new Schedule();

        // 定番
				addSchedule.setGroupId(schedule.getGroupId());
				addSchedule.setCategoryId(schedule.getCategoryId());
				addSchedule.setStatusId(schedule.getStatusId());
				addSchedule.setSubjectLine(schedule.getSubjectLine());
				addSchedule.setContent(schedule.getContent());
				addSchedule.setMemo(schedule.getMemo());
				addSchedule.setCreatedBy(schedule.getCreatedBy());
				addSchedule.setUpdatedBy(schedule.getUpdatedBy());
				// グループIDの付与
				addSchedule.setScheduleGroupId(schedule.getMemberId() + dtformat.format(now));
				// 作業日
				addSchedule.setMainDate(workDateList.get(0));
				addSchedule.setAddDate1st(workDateList.get(0));
				// 作業者
				addSchedule.setMemberId(memberList.get(i));
				addSchedule.setMemberId(memberList.get(i));
				switch (memberList.size()) {
				case 2:
					addSchedule.setAddMemberId1st(memberList.get(0));
					addSchedule.setAddMemberId2nd(memberList.get(1));
					break;
				case 3:
					addSchedule.setAddMemberId1st(memberList.get(0));
					addSchedule.setAddMemberId2nd(memberList.get(1));
					addSchedule.setAddMemberId3rd(memberList.get(2));
					break;
				case 4:
					addSchedule.setAddMemberId1st(memberList.get(0));
					addSchedule.setAddMemberId2nd(memberList.get(1));
					addSchedule.setAddMemberId3rd(memberList.get(2));
					addSchedule.setAddMemberId4th(memberList.get(3));
					break;
				case 5:
					addSchedule.setAddMemberId1st(memberList.get(0));
					addSchedule.setAddMemberId2nd(memberList.get(1));
					addSchedule.setAddMemberId3rd(memberList.get(2));
					addSchedule.setAddMemberId4th(memberList.get(3));
					addSchedule.setAddMemberId5th(memberList.get(4));
					break;
				}
				scheduleRepository.save(addSchedule);
			}
			scheduleRepository.deleteByScheduleGroupId(schedule.getScheduleGroupId());

			// 3-3 既存予定の更新に対する処理 グループID：アリ、作業者リスト：ナシ、作業日リスト：アリ
			// (グループ化された予定を、作業者リスト：ナシ&&作業日リスト：アリのグループ化した予定に変更)
		} else if (StringUtils.isNotBlank(schedule.getScheduleGroupId()) && memberList.size() <= 1 && workDateList.size() > 1) {

			System.out.println("3-3 既存予定の更新に対する処理 グループID：アリ、作業者リスト：ナシ、作業日リスト：アリ(グループ化された予定を、作業者リスト：ナシ&&作業日リスト：アリのグループ化した予定に変更)");

			// "scheduleGroupId"の用意
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

			for (int i = 0; i < workDateList.size(); i++) {

				Schedule addSchedule = new Schedule();

        // 定番
				addSchedule.setGroupId(schedule.getGroupId());
				addSchedule.setCategoryId(schedule.getCategoryId());
				addSchedule.setStatusId(schedule.getStatusId());
				addSchedule.setSubjectLine(schedule.getSubjectLine());
				addSchedule.setContent(schedule.getContent());
				addSchedule.setMemo(schedule.getMemo());
				addSchedule.setCreatedBy(schedule.getCreatedBy());
				addSchedule.setUpdatedBy(schedule.getUpdatedBy());
				// グループIDの付与
				addSchedule.setScheduleGroupId(schedule.getMemberId() + dtformat.format(now));
				// 作業者
				addSchedule.setMemberId(memberList.get(0));
				addSchedule.setAddMemberId1st(memberList.get(0));
				// 作業日リスト
				addSchedule.setMainDate(workDateList.get(i));
				switch (workDateList.size()) {
				case 2:
					addSchedule.setAddDate1st(workDateList.get(0));
					addSchedule.setAddDate2nd(workDateList.get(1));
					break;
				case 3:
					addSchedule.setAddDate1st(workDateList.get(0));
					addSchedule.setAddDate2nd(workDateList.get(1));
					addSchedule.setAddDate3rd(workDateList.get(2));
					break;
				case 4:
					addSchedule.setAddDate1st(workDateList.get(0));
					addSchedule.setAddDate2nd(workDateList.get(1));
					addSchedule.setAddDate3rd(workDateList.get(2));
					addSchedule.setAddDate4th(workDateList.get(3));
					break;
				case 5:
					addSchedule.setAddDate1st(workDateList.get(0));
					addSchedule.setAddDate2nd(workDateList.get(1));
					addSchedule.setAddDate3rd(workDateList.get(2));
					addSchedule.setAddDate4th(workDateList.get(3));
					addSchedule.setAddDate5th(workDateList.get(4));
					break;
				}
				scheduleRepository.save(addSchedule);
			}
			scheduleRepository.deleteByScheduleGroupId(schedule.getScheduleGroupId());

			// 既存予定の更新に対する処理 グループID：アリ、作業者リスト：アリ、作業日リスト：アリ
			// (グループ化された予定を、作業者リスト：アリ&&作業日リスト：アリのグループ化した予定に変更)
		} else if (StringUtils.isNotBlank(schedule.getScheduleGroupId()) && memberList.size() > 1 && workDateList.size() > 1) {

			System.out.println("2-4 既存予定の更新　グループID：アリ、作業者リスト：アリ、作業日リスト：アリ (グループ化された予定を、作業者リスト：アリ&&作業日リスト：アリのグループ化された予定に変更)");

			// "scheduleGroupId"の用意
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

			// 作業者リストの人数分、回す
			for (int i = 0; i < memberList.size(); i++) {

				// 作業日リストの日数分、回す
				for (int j = 0; j < workDateList.size(); j++) {

					Schedule addSchedule = new Schedule();

					// 定番
					addSchedule.setGroupId(schedule.getGroupId());
          addSchedule.setCategoryId(schedule.getCategoryId());
					addSchedule.setStatusId(schedule.getStatusId());
					addSchedule.setSubjectLine(schedule.getSubjectLine());
					addSchedule.setContent(schedule.getContent());
					addSchedule.setMemo(schedule.getMemo());
					addSchedule.setCreatedBy(schedule.getCreatedBy());
					addSchedule.setUpdatedBy(schedule.getUpdatedBy());
					// グループIDの付与
					addSchedule.setScheduleGroupId(schedule.getMemberId() + dtformat.format(now));
					// 作業者
					addSchedule.setMemberId(memberList.get(i));
					switch (memberList.size()) {
					case 2:
						addSchedule.setAddMemberId1st(memberList.get(0));
						addSchedule.setAddMemberId2nd(memberList.get(1));
						break;
					case 3:
						addSchedule.setAddMemberId1st(memberList.get(0));
						addSchedule.setAddMemberId2nd(memberList.get(1));
						addSchedule.setAddMemberId3rd(memberList.get(2));
						break;
					case 4:
						addSchedule.setAddMemberId1st(memberList.get(0));
						addSchedule.setAddMemberId2nd(memberList.get(1));
						addSchedule.setAddMemberId3rd(memberList.get(2));
						addSchedule.setAddMemberId4th(memberList.get(3));
						break;
					case 5:
						addSchedule.setAddMemberId1st(memberList.get(0));
						addSchedule.setAddMemberId2nd(memberList.get(1));
						addSchedule.setAddMemberId3rd(memberList.get(2));
						addSchedule.setAddMemberId4th(memberList.get(3));
						addSchedule.setAddMemberId5th(memberList.get(4));
						break;
					}
					// 作業日
					addSchedule.setMainDate(workDateList.get(j));
					switch (workDateList.size()) {
					case 2:
						addSchedule.setAddDate1st(workDateList.get(0));
						addSchedule.setAddDate2nd(workDateList.get(1));
						break;
					case 3:
						addSchedule.setAddDate1st(workDateList.get(0));
						addSchedule.setAddDate2nd(workDateList.get(1));
						addSchedule.setAddDate3rd(workDateList.get(2));
						break;
					case 4:
						addSchedule.setAddDate1st(workDateList.get(0));
						addSchedule.setAddDate2nd(workDateList.get(1));
						addSchedule.setAddDate3rd(workDateList.get(2));
						addSchedule.setAddDate4th(workDateList.get(3));
						break;
					case 5:
						addSchedule.setAddDate1st(workDateList.get(0));
						addSchedule.setAddDate2nd(workDateList.get(1));
						addSchedule.setAddDate3rd(workDateList.get(2));
						addSchedule.setAddDate4th(workDateList.get(3));
						addSchedule.setAddDate5th(workDateList.get(4));
						break;
					}
					scheduleRepository.save(addSchedule);
				}
			}
			scheduleRepository.deleteByScheduleGroupId(schedule.getScheduleGroupId());
		}
	}
}
