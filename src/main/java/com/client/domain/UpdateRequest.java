package com.client.domain;

import java.io.Serializable;

/**
 * @author sdaskaliesku
 */
public class UpdateRequest implements Serializable {

    private Double userVersion;

    public UpdateRequest() {
    }

    public UpdateRequest(Double userVersion) {
        this.userVersion = userVersion;
    }

    public Double getUserVersion() {
        return userVersion;
    }

    public void setUserVersion(Double userVersion) {
        this.userVersion = userVersion;
    }

    @Override
    public String toString() {
        return "UpdateRequest{" +
                "userVersion=" + userVersion + '}';
    }
}
