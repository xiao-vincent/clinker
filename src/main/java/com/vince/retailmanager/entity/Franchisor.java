package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
	@NotNull
	private String name;

	@Column(unique = true)
	@NotNull
	private String website;

	@NotNull
	private String description;

	@OneToMany(mappedBy = "franchisor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnoreProperties(value = "franchisor")
	private Set<Franchisee> franchisees;


	public void addFranchisee(Franchisee franchisee) {
		if (franchisees == null) franchisees = new HashSet<>();
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
