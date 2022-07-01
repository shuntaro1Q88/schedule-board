package com.shunproduct.scheduleboard.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shunproduct.scheduleboard.entity.CompanyCalendar;

@Repository
public interface CompanyCalendarRepository extends CrudRepository<CompanyCalendar, LocalDate> {

	// 日付の平日or休日情報の取得
	// 参照するテーブルはcompany_calendarsなので注意
	@Query("SELECT\r\n"
			+ "  CASE WHEN calendar_date = CURRENT_DATE THEN CONCAT(TO_CHAR(calendar_date,'MM')::INTEGER,'-',TO_CHAR(calendar_date,'DD')::INTEGER,',',8) \r\n"
			+ "  ELSE CONCAT(TO_CHAR(calendar_date,'MM')::INTEGER,'-',TO_CHAR(calendar_date,'DD')::INTEGER,',',dow_index) END\r\n"
			+ "FROM company_calendars\r\n"
			+ "WHERE calendar_date BETWEEN :startDate AND :endDate")
	List<String> findByMainDateBetween(LocalDate startDate, LocalDate endDate);

}
