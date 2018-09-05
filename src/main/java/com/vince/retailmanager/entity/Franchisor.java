package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "franchisors")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Franchisor extends BaseEntity {
    private String name;
    private String website;
    private String industry;

    @OneToOne
    @JoinColumn(name = "username")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "franchisor", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Franchisee> franchiseeList;

    public Franchisor() {
    }

    public Franchisor(String name, String website, String industry) {
        this.name = name;
        this.website = website;
        this.industry = industry;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Franchisee> getFranchiseeList() {
        return franchiseeList;
    }

    public void setFranchiseeList(Set<Franchisee> franchiseeList) {
        this.franchiseeList = franchiseeList;
    }

    @Override
    public String toString() {
        return "Franchisor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", website='" + website + '\'' +
                ", industry='" + industry + '\'' +
                '}';
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }


    public void addFranchisee(Franchisee franchisee) {
        if (franchisee.isNew()) {
            getFranchiseesInternal().add(franchisee);
        }
        franchisee.setFranchisor(this);
        franchisee.setUser(this.user);
    }

    protected Set<Franchisee> getFranchiseesInternal() {
        if (this.franchiseeList == null) {
            this.franchiseeList = new HashSet<>();
        }
        return this.franchiseeList;
    }
}
