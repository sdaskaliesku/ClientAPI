package com.client.domain.db;

import com.client.domain.enums.AccessType;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
@SuppressWarnings("ALL")
@Entity
@Table(name = "ActivateRequest")
@NamedQueries({
        @NamedQuery(name = ActivateRequest.GET_LOGS, query = "select ar from ActivateRequest ar")
})
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
    private Boolean isBetta;

    @Column
    private AccessType accessType;

    @Transient
    private String millis;

    public ActivateRequest() {
        this.date = new Date(new java.util.Date().getTime());
        this.millis = String.valueOf(System.currentTimeMillis());
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

    public Boolean getBetta() {
        return isBetta;
    }

    public void setBetta(Boolean betta) {
        isBetta = betta;
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
        return new ToStringBuilder(this)
                .append("id", id)
                .append("date", date)
                .append("nickName", nickName)
                .append("clanName", clanName)
                .append("ipAddress", ipAddress)
                .append("macAddress", macAddress)
                .append("clientVersion", clientVersion)
                .append("activated", activated)
                .append("isBetta", isBetta)
                .append("accessType", accessType)
                .append("millis", millis)
                .toString();
    }
}
