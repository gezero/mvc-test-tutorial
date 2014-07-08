package com.barcap.tutorial.controllers;

import com.barcap.tutorial.entities.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Jiri on 8. 7. 2014.
 */
@Controller
@RequestMapping("/accounts")
public class AccountController {

    @RequestMapping("{accountId}")
    public
    @ResponseBody
    Account getAccount(@PathVariable Long accountId) {
        Account account = new Account();
        account.setId(accountId);
        return account;
    }
}
