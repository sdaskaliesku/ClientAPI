package com.client.controller.api;

import com.client.domain.db.ActivateRequest;
import com.client.domain.responses.Response;
import com.client.service.ActivateRequestService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
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

    private static Response buildSearchResponse(ActivateRequest first, List<ActivateRequest> requests) {
        Set<ActivateRequest> found = new LinkedHashSet<>();
        found.add(first);
        if (Objects.nonNull(first)) {
            for (ActivateRequest request : requests) {
                Set<String> macs = new LinkedHashSet<>(request.getMacAdresses());
                macs.add(request.getMacAddress());

                Set<String> ips = new LinkedHashSet<>(request.getIpAdresses());
                ips.add(request.getIpAddress());

                boolean hasMac = CollectionUtils.containsAny(macs, first.getMacAdresses());
                boolean hasIp = CollectionUtils.containsAny(ips, first.getIpAdresses());
                if (hasIp || hasMac) {
                    found.add(request);
                }
            }
        }
        Response response = new Response();
        Set<SearchResponse> searchResponses = found.stream().map(SearchResponse::new).collect(Collectors.toCollection(LinkedHashSet::new));
        response.setRecords(searchResponses);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/nickname", method = RequestMethod.GET)
    public Response byName(@RequestParam String name) {
        List<ActivateRequest> requests = activateRequestService.read();
        ActivateRequest first = requests.stream().filter(request -> request.getNickName().equalsIgnoreCase(name)).findFirst().orElse(null);
        if (Objects.isNull(first)) {
            return new Response();
        }
        return buildSearchResponse(first, requests);
    }

    @ResponseBody
    @RequestMapping(value = "/clanName", method = RequestMethod.GET)
    public Response byClanName(@RequestParam String name) {
        List<ActivateRequest> requests = activateRequestService.read();
        ActivateRequest first = requests.stream().filter(request -> request.getClanName().equalsIgnoreCase(name)).findFirst().orElse(null);
        if (Objects.isNull(first)) {
            return new Response();
        }
        return buildSearchResponse(first, requests);
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
