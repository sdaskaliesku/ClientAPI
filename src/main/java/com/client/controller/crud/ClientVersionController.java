package com.client.controller.crud;

import com.client.domain.db.ClientVersion;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author sdaskaliesku
 */
@Controller
@RequestMapping("/crud/clientVersion")
@Secured({ "ROLE_ADMIN" })
public class ClientVersionController extends CRUDController<ClientVersion> {

}
