package com.client.domain.responses;

import com.client.domain.enums.AccessType;
import com.client.domain.enums.VersionCheckResult;
import com.client.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Date;

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

    public ActivateResponse() {
        accessType = AccessType.NoAccess;
        versionCheckResult = VersionCheckResult.UpToDate;
        accessEndDate = DateUtils.getCurrentDate();
        isClanAccess = false;
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
}
