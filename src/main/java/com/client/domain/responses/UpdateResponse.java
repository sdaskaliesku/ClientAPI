package com.client.domain.responses;

import com.client.domain.enums.VersionCheckResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sdaskaliesku
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateResponse extends Response {

    @JsonProperty
    private VersionCheckResult versionCheckResult;

    @JsonProperty
    private String url;

    @JsonProperty
    private String comments;

    @JsonProperty
    private Boolean isBetta;

    @JsonProperty
    private Double version;

    public UpdateResponse() {
    }

    public UpdateResponse(VersionCheckResult versionCheckResult, String url, Boolean isBetta, Double version) {
        this.versionCheckResult = versionCheckResult;
        this.url = url;
        this.isBetta = isBetta;
        this.version = version;
    }

    public VersionCheckResult getVersionCheckResult() {
        return versionCheckResult;
    }

    public void setVersionCheckResult(VersionCheckResult versionCheckResult) {
        this.versionCheckResult = versionCheckResult;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getBetta() {
        return isBetta;
    }

    public void setBetta(Boolean betta) {
        isBetta = betta;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }
}
