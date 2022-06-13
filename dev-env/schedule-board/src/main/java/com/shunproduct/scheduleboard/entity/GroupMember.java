package com.shunproduct.scheduleboard.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("group_members")
@Data
public class GroupMember {

	@Column("id")
	@Id
	private Long id;

	// 社員番号
	@Column("member_id")
	@NotBlank
	@Pattern(regexp = "[0-9-]{6}")
	private String memberId;

	// 所属
	@Column("group_id")
	@Min(1)
	@Max(99)
	private int groupId;

	// 姓
	@Column("family_name")
	@NotBlank
	private String familyName;

	// 名
	@Column("first_name")
	@NotBlank
	private String firstName;

	// 表示フラグ
	@Column("display_flag")
	private Boolean displayFlag;

	// 表示順
	@Column("display_order")
	@Min(0)
	@Max(999)
	private int displayOrder;

	// メモ
	@Column("memo")
	private String memo;

}
