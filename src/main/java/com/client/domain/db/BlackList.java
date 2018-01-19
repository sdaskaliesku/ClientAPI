package com.client.domain.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author sdaskaliesku
 */
@SuppressWarnings("all")
@Entity
@Table(name = "BlackList")
@NamedQueries({
        @NamedQuery(name = BlackList.IS_USER_IN_BLACK_LIST, query = "select b from BlackList b where b.nickOrClanName = :nickOrClanName"),
        @NamedQuery(name = BlackList.GET_ALL_CLANS_OR_USERS, query = "select b from BlackList b where b.clan= :isClan"),
        @NamedQuery(name = BlackList.GET_ALL, query = "select b from BlackList b")
})
public class BlackList implements Serializable {

    public static final String IS_USER_IN_BLACK_LIST = "BlackList.isUserInBlackList";
    public static final String GET_ALL_CLANS_OR_USERS = "BlackList.getAllClansOrUsers";
    public static final String GET_ALL = "BlackList.getAll";

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String nickOrClanName;

    @Column
    private Boolean clan;

    @Column
    private String reason;

    public BlackList() {
        this.clan = Boolean.FALSE;
        this.reason = "";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickOrClanName() {
        return nickOrClanName;
    }

    public void setNickOrClanName(String nickOrClanName) {
        this.nickOrClanName = nickOrClanName;
    }

    public Boolean getClan() {
        return clan;
    }

    public void setClan(Boolean clan) {
        this.clan = clan;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
