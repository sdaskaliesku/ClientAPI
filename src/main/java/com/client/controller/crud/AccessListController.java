package com.client.controller.crud;

import com.client.domain.db.AccessList;
import com.client.domain.responses.Response;
import com.client.service.AccessListService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author sdaskaliesku
 */
@Controller
@RequestMapping("/crud/accessList")
public class AccessListController extends CRUDController<AccessList> {

    @ResponseBody
    @RequestMapping(value = "/clans", method = RequestMethod.GET)
    public Response getAllClans() {
        Response response = new Response();
        response.setRecords(getService().getAllClans());
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public Response getAllUsers() {
        Response response = new Response();
        response.setRecords(getService().getAllUsers());
        return response;
    }

    private AccessListService getService() {
        return (AccessListService) service;
    }

}
