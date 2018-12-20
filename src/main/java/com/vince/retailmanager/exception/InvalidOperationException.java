package com.vince.retailmanager.exception;

public class InvalidOperationException extends IllegalStateException {

  private final String objectName;
  private final Object invalidValue;

  public InvalidOperationException(String objectName, Object invalidValue) {
    super();
    this.objectName = objectName;
    this.invalidValue = invalidValue;
  }

  public InvalidOperationException(String message, Throwable cause, String objectName,
      Object invalidValue) {
    super(message, cause);
    this.objectName = objectName;
    this.invalidValue = invalidValue;
  }

  public InvalidOperationException(String message, String objectName, Object invalidValue) {
    super(message);
    this.objectName = objectName;
    this.invalidValue = invalidValue;
  }


  public InvalidOperationException(Throwable cause, String objectName, Object invalidValue) {
    super(cause);
    this.objectName = objectName;
    this.invalidValue = invalidValue;
  }


  public Object getInvalidValue() {
    return invalidValue;
  }

  public String getObjectName() {
    return objectName;
  }
}
