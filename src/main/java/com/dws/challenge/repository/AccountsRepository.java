package com.dws.challenge.repository;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.Transfer;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.exception.InvalidAccountException;
import com.dws.challenge.exception.InvalidAmountException;

public interface AccountsRepository {

  void createAccount(Account account) throws DuplicateAccountIdException;
  
  void transfer(Transfer transfer) throws InvalidAccountException, InvalidAmountException;

  Account getAccount(String accountId);

  void clearAccounts();
}
