package com.client.domain.responses;

import com.client.domain.enums.AccessType;
import com.client.domain.enums.VersionCheckResult;
import com.client.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.util.List;

/**
 * @author sdaskaliesku
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivateResponse extends Response {

    private AccessType accessType;
    private VersionCheckResult versionCheckResult;
    private Date accessEndDate;
    private Boolean isClanAccess;
    private String clanName;
    private String nickname;
    private String urlForUpdate;
    private String releaseNotes;
    private String millis;
    private List<String> freeFunctions;

    public ActivateResponse() {
        accessType = AccessType.NoAccess;
        versionCheckResult = VersionCheckResult.UpToDate;
        accessEndDate = DateUtils.getCurrentDate();
        urlForUpdate = StringUtils.EMPTY;
        releaseNotes = StringUtils.EMPTY;
        isClanAccess = false;
        this.millis = String.valueOf(System.currentTimeMillis());
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

    public VersionCheckResult getVersionCheckResult() {
        return versionCheckResult;
    }

    public void setVersionCheckResult(VersionCheckResult versionCheckResult) {
        this.versionCheckResult = versionCheckResult;
    }

    public Date getAccessEndDate() {
        return accessEndDate;
    }

    public void setAccessEndDate(Date accessEndDate) {
        this.accessEndDate = accessEndDate;
    }

    public Boolean getClanAccess() {
        return isClanAccess;
    }

    public void setClanAccess(Boolean clanAccess) {
        isClanAccess = clanAccess;
    }

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUrlForUpdate() {
        return urlForUpdate;
    }

    public void setUrlForUpdate(String urlForUpdate) {
        this.urlForUpdate = urlForUpdate;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    public String getMillis() {
        return millis;
    }

    public void setMillis(String millis) {
        this.millis = millis;
    }

    public List<String> getFreeFunctions() {
        return freeFunctions;
    }

    public void setFreeFunctions(List<String> freeFunctions) {
        this.freeFunctions = freeFunctions;
    }
}
