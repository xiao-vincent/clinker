package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Company extends BaseEntity {

	@JsonIgnore
	private boolean enabled = true;

	@Column
	private BigDecimal balance = BigDecimal.valueOf(0);
}
