package com.client.domain;

import java.io.Serializable;

/**
 * @author sdaskaliesku
 */
public class UpdateRequest implements Serializable {

    private Double userVersion;

    private Boolean isBetta;

    private Boolean updateToBeta;

    public UpdateRequest() {
    }

    public UpdateRequest(Double userVersion, Boolean isBetta, Boolean updateToBeta) {
        this.userVersion = userVersion;
        this.isBetta = isBetta;
        this.updateToBeta = updateToBeta;
    }

    public Double getUserVersion() {
        return userVersion;
    }

    public void setUserVersion(Double userVersion) {
        this.userVersion = userVersion;
    }

    public Boolean getUpdateToBeta() {
        return updateToBeta;
    }

    public Boolean getBetta() {
        return isBetta;
    }

    public void setBetta(Boolean betta) {
        isBetta = betta;
    }

    public void setUpdateToBeta(Boolean updateToBeta) {
        this.updateToBeta = updateToBeta;
    }

    @Override
    public String toString() {
        return "UpdateRequest{" +
                "userVersion=" + userVersion +
                ", isBetta=" + isBetta +
                ", updateToBeta=" + updateToBeta +
                '}';
    }
}
