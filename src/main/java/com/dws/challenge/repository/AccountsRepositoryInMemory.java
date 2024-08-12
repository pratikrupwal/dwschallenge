package com.dws.challenge.repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.Transfer;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.exception.InvalidAccountException;
import com.dws.challenge.exception.InvalidAmountException;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void createAccount(Account account) throws DuplicateAccountIdException {
        Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
        if (previousAccount != null) {
            throw new DuplicateAccountIdException(
                    "Account id " + account.getAccountId() + " already exists!");
        }
    }

    @Override
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    @Override
    public void clearAccounts() {
        accounts.clear();
    }

	@Override
	public synchronized void transfer(Transfer transfer) throws InvalidAccountException, InvalidAmountException {
		// TODO Auto-generated method stub
		Account a1 = getAccount(transfer.getFromAccountId());
		if(a1==null) {
			throw new InvalidAccountException(
                    "Account id " + transfer.getFromAccountId() + " does not exist!");
		}
		Account a2 = getAccount(transfer.getToAccountId());
		if(a2==null) {
			throw new InvalidAccountException(
                    "Account id " + transfer.getToAccountId() + " does not exist!");
		}
		
		if(a1.getBalance().subtract(transfer.getAmount()).compareTo(BigDecimal.ZERO)<0) {
			throw new InvalidAmountException(
                    "Insufficient Amount in Account id " + transfer.getFromAccountId() + "!");
		}
		else {
			a1.setBalance(a1.getBalance().subtract(transfer.getAmount()));
			a2.setBalance(a2.getBalance().add(transfer.getAmount()));
		}
	}
}