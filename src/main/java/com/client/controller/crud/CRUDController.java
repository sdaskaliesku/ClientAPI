package com.client.controller.crud;

import com.client.domain.responses.Response;
import com.client.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author sdaskaliesku
 */
@Controller
public abstract class CRUDController<T> {

    @Autowired
    protected AbstractService<T> service;

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public Response create(@ModelAttribute T t) {
        Response Response = new Response();
        T newT = service.create(t);
        Response.setRecord(newT);
        return Response;
    }


    /**
     * Response should be in format: {"Result":"OK", "Records":[{...},...]}
     * Or {"Result":"ERROR", "Message":""}
     * @return list of objects as json
     */
    @ResponseBody
    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public  Response read() {
        Response Response = new Response();
        List<T> result = service.read();
        Response.setRecords(result);
        return Response;
    }

    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public Response update(@ModelAttribute T t) {
        service.update(t);
        return new Response();
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Response delete(Long id) {
        service.delete(id);
        return new Response();
    }

}
