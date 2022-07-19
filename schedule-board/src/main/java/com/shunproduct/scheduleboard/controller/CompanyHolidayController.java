package com.shunproduct.scheduleboard.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shunproduct.scheduleboard.entity.CompanyHoliday;
import com.shunproduct.scheduleboard.repository.CompanyHolidayRepository;
import com.shunproduct.scheduleboard.service.CompanyHolidaySaveService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CompanyHolidayController {
	
	private final CompanyHolidayRepository companyHolidayRepository;
	private final CompanyHolidaySaveService companyHolidaySaveService;
	
	@GetMapping("/admin/holiday-setting")
	public String getShowCompanyHoliday(@ModelAttribute("companyHoliday") CompanyHoliday companyHoliday, Model model) {

		model.addAttribute("holidays", companyHolidayRepository.findAllOrderByCalendarDateASC());

		return "holiday-setting";
	}
	
	@PostMapping("/admin/holiday-setting")
	public String postShowCompanyHoliday(@RequestParam String calendarFy, @ModelAttribute("companyHoliday") CompanyHoliday companyHoliday, Model model) {
		
		if(calendarFy.equals("all")) {
			model.addAttribute("holidays", companyHolidayRepository.findAllOrderByCalendarDateASC());
		}else {
			model.addAttribute("holidays", companyHolidayRepository.findByFy(Integer.parseInt(calendarFy)));	
		}
		
		return "holiday-setting";
	}
	
	@GetMapping("/delete-holiday/{id}")
	public String deleteCompanyHoliday(@PathVariable Long id) {
		
		companyHolidayRepository.deleteById(id);
		companyHolidayRepository.updateCalendarMaster();
		
		return "redirect:/admin/holiday-setting";
	}
	
	@PostMapping("/add-holiday")
	public String addCompanyHoliday(@ModelAttribute("companyHoliday") CompanyHoliday companyHoliday, Model model,
			@RequestParam(required = false) String addCompanyHoliday1st,
			@RequestParam(required = false) String addCompanyHoliday2nd,
			@RequestParam(required = false) String addCompanyHoliday3rd,
			@RequestParam(required = false) String addCompanyHoliday4th,
			@RequestParam(required = false) String addCompanyHoliday5th) {
		
		List<String> inputStrDateList = Arrays.<String>asList(
				addCompanyHoliday1st, addCompanyHoliday2nd, addCompanyHoliday3rd,addCompanyHoliday4th,addCompanyHoliday5th);
		
		companyHolidaySaveService.saveHolidays(inputStrDateList);
		companyHolidayRepository.updateCalendarMaster();
		
		return "redirect:/admin/holiday-setting";
	}

}
