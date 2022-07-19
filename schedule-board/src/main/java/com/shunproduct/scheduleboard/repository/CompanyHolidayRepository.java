package com.shunproduct.scheduleboard.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.shunproduct.scheduleboard.entity.CompanyHoliday;

public interface CompanyHolidayRepository extends CrudRepository<CompanyHoliday, Long> {
	
	// 年度から会社休日を取得する 
	// デバッグ用
	// SELECT * FROM company_holidays
	// WHERE CASE WHEN EXTRACT(MONTH FROM calendar_date) < 4 
	// THEN EXTRACT(YEAR FROM calendar_date) -1 ELSE EXTRACT(YEAR FROM calendar_date) END = 2022
	@Query("SELECT * FROM company_holidays\r\n"
			+ "WHERE (CASE WHEN EXTRACT(MONTH FROM calendar_date) < 4 \r\n"
			+ "THEN EXTRACT(YEAR FROM calendar_date) -1 ELSE EXTRACT(YEAR FROM calendar_date) END)=:fy\r\n"
			+ "ORDER BY calendar_date ASC")
	List<CompanyHoliday> findByFy(int fy);
	
	@Query("SELECT * FROM company_holidays ORDER BY calendar_date ASC")
	List<CompanyHoliday> findAllOrderByCalendarDateASC();
	
	// デバック用
	// DROP TABLE IF EXISTS company_calendars CASCADE;
	// CREATE TABLE company_calendars AS(
	//   WITH calendar_tb AS(
    //	     SELECT
    //	     GENERATE_SERIES('2021-04-01', CURRENT_DATE+365*15, '1 day')::DATE AS calendar_date
    //	     ,date_part('dow', GENERATE_SERIES('2021-04-01', CURRENT_DATE+365*15, '1 day')) AS dow_index
	//   )
	//   SELECT
    //	     calendar_tb.calendar_date
    //	     ,CASE WHEN company_holidays.dow_index ISNULL THEN calendar_tb.dow_index
    //	       ELSE company_holidays.dow_index END AS dow_index
	//   FROM calendar_tb
	//   LEFT JOIN company_holidays ON
    //	     calendar_tb.calendar_date = company_holidays.calendar_date
	//   ORDER BY calendar_tb.calendar_date
	// );
	@Modifying
	@Query("DROP TABLE IF EXISTS company_calendars CASCADE;\r\n"
			+ "CREATE TABLE company_calendars AS(\r\n"
			+ "  WITH calendar_tb AS(\r\n"
			+ "    SELECT\r\n"
			+ "    GENERATE_SERIES('2021-04-01', CURRENT_DATE+365*15, '1 day')::DATE AS calendar_date\r\n"
			+ "    ,date_part('dow', GENERATE_SERIES('2021-04-01', CURRENT_DATE+365*15, '1 day')) AS dow_index\r\n"
			+ "  )\r\n"
			+ "  SELECT\r\n"
			+ "    calendar_tb.calendar_date\r\n"
			+ "    ,CASE WHEN company_holidays.dow_index ISNULL THEN calendar_tb.dow_index\r\n"
			+ "      ELSE company_holidays.dow_index END AS dow_index\r\n"
			+ "  FROM calendar_tb\r\n"
			+ "  LEFT JOIN company_holidays ON\r\n"
			+ "    calendar_tb.calendar_date = company_holidays.calendar_date\r\n"
			+ "  ORDER BY calendar_tb.calendar_date\r\n"
			+ ");")
	void updateCalendarMaster();
	
}
