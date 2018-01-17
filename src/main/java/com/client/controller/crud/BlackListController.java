package com.client.controller.crud;

import com.client.domain.db.BlackList;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author sdaskaliesku
 */
@Controller
@RequestMapping("/crud/blackList")
@Secured({ "ROLE_USER", "ROLE_ADMIN" })
public class BlackListController extends CRUDController<BlackList> {

}
