package com.client.controller.api;

import com.client.domain.UpdateRequest;
import com.client.domain.db.ActivateRequest;
import com.client.domain.db.ClientVersion;
import com.client.domain.enums.UpdatePolicy;
import com.client.domain.enums.VersionCheckResult;
import com.client.domain.responses.Response;
import com.client.domain.responses.UpdateResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.NotSupportedException;
import javax.validation.Valid;

/**
 * @author sdaskaliesku
 */
@Controller
@RequestMapping("/api/version")
public class ClientVersionApiController extends ApiController {

    private static VersionCheckResult getUpdateCheckResult(ClientVersion clientVersion, UpdateRequest updateRequest) {
        if (clientVersion.getVersion() > updateRequest.getUserVersion()) {
            if (clientVersion.getUpdatePolicy().equals(UpdatePolicy.Required)) {
                return VersionCheckResult.Required;
            } else {
                return VersionCheckResult.Optional;
            }
        } else {
            return VersionCheckResult.UpToDate;
        }
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/checkForUpdates", method = RequestMethod.GET)
    public Response checkForUpdates(@Valid @ModelAttribute UpdateRequest updateRequest) {
        log.info("New check for updates request: {}", updateRequest);
        ClientVersion lastVersion = clientVersionService.getLastVersion();
        UpdateResponse response = buildUpdateResponse(lastVersion, updateRequest);
        VersionCheckResult versionCheckResult = clientVersionService.getUpdateCheckResult(updateRequest, lastVersion);
        response.setVersionCheckResult(versionCheckResult);
        return response;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/getLastVersion", method = RequestMethod.GET)
    public Response getLastVersion() {
        log.info("New getLastVersion request");
        ClientVersion clientVersion = clientVersionService.getLastVersion();
        Response response = new Response();
        response.setRecord(clientVersion);
        return response;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/getVersion", method = RequestMethod.GET)
    public Response getVersion(@RequestParam Double userVersion) {
        log.info("New getVersion request: {}", userVersion);
        ClientVersion clientVersion = clientVersionService.getClientVersion(userVersion);
        Response response = new Response();
        if (clientVersion != null) {
            response.setRecord(clientVersion);
        } else {
            response.setMessage("Version: " + userVersion + " does not exists. Try again.");
        }
        return response;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/getAllVersions", method = RequestMethod.GET)
    public Response getAllVersions() {
        log.info("New getAllVersions request");
        Response response = new Response();
        response.setRecords(clientVersionService.getAllAllowedVersions());
        return response;
    }

    @Override
    public String activate(HttpServletRequest httpServletRequest) {
        return null;
    }

    @Override
    public Response activate(@Valid @ModelAttribute ActivateRequest activateRequest, HttpServletRequest request) throws Exception {
        throw new NotSupportedException();
    }

    private UpdateResponse buildUpdateResponse(ClientVersion clientVersion, UpdateRequest updateRequest) {
        UpdateResponse response = new UpdateResponse();
        response.setVersionCheckResult(getUpdateCheckResult(clientVersion, updateRequest));
        response.setUrl(clientVersion.getLink());
        response.setComments(clientVersion.getReleaseNotes());
        double version = clientVersion.getVersion();
        if (updateRequest.getUserVersion() >= clientVersion.getVersion()) {
            version = updateRequest.getUserVersion();
        }
        response.setVersion(version);
        return response;
    }

}
