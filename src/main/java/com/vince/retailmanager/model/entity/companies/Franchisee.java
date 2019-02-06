package com.vince.retailmanager.model.entity.companies;

import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.web.validator.ValidFranchisee;
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

/**
 * A franchisee operates under the franchisor's business model and brand.
 *
 * @author Vincent Xiao
 * @see Franchisor
 */
@Entity
@Table(name = "franchisees")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidFranchisee
public class Franchisee extends Company {

  @ManyToOne
  @JoinColumn(name = "franchisor_id")
  @EqualsAndHashCode.Exclude
  @JsonView(Franchisee.class)
  @NotNull
  private Franchisor franchisor;
}
