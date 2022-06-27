package com.shunproduct.scheduleboard.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.shunproduct.scheduleboard.repository.SiteUserRepository;

public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin, String> {

	private final SiteUserRepository userRepository;

	public UniqueLoginValidator() {
		this.userRepository = null;
	}

	@Autowired
	public UniqueLoginValidator(SiteUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return userRepository == null || userRepository.findByUsername(value) == null;
	}
}
