package com.shunproduct.scheduleboard.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Table("company_holidays")
@Data


/* 
 * 土日を除く、休日を設定するためのEntiryクラス
 * (会社稼働日とカレンダーを合わせるため）
 * @param dowIndex ここでは、会社休日として、7を代入する
 */
public class CompanyHoliday {
	
	@Column("id")
	@Id
	private Long id;
	
	@Column("calendar_date")
	@DateTimeFormat( pattern = "yyyy-MM-dd")
	private LocalDate calendarDate;
	
	@Column("dow_index")
	private int dowIndex = 7;

}
