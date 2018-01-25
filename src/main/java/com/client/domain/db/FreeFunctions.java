package com.client.domain.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "FreeFunctions")
@NamedQueries({
        @NamedQuery(name = FreeFunctions.FIND_ALL, query = "select fr from FreeFunctions fr")
})
public class FreeFunctions {

    public static final String FIND_ALL = "FreeFunctions.findAll";

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
