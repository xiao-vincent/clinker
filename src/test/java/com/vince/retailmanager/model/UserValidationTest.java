package com.vince.retailmanager.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.vince.retailmanager.model.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class UserValidationTest {

  private static ValidatorFactory validatorFactory;
  private static Validator validator;
  private List<User> users;

  @BeforeAll
  public static void beforeAll() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }


  @AfterAll
  static void afterAll() {
    validatorFactory.close();
  }

  @BeforeEach
  void setUp() {
    users = new ArrayList<>();
    User user = new User();
    user.setUsername("user2001");
    user.setPassword("validPassword");
    users.add(user);

  }

  @Test
  void shouldHaveNoViolations() {
    User user = users.get(0);
    Set<ConstraintViolation<User>> violations
        = validator.validate(user);
    assertTrue(violations.isEmpty());
  }

  @ParameterizedTest(name = "[{0}] - input: {1}")
  @CsvSource({
      "null case, ,must not be null",
      "invalid characters, 'user_*2000',must contain only alphanumeric characters",
      "invalid min, lo, size must be between 3 and 18",
      "invalid max, " +
          "aaaaaaaaaaaaab20000000000000009, size must be between 3 and 18",
  })
  void checkInvalidUsernameInput(String testName, String input, String errMessage) {
    User user = users.get(0);
    user.setUsername(input);

    checkViolations(user, "username", input, errMessage);
  }


  @ParameterizedTest(name = "[{0}] - input: {1}")
  @CsvSource({
      "null case, ,must not be null",
      "invalid min, shortpw, size must be between 8 and 128",
  })
  void checkInvalidPasswordInput(String testName, String input, String errMessage) {
    User user = users.get(0);
    user.setPassword(input);

    checkViolations(user, "password", input, errMessage);
  }


  private <T> void checkViolations(T elem, String fieldName, String input, String errMsg) {
    //when:
    Set<ConstraintViolation<T>> violations = validator.validate(elem);

    //then:
    assertEquals(1, violations.size());

    ConstraintViolation<T> violation
        = violations.iterator().next();
    assertEquals(errMsg,
        violation.getMessage());
    assertEquals(fieldName, violation.getPropertyPath().toString());
    assertEquals(input, violation.getInvalidValue());
  }

//    //    @ParameterizedTest
//    @ParameterizedTest(name = "\"{0}\" should be {1} and {2}")
//    @CsvSource({"Hello, 5", "JUnit 5, 7", "'Hello, JUnit 5!', 15"})
//    void withCsvSource(String word, int length) {
//        System.out.println(length);
//    }

}