package com.client.domain.enums;

/**
 * @author sdaskaliesku
 */
public enum VersionCheckResult {
    Required, // everyone MUST update 0
    Optional, // there is new version, but it's optional update 1
    UpToDate // user has the last version 2
}