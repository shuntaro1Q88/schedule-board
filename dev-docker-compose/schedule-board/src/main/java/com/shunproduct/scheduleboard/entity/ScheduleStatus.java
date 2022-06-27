package com.shunproduct.scheduleboard.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("schedule_statuses")
@Data
public class ScheduleStatus {

	@Column("status_id")
	@Id
	private int statusId;

	// 記号
	@Column("status_symbol")
	@NotBlank
	private String statusSymbol;

	// 名称
	@Column("status_name")
	private String statusName;

	// 表示フラグ
	@Column("display_flag")
	@NotNull(message = "選択してください")
	private Boolean displayFlag;

	// 表示順
	@Column("display_order")
	@Min(1)
	@Max(999)
	private int displayOrder;

}
