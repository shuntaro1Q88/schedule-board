package com.shunproduct.scheduleboard.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import com.shunproduct.scheduleboard.validator.UniqueLogin;

public class SiteUser {
	
	@Id
	private Long id;

	// 社員番号(6桁)
	@Column("user_id")
	@NotBlank
	@Pattern(regexp = "[0-9-]{6}")
	@UniqueLogin // 自作バリデーション
	private String username;

	// グループID
	@Column("group_id")
	private int groupId;

	// 姓
	@Column("family_name")
	@NotBlank
	private String familyName;

	// 名
	@Column("first_name")
	@NotBlank
	private String firstName;

	// パスワード
	@Column("password")
	@Size(min = 8, max = 16)
	private String password;

	// 権限
	@Column("role")
	private String role;

}
