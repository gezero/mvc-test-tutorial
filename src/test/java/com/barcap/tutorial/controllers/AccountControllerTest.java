package com.barcap.tutorial.controllers;

import com.barcap.tutorial.entities.Account;
import com.barcap.tutorial.services.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest {

    @InjectMocks
    AccountController controller;
    MockMvc mockMvc;

    @Mock
    AccountService service;

    ArgumentCaptor<Account> captor;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        captor = ArgumentCaptor.forClass(Account.class);
    }


    @Test
    public void testGetExistingAccount() throws Exception {
        Account account = new Account();
        account.setId(1L);
        account.setUserName("username");
        account.setPassword("password");


        when(service.findOne(1L)).thenReturn(account);


        mockMvc.perform(get("/accounts/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password",is(nullValue())))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void testGetNonExistingAccount() throws Exception {

        mockMvc.perform(get("/accounts/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void testRegisterAccount() throws Exception {
        Account account = new Account();
        account.setId(1L);
        account.setUserName("username");
        account.setPassword("password");


        when(service.createAccount(any(Account.class))).thenReturn(account);


        mockMvc.perform(post("/accounts")
                        .content("{\"id\":1,\"userName\":\"username\",\"password\":\"password\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());

//        verify(service).createAccount(any(Account.class));
        verify(service).createAccount(captor.capture());

        assertThat(captor.getValue().getUserName(),is("username"));
        assertThat(captor.getValue().getPassword(),is("password"));


    }
}