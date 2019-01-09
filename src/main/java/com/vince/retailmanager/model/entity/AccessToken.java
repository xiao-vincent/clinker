package com.vince.retailmanager.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "access_tokens", uniqueConstraints = @UniqueConstraint(columnNames = {"username",
    "company"}))
@Data
@EqualsAndHashCode(callSuper = true, of = "")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "username")
  @JsonIgnore
  private User user;

  @ManyToOne
  @JoinColumn(name = "company")
  private Company company;

}
