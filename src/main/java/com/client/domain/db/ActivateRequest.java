package com.client.domain.db;

import com.client.domain.enums.AccessType;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringJoiner;


/**
 * @author sdaskaliesku
 */
@SuppressWarnings("ALL")
@Entity
@Table(name = "ActivateRequest")
@NamedQueries({
        @NamedQuery(name = ActivateRequest.GET_LOGS, query = "select ar from ActivateRequest ar")
})
@Proxy(lazy=false)
public class ActivateRequest implements Serializable {

    public static final String GET_LOGS = "ActivateRequest.getLogs";

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Date date;

    @Column
    private String nickName;

    @Column
    private String clanName;

    @Column
    private String ipAddress;

    @Column
    private String macAddress;

    @Column
    private Double clientVersion;

    @Column
    private Boolean activated;

    @Column
    private AccessType accessType;

    @Transient
    private String millis;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column
    private Set<String> ipAdresses;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column
    private Set<String> macAdresses;

    public ActivateRequest() {
        this.date = new Date(new java.util.Date().getTime());
        this.millis = String.valueOf(System.currentTimeMillis());
        ipAdresses = new LinkedHashSet<>();
        macAdresses = new LinkedHashSet<>();
    }

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Set<String> getIpAdresses() {
        return ipAdresses;
    }

    public void setIpAdresses(Set<String> ipAdresses) {
        this.ipAdresses = ipAdresses;
    }

    public Set<String> getMacAdresses() {
        return macAdresses;
    }

    public void setMacAdresses(Set<String> macAdresses) {
        this.macAdresses = macAdresses;
    }

    public Double getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(Double clientVersion) {
        this.clientVersion = clientVersion;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

    public String getMillis() {
        return millis;
    }

    public void setMillis(String millis) {
        this.millis = millis;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ActivateRequest.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("date=" + date)
                .add("nickName='" + nickName + "'")
                .add("clanName='" + clanName + "'")
                .add("ipAddress='" + ipAddress + "'")
                .add("macAddress='" + macAddress + "'")
                .add("clientVersion=" + clientVersion)
                .add("activated=" + activated)
                .add("accessType=" + accessType)
                .add("millis='" + millis + "'")
                .add("ipAdresses=" + ipAdresses)
                .add("macAdresses=" + macAdresses)
                .toString();
    }
}
