package com.shunproduct.scheduleboard.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("schedule_categories")
@Data
public class ScheduleCategory {

	@Column("category_id")
	@Id
	private int categoryId;

	// 作業区分名称
	@Column("category_name")
	@NotBlank
	private String categoryName;

	// 背景色
	@Column("category_bg_color")
	private String categoryBgColor;

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
