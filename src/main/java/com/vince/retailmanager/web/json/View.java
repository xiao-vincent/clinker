package com.vince.retailmanager.web.json;

/**
 * JSON views for case by case deserializing
 */
public class View {

  public interface Public {

  }

  public interface Summary extends Public {

  }

  /*
  companies
   */
  public interface Company extends Summary {

  }

  public interface Franchisor extends Company {

  }

  public interface Franchisee extends Company {

  }

  public interface Invoice extends Summary {

  }

  public interface Payment extends Summary {

  }

  public interface PercentageFee extends Summary {

  }

  public interface Financials extends Summary {

  }
}
