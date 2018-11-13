package com.vince.retailmanager.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "access_tokens", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "company"}))
public class AccessToken extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "username")
	@JsonIgnore
	private User user;

	@ManyToOne
	@JoinColumn(name = "company")
	private Company company;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		AccessToken that = (AccessToken) o;
		return Objects.equals(user, that.user) &&
			 Objects.equals(company, that.company);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), user, company);
	}
}
