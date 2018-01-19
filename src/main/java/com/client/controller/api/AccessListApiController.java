package com.client.controller.api;

import com.client.domain.UpdateRequest;
import com.client.domain.db.AccessList;
import com.client.domain.db.ActivateRequest;
import com.client.domain.db.ClientVersion;
import com.client.domain.enums.AccessType;
import com.client.domain.enums.UpdatePolicy;
import com.client.domain.enums.VersionCheckResult;
import com.client.domain.responses.ActivateResponse;
import com.client.domain.responses.Response;
import com.client.service.CryptoKeyService;
import com.client.utils.DateUtils;
import com.client.utils.EncodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.NotSupportedException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author sdaskaliesku
 */
@Controller
@RequestMapping("/api/access")
public class AccessListApiController extends ApiController {

    @Autowired
    private CryptoKeyService cryptoKeyService;

    private static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (StringUtils.isEmpty(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public static VersionCheckResult fromUpdatePolicy(ClientVersion lastVersion, double currentVersion, boolean isBeta) {
        if (lastVersion.getVersion() > currentVersion) {
            if (lastVersion.getUpdatePolicy().equals(UpdatePolicy.Required)) {
                return VersionCheckResult.Required;
            }
            return VersionCheckResult.Optional;
        }
        if (lastVersion.getUpdatePolicy().equals(UpdatePolicy.Required)) {
            return VersionCheckResult.Required;
        } else if (lastVersion.getUpdatePolicy().equals(UpdatePolicy.Optional)) {
            return VersionCheckResult.Optional;
        }
        if (isBeta) {
            return VersionCheckResult.UpToDateBeta;
        }
        return VersionCheckResult.UpToDate;
    }

    @Override
    public Response checkForUpdates(@Valid @ModelAttribute UpdateRequest updateRequest) throws Exception {
        throw new NotSupportedException();
    }

    @Override
    public Response getLastVersion(@RequestParam Boolean allowBetta) throws Exception {
        throw new NotSupportedException();
    }

    @Override
    public Response getVersion(@RequestParam Double version, @RequestParam Boolean isBetta) throws Exception {
        throw new NotSupportedException();
    }

    @Override
    public Response getAllVersions() throws Exception {
        throw new NotSupportedException();
    }

    private Optional<ActivateRequest> findActivateRequest(ActivateRequest activateRequest) {
        List<ActivateRequest> activateRequests = activateRequestService.read();
        for (ActivateRequest request : activateRequests) {
            if (request.getNickName().equalsIgnoreCase(activateRequest.getNickName())) {
                activateRequest.setId(request.getId());
                return Optional.of(activateRequest);
            }
        }
        return Optional.empty();
    }

    private void logLastUserActivateRequest(ActivateRequest activateRequest) {
        Optional<ActivateRequest> opt = findActivateRequest(activateRequest);
        if (opt.isPresent()) {
            activateRequestService.update(opt.get());
        } else {
            activateRequestService.create(activateRequest);
        }
    }

    @Override
    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    @ResponseBody
    public String activate(HttpServletRequest httpServletRequest) {
        EncodeUtils encodeUtils = new EncodeUtils(cryptoKeyService.getCryptoKey());
        String inputString = httpServletRequest.getParameter("request");
        String decodedInputString;
        try {
            decodedInputString = URLDecoder.decode(inputString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            decodedInputString = inputString;
        }
        ActivateRequest activateRequest = encodeUtils.decode(decodedInputString, ActivateRequest.class);
        return encodeUtils.encode(activate(activateRequest, httpServletRequest));
    }

    @Override
    public ActivateResponse activate(ActivateRequest activateRequest, HttpServletRequest request) {
        try {
            activateRequest.setIpAddress(getIpAddress(request));
        } catch (Exception e) {
            // do nothing
        }
        ActivateResponse response = new ActivateResponse();
        try {
            log.info("New activate request: {}", activateRequest);
            // log request to db
            logLastUserActivateRequest(activateRequest);

            boolean isClanInBlackList = blackListService.isClanInBlackList(activateRequest.getClanName());
            boolean isUserInBlackList = blackListService.isUserInBlackList(activateRequest.getNickName());
            if (isClanInBlackList || isUserInBlackList) {
                // access denied
                response.setNickname(activateRequest.getNickName());
                response.setClanName(activateRequest.getClanName());
                response.setAccessType(AccessType.Denied);
                if (isClanInBlackList) {
                    response.setMessage("Clan is banned!");
                } else {
                    response.setMessage("User is banned!");
                }
                return response;
            }
            boolean isVersionBanned = clientVersionService
                    .isVersionBanned(activateRequest.getClientVersion(), activateRequest.getBetta());
            ClientVersion clientVersion = clientVersionService.getLastVersion(activateRequest.getBetta());
            if (Objects.nonNull(clientVersion)) {
                response.setVersionCheckResult(fromUpdatePolicy(clientVersion, activateRequest.getClientVersion(), activateRequest.getBetta()));
                response.setUrlForUpdate(clientVersion.getLink());
                response.setReleaseNotes(clientVersion.getReleaseNotes());
                if (isVersionBanned) {
                    // required update
                    response.setVersionCheckResult(VersionCheckResult.Required);
                    response.setAccessType(AccessType.Denied);
                    response.setMessage("Client version is banned, please update!");
                    return response;
                }
            }

            AccessList accessList = accessListService.getAccessByClanOrUserName(activateRequest.getNickName(),
                    activateRequest.getClanName());
            if (Objects.nonNull(accessList)) {
                if (response.getAccessType() != AccessType.Denied) {
                    if (DateUtils.isDateEqualOrBeforeToday(accessList.getDueDate())) {
                        response.setAccessType(accessList.getAccessType());
                    } else {
                        response.setAccessType(AccessType.NoAccess);
                        response.setMessage("Timed out!");
                    }
                    response.setNickname(activateRequest.getNickName());
                    response.setClanName(activateRequest.getClanName());
                    response.setAccessEndDate(accessList.getDueDate());
                    response.setClanAccess(accessList.getClan());
                }
            } else {
                if (allowFreeFirstTimeUsage) {
                    AccessList newAccess = new AccessList();
                    newAccess.setAccessType(AccessType.Basic);
                    newAccess.setClan(false);
                    newAccess.setName(activateRequest.getNickName());
                    newAccess.setDueDate(DateUtils.addDay(freeDaysPeriod));
                    newAccess.setComments("Auto created free access account for '"
                            + activateRequest.getNickName() + "' from " + DATE_FORMAT.format(newAccess.getFromDate())
                            + " due " + DATE_FORMAT.format(newAccess.getDueDate()));
                    accessListService.create(newAccess);
                    response.setAccessType(AccessType.Basic);
                    response.setAccessEndDate(newAccess.getDueDate());
                }
            }
            log.info("Activate response: {}", response);
        } catch (Exception e) {
            response.setRecord(e);
            log.error("Error activating", e);
        }
        return response;
    }
}
