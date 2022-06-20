package com.shunproduct.scheduleboard.entity;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Table("schedules")
@Data
public class Schedule {
	
	@Id@Column("schedule_id")
	private Long scheduleId;

	@Column("group_id")
	private int groupId;
	
	@Column("member_id")
	@NotBlank
	@Pattern(regexp = "[0-9-]{6}")
	private String memberId;

	@NotBlank
	@Column("subject_line")
	private String subjectLine;
	
	@Column("main_date")
	@DateTimeFormat( pattern = "yyyy-MM-dd")
	private LocalDate mainDate;
	
	@Column("status_id")
	private int statusId;
	
	@Column("category_id")
	private int categoryId;
	
	@Column("content")
	private String content;

	@Column("memo")
	private String memo;
	
	@Column("schedule_group_id")
	private String scheduleGroupId;

	@Column("add_member_id_1st")
	private String addMemberId1st;

	@Column("add_member_id_2nd")
	private String addMemberId2nd;

	@Column("add_member_id_3rd")
	private String addMemberId3rd;
	
	@Column("add_member_id_4th")
	private String addMemberId4th;
	
	@Column("add_member_id_5th")
	private String addMemberId5th;
	
	@Column("add_date_1st")
	@DateTimeFormat( pattern = "yyyy-MM-dd")
	private LocalDate addDate1st;
	
	@Column("add_date_2nd")
	@DateTimeFormat( pattern = "yyyy-MM-dd")
	private LocalDate addDate2nd;
	
	@Column("add_date_3rd")
	@DateTimeFormat( pattern = "yyyy-MM-dd")
	private LocalDate addDate3rd;
	
	@Column("add_date_4th")
	@DateTimeFormat( pattern = "yyyy-MM-dd")
	private LocalDate addDate4th;
	
	@Column("add_date_5th")
	@DateTimeFormat( pattern = "yyyy-MM-dd")
	private LocalDate addDate5th;
	
	@Column("created_by")
	private String createdBy;
	
	@Column("updated_by")
	private String updatedBy;

}
