package com.client.controller.api;

import com.client.domain.db.ActivateRequest;
import com.client.domain.responses.Response;
import com.client.service.ActivateRequestService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    ActivateRequestService activateRequestService;

    private static Set<String> getMacAddressList(ActivateRequest request) {
        Set<String> addressSet = new LinkedHashSet<>(request.getMacAdresses());
        addressSet.add(request.getMacAddress());
        return addressSet.stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());
    }

    private static Set<String> getIpAddressList(ActivateRequest request) {
        Set<String> addressSet = new LinkedHashSet<>(request.getIpAdresses());
        addressSet.add(request.getIpAddress());
        return addressSet.stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());
    }

    private static Set<ActivateRequest> findIntersections(ActivateRequest base, List<ActivateRequest> requests) {
        Set<ActivateRequest> found = new LinkedHashSet<>();

        if (Objects.nonNull(base)) {
            found.add(base);
            Set<String> firstMac = getMacAddressList(base);
            Set<String> firstIps = getIpAddressList(base);
            requests.forEach(request -> {
                Set<String> macs = getMacAddressList(request);
                Set<String> ips = getIpAddressList(request);
                boolean hasMac = CollectionUtils.containsAny(macs, firstMac);
                boolean hasIp = CollectionUtils.containsAny(ips, firstIps);
                if (hasIp || hasMac) {
                    found.add(request);
                }
            });
        }
        return found;
    }

    private static Response buildSearchResponse(Collection<ActivateRequest> requests) {
        Response response = new Response();
        Set<SearchResponse> searchResponses = requests.stream().map(SearchResponse::new).collect(Collectors.toCollection(LinkedHashSet::new));
        response.setRecords(searchResponses);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/nickname", method = RequestMethod.GET)
    public Response byName(@RequestParam String name) {
        List<ActivateRequest> requests = activateRequestService.read();
        ActivateRequest first = requests.stream().filter(request -> StringUtils.equalsIgnoreCase(name, request.getNickName())).findFirst().orElse(null);
        if (Objects.isNull(first)) {
            return new Response();
        }
        Set<ActivateRequest> set = findIntersections(first, requests);
        return buildSearchResponse(set);
    }

    @ResponseBody
    @RequestMapping(value = "/clanName", method = RequestMethod.GET)
    public Response byClanName(@RequestParam String name) {
        List<ActivateRequest> requests = activateRequestService.read();
        Set<ActivateRequest> baseUsers = requests.stream().filter(request -> StringUtils.equalsIgnoreCase(name, request.getClanName())).collect(Collectors.toCollection(LinkedHashSet::new));
        if (CollectionUtils.isEmpty(baseUsers)) {
            return new Response();
        }
        Set<ActivateRequest> responses = new LinkedHashSet<>();
        baseUsers.stream().map(first -> findIntersections(first, requests)).forEach(responses::addAll);
        return buildSearchResponse(responses);
    }

    public static class SearchResponse {
        private String name;
        private String clanName;
        private Set<String> ipAdresses;
        private Set<String> macAdresses;
        private Date date;


        public SearchResponse(ActivateRequest activateRequest) {
            this.name = activateRequest.getNickName();
            this.clanName = activateRequest.getClanName();
            this.ipAdresses = new LinkedHashSet<>();
            this.ipAdresses.add(activateRequest.getIpAddress());
            this.ipAdresses.addAll(activateRequest.getIpAdresses());
            this.macAdresses = new LinkedHashSet<>();
            this.macAdresses.add(activateRequest.getMacAddress());
            this.macAdresses.addAll(activateRequest.getMacAdresses());
            this.date = activateRequest.getDate();
        }

        public String getName() {
            return name;
        }

        public String getClanName() {
            return clanName;
        }

        public Set<String> getIpAdresses() {
            return ipAdresses;
        }

        public Set<String> getMacAdresses() {
            return macAdresses;
        }

        public Date getDate() {
            return date;
        }
    }
}
