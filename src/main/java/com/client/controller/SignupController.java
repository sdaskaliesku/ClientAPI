package com.client.controller;

import com.client.domain.SignupRequest;
import com.client.domain.db.Account;
import com.client.service.UserService;
import com.client.support.web.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class SignupController {

    private static final String SIGNUP_VIEW_NAME = "signup/signup";
    private static final boolean SIGN_UP_ENABLED = true;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "signup")
    public String signup(Model model) {
        if (!SIGN_UP_ENABLED) {
            return null;
        }
        model.addAttribute(new SignupRequest());
        return SIGNUP_VIEW_NAME;
    }

    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public String signup(@Valid @ModelAttribute SignupRequest signupRequest, Errors errors, RedirectAttributes ra) {
        if (!SIGN_UP_ENABLED) {
            return null;
        }
        if (errors.hasErrors()) {
            return SIGNUP_VIEW_NAME;
        }
        Account account = userService.create(signupRequest.createAccount());
        userService.signin(account);
        // see /WEB-INF/i18n/messages.properties and /WEB-INF/views/homeSignedIn.html
        MessageHelper.addSuccessAttribute(ra, "signup.success");
        return "redirect:/";
    }
}
