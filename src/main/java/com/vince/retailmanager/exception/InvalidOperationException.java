package com.vince.retailmanager.exception;

public class InvalidOperationException extends IllegalStateException {

  private Object detail;

  public InvalidOperationException(String message) {
    super(message);
  }


  public InvalidOperationException(String message, Object detail) {
    super(message);
    this.detail = detail;
  }

  public InvalidOperationException(String message, Throwable cause,
      Object detail) {
    super(message, cause);
    this.detail = detail;
  }

  public Object getDetail() {
    return detail;
  }

}
