package com.shunproduct.scheduleboard.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class ScheduleDisplayParam implements Serializable {

	private static final long serialVersionUID = 1L;

	private int Id;

	private String startDate;

	private int displayTerm;

}
