package com.shunproduct.scheduleboard.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.shunproduct.scheduleboard.entity.CompanyCalendar;

public interface CompanyCalendarRepository extends CrudRepository<CompanyCalendar, LocalDate> {

	// 日付の平日or休日情報の取得
	// 参照するテーブルはcompany_calendarsなので注意
	@Query("SELECT CONCAT(calendar_date,',',dow_index) FROM company_calendars WHERE calendar_date BETWEEN :startDate AND :endDate")
	List<String> findByMainDateBetween(LocalDate startDate, LocalDate endDate);

}
