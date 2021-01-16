package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.InsufficientBalanceException;
import com.techelevator.tenmo.model.Transfer;

public interface AccountDAO {
	
	Account getAccountByAccountId(int id);
	
	Account getAccountByUserId(int id);

//	void createAccount(Account account);
//
////	void deleteAccount(int id);

	Transfer sendBucks(Transfer transfer) throws InsufficientBalanceException;
	
	Transfer requestBucks(Transfer transfer);

	Transfer rejectBucks(Transfer transfer);
	
}
