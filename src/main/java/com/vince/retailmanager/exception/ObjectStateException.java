package com.vince.retailmanager.exception;

public class ObjectStateException extends RuntimeException {
	private final String objectName;
	private final Object invalidValue;

	public ObjectStateException(String objectName, Object invalidValue) {
		super();
		this.objectName = objectName;
		this.invalidValue = invalidValue;
	}

	public ObjectStateException(String message, Throwable cause, String objectName, Object invalidValue) {
		super(message, cause);
		this.objectName = objectName;
		this.invalidValue = invalidValue;
	}

	public ObjectStateException(String message, String objectName, Object invalidValue) {
		super(message);
		this.objectName = objectName;
		this.invalidValue = invalidValue;
	}


	public ObjectStateException(Throwable cause, String objectName, Object invalidValue) {
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
