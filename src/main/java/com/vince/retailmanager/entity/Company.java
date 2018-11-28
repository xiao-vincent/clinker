package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Company extends BaseEntity {

	@JsonIgnore
	private boolean enabled = true;

	@Column
	private BigDecimal cashBalance = BigDecimal.valueOf(0);


	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Invoice> invoices = new HashSet<>();
}
