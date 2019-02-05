package com.vince.retailmanager.model.entity.companies;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.model.entity.BaseEntity;
import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import com.vince.retailmanager.web.json.View;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Base abstract class for companies.
 *
 * @author Vincent Xiao
 */
@Entity
/*
 Using the joined table approach maps each class of the inheritance hierarchy to its own database table. This abstract superclass gets mapped to a database table. This table contains all shared entity attributes.
 */
@Table(name = "companies")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = true, of = {""})
public abstract class Company extends BaseEntity {

  @JsonIgnore
  private boolean enabled = true;

  @JsonView(View.Summary.class)
  private String type = getClass().getSimpleName();

  @Column
  @JsonView(View.Financials.class)
  private BigDecimal cashBalance = BigDecimal.valueOf(0);

  @OneToMany(mappedBy = "company")
  @JsonIgnore
  private Set<IncomeStatement> incomeStatements = new HashSet<>();


  /**
   * Sets the bidirectional one-to-many relationship and adjusts the company's cash balance by the
   * income statement's net income
   *
   * @param incomeStatement the income statement reported by the company
   */
  public void addIncomeStatement(IncomeStatement incomeStatement) {
    incomeStatements.add(incomeStatement);
    incomeStatement.setCompany(this);
    setCashBalance(
        this.getCashBalance()
            .add(incomeStatement.getNetIncome())
    );
  }

}
