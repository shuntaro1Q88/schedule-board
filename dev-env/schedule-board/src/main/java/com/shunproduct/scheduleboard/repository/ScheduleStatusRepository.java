package com.shunproduct.scheduleboard.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.shunproduct.scheduleboard.entity.ScheduleStatus;

public interface ScheduleStatusRepository extends PagingAndSortingRepository<ScheduleStatus, Integer> {

}
