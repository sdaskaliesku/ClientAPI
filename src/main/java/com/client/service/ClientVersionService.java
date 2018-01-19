package com.client.service;

import com.client.domain.UpdateRequest;
import com.client.domain.db.ClientVersion;
import com.client.domain.enums.UpdatePolicy;
import com.client.domain.enums.VersionCheckResult;
import com.client.repository.ClientVersionRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author sdaskaliesku
 */
@Service
public class ClientVersionService extends AbstractService<ClientVersion> {

    private List<ClientVersion> getAllClientVersions() {
        List<ClientVersion> list = read();
        Collections.sort(list);
        return list.stream().filter(version -> !version.getBanned()).collect(Collectors.toList());
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

    public ClientVersion getLastVersion() {
        List<ClientVersion> versions = getAllClientVersions();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(versions)) {
            return versions.get(0);
        }
        return new ClientVersion();
    }

    public VersionCheckResult getUpdateCheckResult(UpdateRequest request, ClientVersion lastVersion) {
        Double version = request.getUserVersion();
        VersionCheckResult result = VersionCheckResult.Optional;
        ClientVersion current = getClientVersion(version);
        if (Objects.nonNull(current) && current.getBanned()) {
            result = VersionCheckResult.Required;
            return result;
        }
        List<ClientVersion> list = getAllClientVersions();
        for (ClientVersion clientVersion : list) {
            if (clientVersion.getVersion() > version) {
                if (clientVersion.getUpdatePolicy().equals(UpdatePolicy.Required)) {
                    result = VersionCheckResult.Required;
                    break;
                }
            }
        }
        if (!result.equals(VersionCheckResult.Required) && version >= lastVersion.getVersion()) {
            result = VersionCheckResult.UpToDate;
        }
        return result;
    }

    public boolean isVersionBanned(double version) {
        List<ClientVersion> versions = read();
        for (ClientVersion clientVersion : versions) {
            if (clientVersion.getVersion().equals(version)) {
                return clientVersion.getBanned();
            }
        }
        return false;
    }

    public void setAllVersionsBanned(double lastWorkingVersion) {
        getRepository().setAllVersionsBanned(lastWorkingVersion);
    }

    public ClientVersion getClientVersion(double version) {
        return getRepository().getClientVersion(version);
    }

    public List<ClientVersion> getCurrentVersion(double version) {
        return getRepository().getCurrentVersion(version);
    }

    private ClientVersionRepository getRepository() {
        return (ClientVersionRepository) repository;
    }

}
