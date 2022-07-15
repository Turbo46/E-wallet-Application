package com.hanabank.controller;

import com.hanabank.model.Account;
import com.hanabank.model.Transcation;
import com.hanabank.model.Transfer;
import com.hanabank.model.User;

import com.hanabank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin
@RestController
public class AccountsController {

    @Autowired
    AccountRepository accountRepository;

    @PostMapping("/signup")
    public ResponseEntity create (@RequestBody User user) {

        String email = user.getEmail();

        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        Account userByEmail = accountRepository.findByEmail(email);

        if(!matcher.matches()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not valid");
        }

        if(userByEmail == null){
            Account newAccount = new Account(email);
            accountRepository.save(newAccount);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(newAccount);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email Already used");
    }

    @PostMapping("/signin")
    public ResponseEntity login(@RequestBody User user) {

        String email = user.getEmail();

        if (email == null || email =="") {
            return ResponseEntity.notFound().build();
        }

        Account userByEmail = accountRepository.findByEmail(email);

        if (userByEmail == null) {
                return ResponseEntity.notFound().build();
            }

        return ResponseEntity.accepted().build();
    }

    @PutMapping("/top-up")
    public ResponseEntity addMoney(@RequestBody Transcation transcation) {

        String emailTarget = transcation.getEmail();
        double addBalance = transcation.getBalance();

        Account userByEmail = accountRepository.findByEmail(emailTarget);

        if(userByEmail==null){
            return ResponseEntity.notFound().build();
        }

        if(addBalance < 0){
            return ResponseEntity.badRequest().build();
        }

        userByEmail.setBalance(addBalance);
        accountRepository.save(userByEmail);
        return ResponseEntity.status(HttpStatus.OK).body(userByEmail);
    }

    @PutMapping("/transfer")
    public ResponseEntity transfer(@RequestBody Transfer transfer) {

        String emailSource = transfer.getEmailSource();
        double balanceSource = transfer.getBalanceSource();

        String emailTarget = transfer.getEmailTarget();

        Account sourceByEmail = accountRepository.findByEmail(emailSource);

        double currentBalance = sourceByEmail.getBalance();

        Account targetByEmail = accountRepository.findByEmail(emailTarget);

        if(sourceByEmail == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Your account does not exist");
        }

        if(targetByEmail == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("email recipient not found");
        }

        if(targetByEmail == null && sourceByEmail == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email recipient and destiny was not found");
        }

        if (balanceSource < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid value");
        }

        if(currentBalance < balanceSource) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your balance is not enough");
        } else {
            sourceByEmail.setBalance(-balanceSource);
            targetByEmail.setBalance(balanceSource);
            accountRepository.save(sourceByEmail);
            accountRepository.save(targetByEmail);
            return ResponseEntity.status(HttpStatus.OK).body(targetByEmail);
        }
    }

    @GetMapping("/check-balances/{email}")
    public ResponseEntity checkBalance(@PathVariable ("email") String email) {

        Account checkByEmail = accountRepository.findByEmail(email);

        if (checkByEmail == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (email.equals(checkByEmail.getEmail())) {
            double showBalance = checkByEmail.getBalance();
            return new ResponseEntity<>(showBalance, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This account does not exist");
    }
}

