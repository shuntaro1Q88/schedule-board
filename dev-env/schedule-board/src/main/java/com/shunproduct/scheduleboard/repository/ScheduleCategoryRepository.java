package com.shunproduct.scheduleboard.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shunproduct.scheduleboard.entity.ScheduleCategory;

@Repository
public interface ScheduleCategoryRepository extends PagingAndSortingRepository<ScheduleCategory, Integer> {

}
