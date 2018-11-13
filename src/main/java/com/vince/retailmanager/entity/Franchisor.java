package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "franchisors")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Franchisor extends Company {

	@Column(unique = true)
	private String name;
	private String website;
	private String description;

	@OneToMany(mappedBy = "franchisor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Franchisee> franchisees;


	public void addFranchisee(Franchisee franchisee) {
		if (franchisees == null) {
			franchisees = new HashSet<>();
		}
		franchisees.add(franchisee);
		franchisee.setFranchisor(this);
	}

//	protected Set<Franchisee> getFranchiseesInternal() {
//		if (this.franchisees == null) {
//			this.franchisees = new HashSet<>();
//		}
//		return this.franchisees;
//	}
}
