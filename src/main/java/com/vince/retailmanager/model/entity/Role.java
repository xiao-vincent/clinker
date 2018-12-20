package com.vince.retailmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import java.util.StringJoiner;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "role"}))
public class Role extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "username")
  @JsonIgnore
  private User user;

  @Column(name = "role")
  private String name;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Role.class.getSimpleName() + "[", "]")
        .add("name='" + name + "'")
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Role role = (Role) o;
    return Objects.equals(user, role.user) &&
        Objects.equals(name, role.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), user, name);
  }
}
