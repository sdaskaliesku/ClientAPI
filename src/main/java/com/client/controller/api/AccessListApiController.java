package com.client.controller.api;

import com.client.domain.UpdateRequest;
import com.client.domain.db.AccessList;
import com.client.domain.db.ActivateRequest;
import com.client.domain.db.ClientVersion;
import com.client.domain.enums.AccessType;
import com.client.domain.enums.VersionCheckResult;
import com.client.domain.responses.ActivateResponse;
import com.client.domain.responses.Response;
import com.client.utils.DateUtils;
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
import java.util.List;
import java.util.Objects;

/**
 * @author sdaskaliesku
 */
@Controller
@RequestMapping("/api/access")
public class AccessListApiController extends ApiController {
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

    private ActivateRequest getActivateRequestToLog(ActivateRequest activateRequest) {
        if (Objects.nonNull(activateRequest)) {
            List<ActivateRequest> list = activateRequestService.read();
            if (list.stream()
                    .filter(Objects::nonNull)
//                    filter out just null names, not empty
                    .filter(x -> Objects.nonNull(x.getClanName()))
                    .filter(x -> Objects.nonNull(x.getNickName()))
                    .noneMatch(x -> x.getClanName().equalsIgnoreCase(activateRequest.getClanName()) && x.getNickName().equalsIgnoreCase(activateRequest.getNickName()))) {
                return activateRequest;
            }
        }
        return null;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    public ActivateResponse activate(@Valid @ModelAttribute ActivateRequest activateRequest, HttpServletRequest request) {
        try {
            activateRequest.setIpAddress(getIpAddress(request));
        } catch (Exception e) {
            // do nothing
        }
        ActivateResponse response = new ActivateResponse();
        try {
            log.info("New activate request: {}", activateRequest);
            // log request to db
            ActivateRequest requestToLog = getActivateRequestToLog(activateRequest);
            if (Objects.nonNull(requestToLog)) {
                activateRequestService.create(activateRequest);
            }
            if (blackListService.isClanInBlackList(activateRequest.getClanName())) {
                // access denied
                response.setAccessType(AccessType.Denied);
                response.setMessage("Clan is banned!");
                return response;
            }
            if (blackListService.isUserInBlackList(activateRequest.getNickName())) {
                // access denied
                response.setAccessType(AccessType.Denied);
                response.setMessage("User is banned!");
                return response;
            }
            boolean isVersionBanned = clientVersionService
                    .isVersionBanned(activateRequest.getClientVersion(), activateRequest.getBetta());
            if (isVersionBanned) {
                ClientVersion clientVersion = clientVersionService.getLastVersion(activateRequest.getBetta());
                // required update
                response.setRecord(clientVersion.getLink());
                response.setVersionCheckResult(VersionCheckResult.Required);
                response.setAccessType(AccessType.Denied);
                response.setMessage("Client version is banned, please update!");
                return response;
            }

            AccessList accessList = accessListService.getAccessByClanOrUserName(activateRequest.getNickName(),
                    activateRequest.getClanName());
            if (accessList != null) {
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
            } /*else {
                // TODO: do this jmx-able - first time use - free period
                if (!isClanInBlackList && !isUserInBlackList && !isVersionBanned) {
                    if (allowFreeFirstTimeUsage) {
                        AccessList newAccess = new AccessList();
                        newAccess.setAccessType(AccessType.Basic);
                        newAccess.setClanAccess(false);
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
            }*/
            log.info("Activate response: {}", response);
        } catch (Exception e) {
            response.setRecord(e);
        }
        return response;
    }

    private static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (StringUtils.isEmpty(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
