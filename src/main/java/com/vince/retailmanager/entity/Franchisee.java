package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.StringJoiner;

@Entity
@Table(name = "franchisees")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Franchisee extends Company {

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "franchisor_id")
	@EqualsAndHashCode.Exclude
	@JsonManagedReference
	private Franchisor franchisor;

	public Payment createPaymentToFranchisor(Double amount) {
		return Payment.builder()
			 .payer(this)
			 .recipient(franchisor)
			 .amount(amount)
			 .build();
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Franchisee.class.getSimpleName() + "[", "]")
			 .add("id=" + getId())
			 .toString();
	}
}

