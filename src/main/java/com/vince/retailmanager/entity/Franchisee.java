package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
	@JsonBackReference
	private Franchisor franchisor;

	@Override
	public String toString() {
		return new StringJoiner(", ", Franchisee.class.getSimpleName() + "[", "]")
			 .add("id=" + this.getId())
			 .toString();
	}
}

