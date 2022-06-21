package com.shunproduct.scheduleboard.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.shunproduct.scheduleboard.entity.ScheduleCategory;

public interface ScheduleCategoryRepository extends PagingAndSortingRepository<ScheduleCategory, Integer> {

}
