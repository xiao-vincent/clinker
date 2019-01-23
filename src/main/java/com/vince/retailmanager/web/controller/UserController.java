package com.vince.retailmanager.web.controller;

import com.vince.retailmanager.model.entity.authorization.User;
import com.vince.retailmanager.service.UserService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/users")
public class UserController {

  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;

  }

  @PostMapping
  public ResponseEntity<User> addUser(@RequestBody @Valid User user) {
    user = this.userService.saveUser(user);
    user.setPassword("");
    return new ResponseEntity<>(user, HttpStatus.CREATED);
  }

}
