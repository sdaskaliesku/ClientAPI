package com.client.domain.db;

import com.client.domain.enums.UpdatePolicy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

/**
 * @author sdaskaliesku
 */
@SuppressWarnings("ALL")
@Entity
@Table(name = "ClientVersion")
@NamedQueries({
        @NamedQuery(name = ClientVersion.GET_ALL_VERSIONS, query = "select v from ClientVersion v"),
        @NamedQuery(name = ClientVersion.GET_ALL_ALLOWED_VERSIONS, query = "select v from ClientVersion v where v.banned = :isBanned"),
        @NamedQuery(name = ClientVersion.GET_ALL_VERSIONS_DESC, query = "select v from ClientVersion v " +
                "where v.banned = :isBanned ORDER BY v.version DESC"),
        @NamedQuery(name = ClientVersion.GET_CURRENT_VERSION, query = "select v from ClientVersion v " +
                "where v.version = :version and v.banned = :isBanned")
})
public class ClientVersion implements Serializable, Comparable<ClientVersion> {

    public static final String GET_ALL_VERSIONS = "UpdateRequest.getAllVersions";

    public static final String GET_ALL_ALLOWED_VERSIONS = "UpdateRequest.getAllAllowedVersions";

    public static final String GET_ALL_VERSIONS_DESC = "UpdateRequest.getAllVersionsDesc";

    public static final String GET_CURRENT_VERSION = "UpdateRequest.getCurrentVersion";

    public static final boolean ASCENDING_SORT_ORDER = false;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Double version;

    @Column
    private UpdatePolicy updatePolicy;

    @Column
    private Boolean banned;

    @Column
    private String releaseNotes;

    @Column
    private String link;

    @Column
    private Date date;

    public ClientVersion() {
        this.date = new Date(new java.util.Date().getTime());
        this.banned = false;
        this.link = "";
        this.releaseNotes = "";
        this.version = 0.0;
        this.updatePolicy = UpdatePolicy.Optional;
    }

    public ClientVersion(Double version, UpdatePolicy updatePolicy, Boolean banned, String releaseNotes, String link) {
        this();
        this.version = version;
        this.updatePolicy = updatePolicy;
        this.banned = banned;
        this.releaseNotes = releaseNotes;
        this.link = link;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public UpdatePolicy getUpdatePolicy() {
        return updatePolicy;
    }

    public void setUpdatePolicy(UpdatePolicy updatePolicy) {
        this.updatePolicy = updatePolicy;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(ClientVersion o) {
        int result = 0;
        if (o.getVersion() < this.getVersion()) {
            result = -1;
        }
        if (o.getVersion() > this.getVersion()) {
            result = 1;
        }
        if (ASCENDING_SORT_ORDER) {
            return result * (-1);
        } else {
            return result;
        }
    }

    @Override
    public String toString() {
        return "ClientVersion{" +
                "id=" + id +
                ", version=" + version +
                ", updatePolicy=" + updatePolicy +
                ", banned=" + banned +
                ", releaseNotes='" + releaseNotes + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
