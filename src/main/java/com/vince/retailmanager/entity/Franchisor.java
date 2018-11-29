package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.web.controller.validator.ValidFranchisor;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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
	@JsonView(View.Public.class)
	private String name;

	@Column(unique = true)
	@NotNull
	@JsonView(View.Public.class)
	private String website;

	@NotNull
	@JsonView(View.Summary.class)
	private String description;

	@OneToMany(mappedBy = "franchisor", fetch = FetchType.EAGER)
	@JsonIgnoreProperties(value = {"franchisor", "cashBalance"})
	@Builder.Default
	@JsonProperty("franchisees")
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
	private double royaltyFee;

	@NotNull
	@Range(min = 0, max = 1)
	@JsonView(View.Franchisor.class)
	private double marketingFee;

	@NotNull
	@Range(min = 1, max = 365)
	@Builder.Default
	@JsonView(View.Franchisor.class)
	private int feeFrequency = 12; //per year

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
