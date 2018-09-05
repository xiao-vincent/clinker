package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
@Table(name = "franchisees")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Franchisee extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "username")
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "franchisor_id")
    @JsonFormat()
    private Franchisor franchisor;

    public Franchisee() {
    }


    public Franchisor getFranchisor() {
        return franchisor;
    }

    public void setFranchisor(Franchisor franchisor) {
        this.franchisor = franchisor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
