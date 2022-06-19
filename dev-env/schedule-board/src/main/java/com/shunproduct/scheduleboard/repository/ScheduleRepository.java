package com.shunproduct.scheduleboard.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.shunproduct.scheduleboard.entity.Schedule;

public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
	
	// スケジュール画面表示データの取得
	/* -- デバッグ用
	 * WIth target_tb AS(
	 *   SELECT schedules.* FROM schedules
	 *   LEFT JOIN schedule_categories ON schedules.category_id =schedule_categories.category_id
	 *   LEFT JOIN groups ON schedules.group_id = groups.group_id
	 *   LEFT JOIN schedule_statuses ON schedules.status_id = schedule_statuses.status_id
	 *   WHERE groups.group_id = 1 AND main_date BETWEEN '2022-06-01' AND '2022-06-30' -- 変数設定
	 *   ORDER BY schedule_categories.display_order ASC, schedule_statuses.display_order DESC
	 * )
	 * SELECT target_tb.* FROM company_calendars
	 * LEFT JOIN target_tb ON company_calendars.calendar_date = target_tb.main_date
	 * WHERE target_tb.main_date BETWEEN '2022-06-01' AND '2022-06-30' -- 変数設定
	 */
	@Query("WIth target_tb AS(\r\n"
			+ "  SELECT schedules.* FROM schedules\r\n"
			+ "  LEFT JOIN schedule_categories ON schedules.category_id =schedule_categories.category_id\r\n"
			+ "  LEFT JOIN groups ON schedules.group_id = groups.group_id\r\n"
			+ "  LEFT JOIN schedule_statuses ON schedules.status_id = schedule_statuses.status_id\r\n"
			+ "  WHERE groups.group_id =:groupId AND main_date BETWEEN :startDate AND :endDate -- 変数設定\r\n"
			+ "  ORDER BY schedule_categories.display_order ASC, schedule_statuses.display_order DESC\r\n"
			+ ")\r\n"
			+ "SELECT target_tb.* FROM company_calendars\r\n"
			+ "LEFT JOIN target_tb ON company_calendars.calendar_date = target_tb.main_date\r\n"
			+ "WHERE target_tb.main_date BETWEEN :startDate AND :endDate -- 変数設定")
	List<Schedule> findByGroupIdAndMainDateBetween(int groupId, LocalDate startDate, LocalDate endDate);

}