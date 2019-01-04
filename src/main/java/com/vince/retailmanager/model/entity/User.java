package com.vince.retailmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vince.retailmanager.web.controller.validator.ValidUser;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@ValidUser
public class User {

  @Id
  @Column(name = "username")
  @Size(min = 3, max = 18)
  @NotNull
  @Pattern(regexp = "^[A-Za-z0-9_]*$", message = "must contain only alphanumeric characters or underscores")
  private String username;

  @Column(name = "password")
  @Size(min = 8, max = 128)
  @JsonIgnore
  @NotNull
  private String password;

  @Column(name = "enabled")
  private Boolean enabled;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
  private Set<Role> roles = new HashSet<>();


  public User() {
    //add default  role
    addRole("USER");
    setEnabled(true);
  }

  public User(String username, String password) {
    this();
    this.username = username;
    this.password = password;
  }


  public String getUsername() {
    return username;
  }


  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  @JsonProperty
  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }


  public Set<Role> getRoles() {
    return roles;
  }

  public void addRole(String roleName) {
    Role role = new Role();
    role.setUser(this);
    role.setName(roleName);
    this.roles.add(role);
  }


  @Override
  public String toString() {
    return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
        .add("username='" + username + "'")
        .add("password='" + password + "'")
        .add("enabled=" + enabled)
        .add("roles=" + roles)
        .toString();
  }

}
