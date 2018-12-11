package com.vince.retailmanager.web.controller.utils;

import com.vince.retailmanager.entity.AccessToken;
import com.vince.retailmanager.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ControllerUtils {

	public static void addActiveUsername(
		 Model model,
		 @AuthenticationPrincipal User authenticatedUser,
		 Integer id,
		 UserService userService
	) {
		String authenticatedUsername = authenticatedUser.getUsername();

		AccessToken accessToken = userService.findAccessToken(authenticatedUsername, id);
		if (id != null && accessToken != null) {
			model.addAttribute("activeUsername", accessToken.getUser().getUsername());
		}
	}

	public static void validate(Validator validator, Object object, Class<?>... groups) {
		Set<ConstraintViolation<Object>> violations = validator.validate(object, groups);
		System.out.println(violations);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}
}
