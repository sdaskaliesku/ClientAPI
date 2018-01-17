package com.client.controller;

import com.client.domain.db.Account;
import com.client.domain.responses.Response;
import com.client.repository.AccountRepository;
import com.client.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.Principal;

@Controller
@Secured("ROLE_ADMIN")
public class AccountController {

    private AccountRepository accountRepository;
    @Qualifier("accountService")
    @Autowired
    protected AbstractService<Account> service;

    @Autowired
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Secured("ROLE_USER")
    @RequestMapping(value = "account/current", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Account accounts(Principal principal) {
        Assert.notNull(principal);
        return accountRepository.findByName(principal.getName());
    }

    @ResponseBody
    @RequestMapping("/crud/account/create")
    public Response create(Account account) {
        service.create(account);
        return new Response();
    }

    @ResponseBody
    @RequestMapping("/crud/account/read")
    public Response read() {
        Response response = new Response();
        response.setRecords(service.read());
        return response;
    }

    @ResponseBody
    @RequestMapping("/crud/account/update")
    public Response update(Account account) {
        service.update(account);
        return new Response();
    }

    @ResponseBody
    @RequestMapping("/crud/account/delete")
    public Response delete(Long id) {
        service.delete(id);
        return new Response();
    }
}
