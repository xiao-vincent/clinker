package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.StringJoiner;


@MappedSuperclass
@Data
@EqualsAndHashCode(of = "id")
public abstract class BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(View.Public.class)
	protected Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("isNew")
	@JsonIgnore
	public boolean isNew() {
		return this.id == null;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", BaseEntity.class.getSimpleName() + "[", "]")
			 .add("id=" + id)
			 .toString();
	}

}


