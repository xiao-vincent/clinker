package com.vince.retailmanager.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides an endpoint to test the server connection
 */
@RestController
//@RequestMapping("/{franchisorId}/franchisees/{franchiseeId}")
public class PingController {

  @GetMapping("/ping")
  public ResponseEntity<HttpStatus> ping() {
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
