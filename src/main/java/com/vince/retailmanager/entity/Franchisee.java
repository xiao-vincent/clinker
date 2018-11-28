package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.web.controller.validator.ValidFranchisee;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.StringJoiner;

@Entity
@Table(name = "franchisees")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidFranchisee
public class Franchisee extends Company {

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "franchisor_id")
	@EqualsAndHashCode.Exclude
	@JsonView(View.Public.class)
	private Franchisor franchisor;

	@Override
	@JsonView(View.Franchisee.class)
	public BigDecimal getCashBalance() {
		return super.getCashBalance();
	}


	@Override
	public String toString() {
		return new StringJoiner(", ", Franchisee.class.getSimpleName() + "[", "]")
			 .add("id=" + this.getId())
			 .toString();
	}
}

