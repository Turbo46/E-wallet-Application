package com.hanabank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanabank.model.Account;
import com.hanabank.repository.AccountRepository;
import org.hamcrest.CoreMatchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountsController.class)
class AccountsControllerTest {
    private static ObjectMapper mapper;

    @BeforeAll
    public static void init() {
        mapper = new ObjectMapper();
    }

    @Autowired
    MockMvc mvc;

    @MockBean
    private AccountRepository accountRepository;

    @Test
    void create_shouldCreateAccountWhenRegisterWithValidEmail() throws Exception {

        String validEmail = "alan@gmail.com";
        Account account = new Account(validEmail);

        Mockito.when(accountRepository.findByEmail(validEmail)).thenReturn(null);
        Mockito.when(accountRepository.save(account)).thenReturn(account);
        this.mvc
                .perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(account)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isAccepted());
    }

    @Test
    void create_shouldNotCreatedAccountGivenInvalidEmail() throws Exception {

        String invalidEmail = ".bakaba.baka.@gmail.com";
        Account account = new Account(invalidEmail);

        Mockito.when(accountRepository.findByEmail(invalidEmail)).thenReturn(null);
        Mockito.when(accountRepository.save(account)).thenReturn(account);
        this.mvc
                .perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(account)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldNotLoginWhenNotGivenAnEmail () throws Exception {

        Account account =new Account();
        Mockito.when(accountRepository.findByEmail(account.getEmail())).thenReturn(null);
        this.mvc
                .perform(MockMvcRequestBuilders.post("/signin")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(account)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    void addMoney_shouldAddMoneyWhenGivenExistEmail() throws Exception {
        Account account1 = new Account("bakabakayow@gmail.com");
        double addBalance = 5000;

        Account account2 = new Account("bakabakayow@gmail.com");
        account2.setBalance(5000);

        this.mvc
                .perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(account1)));
        this.mvc
                .perform(MockMvcRequestBuilders.put("/top-up")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(account2)));
        //dont have get api to see is there balance or not
        // form should be like this
        this.mvc
                .perform(MockMvcRequestBuilders.get("/display-data")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(account2)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", IsCollectionWithSize.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", CoreMatchers.is("bakabakayow@gmail.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].balance", CoreMatchers.is(5000)));
    }

    @Test
    void transfer_shouldAddTargetEmailBalance_decreaseSourceEmail() throws Exception {
        String sourceEmail = "bakabakayow@gmail.com";
        String targetEmail = "jackson@gmail.com";

        double balance = 5000;

        Account source = new Account(sourceEmail);
        source.setBalance(5000);

        Account target = new Account(targetEmail);

        Account sourceByEmail= accountRepository.findByEmail(sourceEmail);
        Account targetByEmail= accountRepository.findByEmail(targetEmail);

        sourceByEmail.setBalance(-5000);
        targetByEmail.setBalance(5000);

        Assertions.assertEquals(0,sourceByEmail.getBalance());
        Assertions.assertEquals(5000,targetByEmail.getBalance());

        Mockito.when(accountRepository.save(source)).thenReturn(source);
        this.mvc
                .perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(sourceEmail)))
                .andDo(MockMvcResultHandlers.print());

        Mockito.when(accountRepository.save(target)).thenReturn(target);
        this.mvc
                .perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(sourceEmail)))
                .andDo(MockMvcResultHandlers.print());
        // do top up
        //get data
        //match data

    }

}