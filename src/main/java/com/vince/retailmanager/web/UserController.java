package com.vince.retailmanager.web;

import com.vince.retailmanager.entity.User;
import com.vince.retailmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/users")
public class UserController {


	@Autowired
	private UserService userService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<User> addUser(@RequestBody @Valid User user, BindingResult bindingResult) throws Exception {
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if (bindingResult.hasErrors() || (user == null)) {
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<User>(user, headers, HttpStatus.BAD_REQUEST);
		}

		this.userService.saveUser(user);
		user.setPassword("");
		return new ResponseEntity<User>(user, headers, HttpStatus.CREATED);
	}

	@GetMapping("/test")
	public String getAuthenticationInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication);
		return authentication.toString();
	}
}
