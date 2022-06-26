package com.shunproduct.scheduleboard.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shunproduct.scheduleboard.entity.GroupMember;

@Repository
public interface GroupMemberRepository extends PagingAndSortingRepository<GroupMember, Long> {
	
    // ユーザ一覧でデータ抽出
	// グループ名のみ選択時
	List<GroupMember> findAllByGroupId(int groupId);
	// 苗字のみ入力時
	List<GroupMember> findAllByFamilyNameContaining(String familyName);
	// グループ名、苗字、両方入力時
	List<GroupMember> findAllByGroupIdAndFamilyNameContaining(int groupId, String familyName);
	
	// スケジュール表示に使用
	@Query("SELECT * FROM group_members\r\n"
			+ "WHERE display_flag is TRUE AND group_id =:groupId\r\n"
			+ "-- WHERE display_flag is TRUE AND group_id = 1 -- デバッグ用\r\n"
			+ "ORDER BY display_order")
	List<GroupMember> findByDisplayFlagIsTrueAndGroupIdOrderByDisplayOrder(int groupId);
	
	// Schedule保存時に使用
	List<GroupMember> findByGroupIdAndMemberId(int groupId, String memberId);

}
