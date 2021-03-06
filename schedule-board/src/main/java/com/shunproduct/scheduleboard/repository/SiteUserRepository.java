package com.shunproduct.scheduleboard.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.shunproduct.scheduleboard.entity.SiteUser;

public interface SiteUserRepository extends PagingAndSortingRepository<SiteUser, Long> {
	
	/* 
	 * @param username 6桁社員番号
	 * 
	 */
	
	// ユーザ登録時、自作validationで使用
	SiteUser findByUsername(String username);
	
    // ユーザ一覧でデータ抽出
	// グループ名のみ選択時
	List<SiteUser> findAllByGroupId(int groupId);
	// 苗字のみ入力時
	List<SiteUser> findAllByFamilyNameContaining(String familyName);
	// グループ名、苗字、両方入力時
	List<SiteUser> findAllByGroupIdAndFamilyNameContaining(int groupId, String familyName);

}
