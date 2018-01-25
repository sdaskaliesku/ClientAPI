package com.client.controller.crud;

import com.client.domain.db.FreeFunctions;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/crud/freeFunctions")
@Secured({ "ROLE_USER", "ROLE_ADMIN" })
public class FreeFunctionsController extends CRUDController<FreeFunctions> {
}
