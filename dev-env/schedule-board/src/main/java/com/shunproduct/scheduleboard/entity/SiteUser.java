package com.shunproduct.scheduleboard.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
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
	@Pattern(regexp = "[0-9-]{6}", message="半角数字6桁で入力してください")
	@UniqueLogin // 自作バリデーション
	private String username;

	// グループID
	// 選択していない状態 = 0 を不可とする
	@Column("group_id")
	@Range(min=1,max=99, message="選択してください")
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
	@Size(min = 8, max = 16, message="8～16文字で設定してください")
	private String password;

	// 権限
	@Column("role")
	private String role;

}
