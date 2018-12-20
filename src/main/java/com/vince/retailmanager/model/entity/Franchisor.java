package com.vince.retailmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.model.View;
import com.vince.retailmanager.model.View.Public;
import com.vince.retailmanager.model.View.Summary;
import com.vince.retailmanager.web.controller.Franchisor.ValidFranchisor;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "franchisors")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//@JsonIdentityReference(alwaysAsId = true) // otherwise first ref as POJO, others as id
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidFranchisor
public class Franchisor extends Company {

  @Column(unique = true)
  @NotNull
  @JsonView(Public.class)
  private String name;

  @Column(unique = true)
  @NotNull
  @JsonView(Public.class)
  private String website;

  @NotNull
  @JsonView(Summary.class)
  private String description;

  @OneToMany(mappedBy = "franchisor", fetch = FetchType.EAGER)
  @JsonIgnoreProperties(value = {"franchisor"})
  @Builder.Default
  @JsonView(View.Franchisor.class)
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

//	@OneToMany(mappedBy = "franchisor")
//	@JsonIgnoreProperties(value = {"franchisor"})
//	@Builder.Default
//	@JsonIgnore
//	@ToString.Exclude
//	private Set<PercentageFee> feesSent = new HashSet<>();

  public void addFranchisee(Franchisee franchisee) {
//		if (franchisees == null) franchisees = new HashSet<>();
    franchisees.add(franchisee);
    franchisee.setFranchisor(this);
  }

  @Override
  @JsonView(View.Franchisor.class)
  public BigDecimal getCashBalance() {
    return super.getCashBalance();
  }

//	protected Set<Franchisee> getFranchiseesInternal() {
//		if (this.franchisees == null) {
//			this.franchisees = new HashSet<>();
//		}
//		return this.franchisees;
//	}
}
