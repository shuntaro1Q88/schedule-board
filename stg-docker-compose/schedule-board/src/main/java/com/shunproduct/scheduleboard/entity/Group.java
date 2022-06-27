package com.shunproduct.scheduleboard.entity;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("groups")
@Data
public class Group {
	
	@Id
	@Column("group_id")
	@NotBlank
	private int groupId;
	
	// グループ名
	@Column("group_name")
	@NotBlank
	private String groupName;
	
	// 表示用フラグ
	@Column("display_flag")
	@NotBlank
	private boolean displayFlag;
	
	// 表示順
	@Column("display_order")
	@NotBlank
	private int displayOrder;
	
}
