package com.client.controller.api;

import com.client.domain.UpdateRequest;
import com.client.domain.db.ActivateRequest;
import com.client.domain.db.ClientVersion;
import com.client.domain.enums.VersionCheckResult;
import com.client.domain.enums.UpdatePolicy;
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

    @Override
    @ResponseBody
    @RequestMapping(value = "/checkForUpdates", method = RequestMethod.GET)
    public Response checkForUpdates(@Valid @ModelAttribute UpdateRequest updateRequest) {
        log.info("New check for updates request: {}", updateRequest);
        ClientVersion lastVersion = clientVersionService.getLastVersion(updateRequest.getUpdateToBeta());
        UpdateResponse response = buildUpdateResponse(lastVersion, updateRequest);
        VersionCheckResult versionCheckResult = clientVersionService.getUpdateCheckResult(updateRequest, lastVersion);
        response.setVersionCheckResult(versionCheckResult);
        return response;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/getLastVersion", method = RequestMethod.GET)
    public Response getLastVersion(@RequestParam Boolean allowBetta) {
        log.info("New getLastVersion request: {}", allowBetta);
        ClientVersion clientVersion = clientVersionService.getLastVersion(allowBetta);
        Response response = new Response();
        response.setRecord(clientVersion);
        return response;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/getVersion", method = RequestMethod.GET)
    public Response getVersion(@RequestParam Double version, @RequestParam Boolean isBetta) {
        log.info("New getVersion request: {}/{}", version, isBetta);
        ClientVersion clientVersion = clientVersionService.getClientVersion(version, isBetta);
        Response response = new Response();
        if (clientVersion != null) {
            response.setRecord(clientVersion);
        } else {
            response.setMessage("Version: " + version + " with type: " + isBetta + " does not exists. Try again.");
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
    public Response activate(@Valid @ModelAttribute ActivateRequest activateRequest, HttpServletRequest request) throws Exception {
        throw new NotSupportedException();
    }

    private UpdateResponse buildUpdateResponse(ClientVersion clientVersion, UpdateRequest updateRequest) {
        UpdateResponse response = new UpdateResponse();
        response.setVersionCheckResult(getUpdateCheckResult(clientVersion, updateRequest));
        response.setUrl(clientVersion.getLink());
        response.setComments(clientVersion.getReleaseNotes());
        boolean betta = clientVersion.getBetta();
        double version = clientVersion.getVersion();
        if (updateRequest.getUserVersion() >= clientVersion.getVersion()) {
            betta = updateRequest.getBetta();
            version = updateRequest.getUserVersion();
        }
        response.setBetta(betta);
        response.setVersion(version);
        return response;
    }

    private static VersionCheckResult getUpdateCheckResult(ClientVersion clientVersion, UpdateRequest updateRequest) {
        if (clientVersion.getVersion() > updateRequest.getUserVersion()) {
            if (clientVersion.getUpdatePolicy().equals(UpdatePolicy.Required)) {
                return VersionCheckResult.Required;
            } else {
                return VersionCheckResult.Optional;
            }
        } else {
            if (clientVersion.getBetta()) {
                return VersionCheckResult.UpToDateBeta;
            } else {
                return VersionCheckResult.UpToDate;
            }
        }
    }

}
