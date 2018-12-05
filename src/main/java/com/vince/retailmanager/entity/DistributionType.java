package com.vince.retailmanager.entity;

public enum DistributionType {
	RECEIVED("received"),
	SENT("sent");

	private final String name;

	DistributionType(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public static boolean contains(String test) {
		for (DistributionType type : DistributionType.values()) {
			if (type.name().equalsIgnoreCase(test)) {
				return true;
			}
		}

		return false;
	}
}
