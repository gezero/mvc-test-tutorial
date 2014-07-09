package com.barcap.tutorial.controllers;

import com.barcap.tutorial.entities.Account;
import com.barcap.tutorial.services.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
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

/**
 * This tests shows how mockMVC, jsonPath and Mockito integrate well together.
 *
 * Among other things note that password is serialized only on the way from client to server when registering new
 * account. Password is not serialized when information about account are requested.
 *
 * Note how we check that the password is properly serialized when registering new account by capturing the data passed
 * to the mocked AccountService using the captor entity.
 */
public class AccountControllerTest {

    @InjectMocks
    AccountController controller;
    MockMvc mockMvc;

    @Mock
    AccountService service;

    @Captor
    ArgumentCaptor<Account> captor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    /**
     * Among other things this test tests, that the password is not serialized on the way from server to client
     */
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
                .andExpect(jsonPath("$.password", is(nullValue())))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void testGetNonExistingAccount() throws Exception {

        mockMvc.perform(get("/accounts/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    /**
     * Among other things this test tests that the password is properly serialized from the input JSON object.
     */
    @Test
    public void testCreateNewAccount() throws Exception {
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

        verify(service).createAccount(captor.capture());

        assertThat(captor.getValue().getUserName(), is("username"));
        assertThat(captor.getValue().getPassword(), is("password"));


    }
}