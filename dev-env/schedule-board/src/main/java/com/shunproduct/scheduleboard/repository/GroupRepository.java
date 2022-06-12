package com.shunproduct.scheduleboard.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.shunproduct.scheduleboard.entity.Group;

public interface GroupRepository extends PagingAndSortingRepository<Group, Long> {
	
	// グループ名選択のプルダウンで使用
	List<Group> findByDisplayFlagIsTrueOrderByDisplayOrderAsc();
	
	// groupMapのキー(groupId)からグループ名を取得するときに使用
	Group findByGroupId(int groupId);

}
