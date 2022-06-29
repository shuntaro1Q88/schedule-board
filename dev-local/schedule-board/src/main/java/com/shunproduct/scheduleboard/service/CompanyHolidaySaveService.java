package com.shunproduct.scheduleboard.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shunproduct.scheduleboard.entity.CompanyHoliday;
import com.shunproduct.scheduleboard.repository.CompanyHolidayRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CompanyHolidaySaveService {
	
	private final CompanyHolidayRepository companyHolidayRepository;
	
	// 入力された日付がYYYY-MM-DD形式か判定
	public Boolean checkDateFormate(String targetDate) {
		
		/**
		 * \\d{4}   -->数字4個
		 * \\d{2}   -->数字2個
		 * \\-      -->-
		 */
		String regex = "\\d{4}\\-\\d{2}\\-\\d{2}";
		
		return targetDate.matches(regex);
	}
	
	public void saveHolidays(List<String> inputStrDateList) {

		for (int i = 0; i < inputStrDateList.size(); i++) {

			if (checkDateFormate(inputStrDateList.get(i))) {

				CompanyHoliday companyHoliday = new CompanyHoliday();

				companyHoliday.setCalendarDate(
						LocalDate.parse(inputStrDateList.get(i), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				companyHoliday.setDowIndex(7);

				companyHolidayRepository.save(companyHoliday);
				
				
			} else {
				// ToDo エラー処理
				System.out.println("不正な値");
			}
		}
	}
}
