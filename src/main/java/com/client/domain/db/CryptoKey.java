package com.client.domain.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

@SuppressWarnings("ALL")
@Entity
@Table(name = "CryptoKey")
@NamedQuery(name = CryptoKey.FIND_ALL, query = "select ck from CryptoKey ck")
public class CryptoKey implements Serializable, Comparable<CryptoKey> {
    public static final String FIND_ALL = "CryptoKey.findAll";

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Date date;

    @Column
    private String cryptoKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCryptoKey() {
        return cryptoKey;
    }

    public void setCryptoKey(String cryptoKey) {
        this.cryptoKey = cryptoKey;
    }


    @Override
    public int compareTo(CryptoKey o) {
        return getDate().compareTo(o.getDate());
    }
}
