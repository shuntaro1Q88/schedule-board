package com.shunproduct.scheduleboard.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shunproduct.scheduleboard.entity.ScheduleStatus;

@Repository
public interface ScheduleStatusRepository extends PagingAndSortingRepository<ScheduleStatus, Integer> {

	@Query("SELECT * FROM schedule_statuses ORDER BY display_order ASC")
	List<ScheduleStatus> findAllOrderByDisplayOrderAsc();

}
