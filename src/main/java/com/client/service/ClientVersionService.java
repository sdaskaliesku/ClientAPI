package com.client.service;

import com.client.domain.UpdateRequest;
import com.client.domain.db.ClientVersion;
import com.client.domain.enums.VersionCheckResult;
import com.client.domain.enums.UpdatePolicy;
import com.client.repository.ClientVersionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sdaskaliesku
 */
@Service
public class ClientVersionService extends AbstractService<ClientVersion> {

    private List<ClientVersion> getClientVersionsByType(boolean allowBetta) {
        List<ClientVersion> list = read();
        Collections.sort(list);
        List<ClientVersion> result = new ArrayList<>();
        for (ClientVersion version : list) {
            if (!version.getBanned()) {
                if (allowBetta) {
                    result.add(version);
                } else if (!version.getBetta()) {
                    result.add(version);
                }
            }
        }
        return result;
    }

    @Override
    public List<ClientVersion> read() {
        List<ClientVersion> clientVersions = super.read();
        Collections.sort(clientVersions);
        return clientVersions;
    }

    public List<ClientVersion> getAllAllowedVersions() {
        List<ClientVersion> clientVersions = getRepository().getAllAllowedVersions();
        Collections.sort(clientVersions);
        return clientVersions;
    }

    public List<ClientVersion> getStableVersions() {
        return getClientVersionsByType(false);
    }

    public List<ClientVersion> getBettaVersions() {
        return getClientVersionsByType(true);
    }

    public ClientVersion getLastStableVersion() {
        return getStableVersions().get(0);
    }

    public ClientVersion getLastBettaVersion() {
        return getBettaVersions().get(0);
    }

    public ClientVersion getLastVersion(boolean allowBetta) {
        return getClientVersionsByType(allowBetta).get(0);
    }

    public VersionCheckResult getUpdateCheckResult(UpdateRequest request, ClientVersion lastVersion) {
        Double version = request.getUserVersion();
        Boolean isBetta = request.getBetta();
        Boolean allowBetta = request.getUpdateToBeta();
        VersionCheckResult result = VersionCheckResult.Optional;
        ClientVersion current = getClientVersion(version, isBetta);
        if (current.getBanned()) {
            result = VersionCheckResult.Required;
            return result;
        }
        List<ClientVersion> list = getClientVersionsByType(allowBetta);
        for (ClientVersion clientVersion : list) {
            if (clientVersion.getVersion() > version) {
                if (clientVersion.getUpdatePolicy().equals(UpdatePolicy.Required)) {
                    result = VersionCheckResult.Required;
                    break;
                }
            }
        }
        if (!result.equals(VersionCheckResult.Required) && version >= lastVersion.getVersion()) {
            if (isBetta) {
                result = VersionCheckResult.UpToDateBeta;
            } else {
                result = VersionCheckResult.UpToDate;
            }
        }
        return result;
    }

    public boolean isVersionBanned(double version, boolean isBetta) {
        List<ClientVersion> versions = read();
        for (ClientVersion clientVersion : versions) {
            if (clientVersion.getVersion().equals(version) && clientVersion.getBetta().equals(isBetta)) {
                return clientVersion.getBanned();
            }
        }
        return false;
    }

    public void setAllVersionsBanned(double lastWorkingVersion) {
        getRepository().setAllVersionsBanned(lastWorkingVersion);
    }

    public ClientVersion getClientVersion(double version, boolean isBetta) {
        return getRepository().getClientVersion(version, isBetta);
    }

    public List<ClientVersion> getCurrentVersion(double version) {
        return getRepository().getCurrentVersion(version);
    }

    private ClientVersionRepository getRepository() {
        return (ClientVersionRepository) repository;
    }

}
