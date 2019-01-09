package com.vince.retailmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.model.View;
import com.vince.retailmanager.model.View.Public;
import com.vince.retailmanager.model.View.Summary;
import com.vince.retailmanager.web.controller.validator.ValidFranchisor;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "franchisors")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidFranchisor
public class Franchisor extends Company {

  @Column(unique = true)
  @NotNull
  @Size(min = 3, max = 35)
  @JsonView(Public.class)
  private String name;

  @Column(unique = true)
  @NotNull
  @Size(min = 2)
  @JsonView(Public.class)
  private String website;

  @NotNull
  @Size(min = 3, max = 280)
  @JsonView(Summary.class)
  private String description;

  @OneToMany(mappedBy = "franchisor", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
      CascadeType.REFRESH, CascadeType.DETACH})
  @JsonIgnore
  @Builder.Default
  private Set<Franchisee> franchisees = new HashSet<>();

  @NotNull
  @Min(0)
  @JsonView(View.Franchisor.class)
  private double liquidCapitalRequirement;

  @NotNull
  @Min(0)
  @JsonView(View.Franchisor.class)
  private double franchiseFee;

  @NotNull
  @Range(min = 0, max = 1)
  @JsonView(View.Franchisor.class)
  private double royaltyFeePercent;

  @NotNull
  @Range(min = 0, max = 1)
  @JsonView(View.Franchisor.class)
  private double marketingFeePercent;

  @NotNull
  @Range(min = 1, max = 365)
  @Builder.Default
  @JsonView(View.Franchisor.class)
  private int feeFrequency = 12; //per year

  public void addFranchisee(Franchisee franchisee) {
    franchisees.add(franchisee);
    franchisee.setFranchisor(this);
  }

//  @Override
//  @JsonView(View.Franchisor.class)
//  public BigDecimal getCashBalance() {
//    return super.getCashBalance();
//  }
}
