package com.vince.retailmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaAuditing
public class RetailManagerApplication {
//
//	@Bean
//	public Validator validator() {
//		return new LocalValidatorFactoryBean();
//	}
//
//	@Bean
//	public MethodValidationPostProcessor methodValidationPostProcessor() {
//		MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
//		methodValidationPostProcessor.setValidator(validator());
//		return methodValidationPostProcessor;
//	}

	public static void main(String[] args) {
		SpringApplication.run(RetailManagerApplication.class, args);
	}


}
