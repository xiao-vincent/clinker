package com.vince.retailmanager.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class Company extends BaseEntity {

//	@OneToOne
//	@JoinColumn(name = "username")
//	@JsonIgnore
//	private User user;

}
