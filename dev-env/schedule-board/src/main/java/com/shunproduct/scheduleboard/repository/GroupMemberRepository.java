package com.shunproduct.scheduleboard.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.shunproduct.scheduleboard.entity.GroupMember;

public interface GroupMemberRepository extends PagingAndSortingRepository<GroupMember, Long> {
	
    // ユーザ一覧でデータ抽出
	// グループ名のみ選択時
	List<GroupMember> findAllByGroupId(int groupId);
	// 苗字のみ入力時
	List<GroupMember> findAllByFamilyNameContaining(String familyName);
	// グループ名、苗字、両方入力時
	List<GroupMember> findAllByGroupIdAndFamilyNameContaining(int groupId, String familyName);

}
