package com.shunproduct.scheduleboard.entity;

import java.time.LocalDate;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Table("company_calendars")
@Data
public class CompanyCalendar {

	@Column("calendar_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate calendarDate;

	@Column("dow_index")
	private int dowIndex;

}
