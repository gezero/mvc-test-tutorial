package com.barcap.tutorial.controllers;

import com.barcap.tutorial.entities.Account;
import com.barcap.tutorial.exceptions.NotFoundException;
import com.barcap.tutorial.services.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Jiri on 8. 7. 2014.
 */
@Controller
@RequestMapping("/accounts")
public class AccountController {

    AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    /**
     * This method is responsible for retrieving information about account.
     */
    @RequestMapping("{accountId}")
    public
    @ResponseBody
    Account getAccount(@PathVariable Long accountId) {

        Account account = service.findOne(accountId);
        if (account != null) {
            return account;
        }
        throw new NotFoundException();
    }


    /**
     * This method is responsible for creating new account
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public
    @ResponseBody
    Account createNewAccount(@RequestBody Account account) {
        return service.createAccount(account);
    }
}
