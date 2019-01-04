package com.vince.retailmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.web.controller.validator.ValidFranchisee;
import java.math.BigDecimal;
import java.util.StringJoiner;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "franchisees")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidFranchisee
public class Franchisee extends Company {

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
      CascadeType.REFRESH})
  @JoinColumn(name = "franchisor_id")
  @EqualsAndHashCode.Exclude
  @JsonView(Franchisee.class)
  @NotNull
  private Franchisor franchisor;

  @Override
  @JsonView(Franchisee.class)
  public BigDecimal getCashBalance() {
    return super.getCashBalance();
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Franchisee.class.getSimpleName() + "[", "]")
        .add("id=" + this.getId())
        .toString();
  }
}

