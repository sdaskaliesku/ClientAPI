package com.client.controller.crud;

import com.client.domain.db.BlackList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author sdaskaliesku
 */
@Controller
@RequestMapping("/crud/blackList")
public class BlackListController extends CRUDController<BlackList> {

}
