package com.vince.retailmanager.model.entity.companies;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.model.entity.BaseEntity;
import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import com.vince.retailmanager.model.entity.transactions.Invoice;
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

@Entity
@Table(name = "companies")
/*
 Using the joined table approach maps each class of the inheritance hierarchy to its own database table. This abstract superclass gets mapped to a database table. This table contains columns for all shared entity attributes.
 */
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = true, of = {""})
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//@JsonIdentityReference
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

  public void addInvoiceReceived(Invoice invoice) {
    invoice.setRecipient(this);
  }

  public void addInvoiceSent(Invoice invoice) {
    invoice.setSender(this);
  }

  public void addIncomeStatement(IncomeStatement incomeStatement) {
    incomeStatements.add(incomeStatement);
    incomeStatement.setCompany(this);
    setCashBalance(
        this.getCashBalance()
            .add(incomeStatement.getNetIncome())
    );
  }

}
