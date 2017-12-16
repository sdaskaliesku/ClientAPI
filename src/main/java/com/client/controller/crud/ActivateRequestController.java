package com.client.controller.crud;

import com.client.domain.db.ActivateRequest;
import com.client.domain.responses.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author sdaskaliesku
 */
@Controller
@RequestMapping("/crud/activateRequest")
public class ActivateRequestController extends CRUDController<ActivateRequest> {

    private Response response;

    public ActivateRequestController() {
        response = new Response();
        response.setResult(Response.RESULT_ERROR);
        response.setMessage("You are not allowed to change this entity");
    }

    @Override
    public Response create(@ModelAttribute ActivateRequest activateRequest) {
        return response;
    }

    @Override
    public Response update(@ModelAttribute ActivateRequest activateRequest) {
        return response;
    }

    @Override
    public Response delete(Long id) {
        return response;
    }
}
