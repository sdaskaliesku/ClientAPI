package com.client.controller.api;

import com.client.domain.UpdateRequest;
import com.client.domain.db.ActivateRequest;
import com.client.domain.responses.Response;
import com.client.service.AccessListService;
import com.client.service.ActivateRequestService;
import com.client.service.BlackListService;
import com.client.service.ClientVersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author sdaskaliesku
 */
@Controller
public abstract class ApiController {

    protected final Logger log = LoggerFactory.getLogger(AccessListApiController.class);
    protected static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    // TODO: do this jmx-able
    protected Boolean allowFreeFirstTimeUsage = false;
    protected Integer freeDaysPeriod = 1;

    @Autowired
    ClientVersionService clientVersionService;

    @Autowired
    AccessListService accessListService;

    @Autowired
    ActivateRequestService activateRequestService;

    @Autowired
    BlackListService blackListService;

    @ResponseBody
    @RequestMapping(value = "/checkForUpdates", method = RequestMethod.GET)
    public abstract Response checkForUpdates(@Valid @ModelAttribute UpdateRequest updateRequest) throws Exception;

    @ResponseBody
    @RequestMapping(value = "/getLastVersion", method = RequestMethod.GET)
    public abstract Response getLastVersion(@RequestParam Boolean allowBetta) throws Exception;

    @ResponseBody
    @RequestMapping(value = "/getVersion", method = RequestMethod.GET)
    public abstract Response getVersion(@RequestParam Double version, @RequestParam Boolean isBetta) throws Exception;

    @ResponseBody
    @RequestMapping(value = "/getAllVersions", method = RequestMethod.GET)
    public abstract Response getAllVersions() throws Exception;

    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    @ResponseBody
    public abstract String activate(HttpServletRequest httpServletRequest);

    public abstract Response activate(ActivateRequest activateRequest, HttpServletRequest request) throws Exception;

    public void setClientVersionService(ClientVersionService clientVersionService) {
        this.clientVersionService = clientVersionService;
    }

    public void setAccessListService(AccessListService accessListService) {
        this.accessListService = accessListService;
    }

    public void setActivateRequestService(ActivateRequestService activateRequestService) {
        this.activateRequestService = activateRequestService;
    }

    public void setBlackListService(BlackListService blackListService) {
        this.blackListService = blackListService;
    }

    public Boolean getAllowFreeFirstTimeUsage() {
        return allowFreeFirstTimeUsage;
    }

    public void setAllowFreeFirstTimeUsage(Boolean allowFreeFirstTimeUsage) {
        this.allowFreeFirstTimeUsage = allowFreeFirstTimeUsage;
    }

    public Integer getFreeDaysPeriod() {
        return freeDaysPeriod;
    }

    public void setFreeDaysPeriod(Integer freeDaysPeriod) {
        this.freeDaysPeriod = freeDaysPeriod;
    }

    public static DateFormat getDateFormat() {
        return DATE_FORMAT;
    }
}
