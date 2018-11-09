package com.vince.retailmanager.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "payments")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {
	@OneToOne
	@JoinColumn(name = "payer_id")
	private Company payer;

	@OneToOne
	@JoinColumn(name = "recipient_id")
	private Company recipient;

	//    @Column(precision = 8, scale = 2)
	private Double amount;

}
