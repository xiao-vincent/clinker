package com.vince.retailmanager.web;

import com.vince.retailmanager.entity.AccessToken;
import com.vince.retailmanager.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Component
class ControllerUtilities {

	static void addActiveUsername(
		 Model model,
		 @AuthenticationPrincipal User authenticatedUser,
		 @PathVariable(value = "id", required = false) Integer id,
		 UserService userService
	) {
		String authenticatedUsername = authenticatedUser.getUsername();

		AccessToken accessToken = userService.findAccessToken(authenticatedUsername, id);
		if (accessToken != null) {
			model.addAttribute("activeUsername", accessToken.getUser().getUsername());
		}
	}

	static void validate(Validator validator, Object object) {
		Set<ConstraintViolation<Object>> violations = validator.validate(object);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}
}
