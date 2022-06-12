package com.shunproduct.scheduleboard.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.shunproduct.scheduleboard.validator.UniqueLogin;

import lombok.Data;

@Table("site_users")
@Data
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
	// 選択していない状態 = 0 を不可とする
	@Column("group_id")
	@Min(1)
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
