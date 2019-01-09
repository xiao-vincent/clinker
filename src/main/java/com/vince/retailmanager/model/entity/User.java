package com.vince.retailmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vince.retailmanager.web.controller.validator.ValidUser;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
@ValidUser
public class User {

  @Id
  @Size(min = 3, max = 18)
  @NotNull
  @Pattern(regexp = "^[A-Za-z0-9_]*$", message = "must contain only alphanumeric characters or underscores")
  private String username;

  @Size(min = 8, max = 128)
  @JsonIgnore
  @NotNull
  private String password;

  private Boolean enabled;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
  private Set<Role> roles;

  public User() {
    //add default  role
    this.addRole("USER");
    this.setEnabled(true);
  }

  public User(String username, String password) {
    this();
    this.username = username;
    this.password = password;
  }

  @JsonIgnore
  public void addRole(String roleName) {
    if (this.roles == null) {
      this.roles = new HashSet<>();
    }
    Role role = new Role();
    role.setName(roleName);
    this.roles.add(role);
  }


}
