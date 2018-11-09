package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

	@OneToMany(mappedBy = "franchisor", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Franchisee> franchiseeList = new HashSet<>();


	public void addFranchisee(Franchisee franchisee) {
		if (franchisee.isNew()) {
			franchiseeList.add(franchisee);
		}
		franchisee.setFranchisor(this);
		franchisee.setUser(this.getUser());
	}

//	protected Set<Franchisee> getFranchiseesInternal() {
//		if (this.franchiseeList == null) {
//			this.franchiseeList = new HashSet<>();
//		}
//		return this.franchiseeList;
//	}
}
