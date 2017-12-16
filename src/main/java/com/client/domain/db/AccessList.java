package com.client.domain.db;

import com.client.domain.enums.AccessType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Date;

/**
 * @author sdaskaliesku
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "AccessList")
@NamedQueries({
        @NamedQuery(name = AccessList.GET_BY_CLAN_OR_USERNAME, query = "select al from AccessList al where al.name = :nickName OR al.name = :clanName"),
        @NamedQuery(name = AccessList.GET_ALL, query = "select al from AccessList al"),
        @NamedQuery(name = AccessList.GET_ALL_CLANS_OR_USERS, query = "select al from AccessList al where al.clan = :isClan")
})
public class AccessList implements Serializable {

    public static final String GET_BY_CLAN_OR_USERNAME = "AccessList.getByClanOrUserName";
    public static final String GET_ALL = "AccessList.getAll";
    public static final String GET_ALL_CLANS_OR_USERS = "AccessList.getAllClansOrUsers";

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    @Column
    private Date fromDate;

    @Column
    private Date dueDate;

    @Column
    private AccessType accessType;

    @Column
    private String comments;

    @Column
    private Boolean clan;

    @Transient
    private Boolean isClanAccess;

    public AccessList() {
        this.fromDate = new Date(new java.util.Date().getTime());
        this.dueDate = this.fromDate;
        this.clan = false;
        this.isClanAccess = this.clan;
        this.accessType = AccessType.Basic;
        this.comments = "";
    }

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

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getClan() {
        return clan;
    }

    public void setClan(Boolean clan) {
        this.clan = clan;
    }

    public Boolean getClanAccess() {
        return isClanAccess;
    }

    public void setClanAccess(Boolean clanAccess) {
        isClanAccess = clanAccess;
    }
}
