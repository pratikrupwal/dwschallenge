package com.dws.challenge.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.Transfer;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.exception.InvalidAccountException;
import com.dws.challenge.exception.InvalidAmountException;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.EmailNotificationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

  private final AccountsService accountsService;
  private final EmailNotificationService emailService;

  @Autowired
  public AccountsController(AccountsService accountsService, EmailNotificationService emailService) {
    this.accountsService = accountsService;
	this.emailService = emailService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
    log.info("Creating account {}", account);

    try {
    this.accountsService.createAccount(account);
    } catch (DuplicateAccountIdException daie) {
      return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
  }
  
  @PostMapping("/transfer")
  public ResponseEntity<Object> transfer(@RequestBody @Valid Transfer transfer) {
	    log.info("Processing transfer");
	    try {
	    	this.accountsService.transferAmount(transfer);
	    	this.emailService.notifyAboutTransfer(getAccount(transfer.getFromAccountId()), 
	    			"Transferred amount "+transfer.getAmount()+" to Account Id : "+transfer.getToAccountId());
	    	this.emailService.notifyAboutTransfer(getAccount(transfer.getToAccountId()), 
	    			"Received amount "+transfer.getAmount()+" from Account Id : "+transfer.getFromAccountId());
	    	
	    } catch (InvalidAmountException iae) {
	    	this.emailService.notifyAboutTransfer(getAccount(transfer.getFromAccountId()), 
	    			"Insufficient Amount to Transfer amount "+transfer.getAmount());
	      return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
	    }
	    catch (InvalidAccountException iae) {
	    	return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
		}
	    return new ResponseEntity<>(HttpStatus.OK);
	  }

  @GetMapping(path = "/{accountId}")
  public Account getAccount(@PathVariable String accountId) {
    log.info("Retrieving account for id {}", accountId);
    return this.accountsService.getAccount(accountId);
  }
}