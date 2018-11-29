package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Company extends BaseEntity {

	@JsonIgnore
	private boolean enabled = true;

	@JsonView(View.Summary.class)
	private String type = getClass().getSimpleName();

	@Column
	private BigDecimal cashBalance = BigDecimal.valueOf(0);


	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
//	@JsonIgnore
	private Set<Invoice> invoicesReceived = new HashSet<>();


	@OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
//	@JsonIgnore
	private Set<Invoice> invoicesSent = new HashSet<>();
}
