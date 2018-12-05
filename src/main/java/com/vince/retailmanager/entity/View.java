package com.vince.retailmanager.entity;

public class View {
	public interface Public {
	}

	public interface Summary extends Public {
	}

	public interface Franchisor extends Summary {
	}

	public interface Franchisee extends Summary {
	}

	public interface Invoice extends Summary {
	}

	public interface Payment extends Summary {
	}


}
