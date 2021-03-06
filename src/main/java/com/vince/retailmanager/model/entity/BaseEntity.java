package com.vince.retailmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.web.json.View.Public;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract base class for all objects that need a generated ID
 *
 * @author Vincent Xiao
 */
@MappedSuperclass
@Data
@EqualsAndHashCode
public abstract class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonView(Public.class)
  protected Integer id;

  @JsonProperty("isNew")
  @JsonIgnore
  public boolean isNew() {
    return this.id == null;
  }

}


