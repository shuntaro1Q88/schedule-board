package com.shunproduct.scheduleboard.repository;

import org.springframework.data.repository.CrudRepository;

import com.shunproduct.scheduleboard.entity.Schedule;

public interface ScheduleRepository extends CrudRepository<Schedule, Long> {

}
