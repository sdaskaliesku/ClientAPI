package com.client.controller.crud;

import com.client.domain.db.CryptoKey;
import com.client.domain.responses.Response;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Date;

@Controller
@RequestMapping("/crud/cryptoKey")
@Secured({"ROLE_ADMIN"})
public class CryptoKeyController extends CRUDController<CryptoKey> {

    @Override
    public Response create(CryptoKey cryptoKey) {
        cryptoKey.setDate(new Date(new java.util.Date().getTime()));
        return super.create(cryptoKey);
    }

    @Override
    public Response update(CryptoKey cryptoKey) {
        cryptoKey.setDate(new Date(new java.util.Date().getTime()));
        return super.update(cryptoKey);
    }
}
