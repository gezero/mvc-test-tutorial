package com.barcap.tutorial.services;

import com.barcap.tutorial.entities.Account;

/**
 * Created by Jiri on 8. 7. 2014.
 */
public interface AccountService {
    Account findOne(Long id);
}
