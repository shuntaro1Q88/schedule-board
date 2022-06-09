package com.shunproduct.scheduleboard.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.shunproduct.scheduleboard.entity.SiteUser;

public interface SiteUserRepository extends PagingAndSortingRepository<SiteUser, Long> {
	
	/* 
	 * @param username 6桁社員番号
	 * 
	 */
	
	// ユーザ登録時、自作validationで使用
	SiteUser findByUsername(String username);

}
