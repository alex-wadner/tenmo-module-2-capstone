package com.techelevator.tenmo.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class AccountTest {



	@Test
	public void id_expect_2_return_2() {
		Account account = new Account();
		long val = 2;
		account.setAccountId(val);
		assertEquals(val, account.getAccountId());
	}

	@Test
	public void userId_expect_5_return_5() {
		Account account = new Account();
		account.setUserId(5);
		assertEquals(5, account.getUserId());
	}
	
	@Test
	public void balance_expect_100_return_100() {
		Account account = new Account();
		BigDecimal actual = new BigDecimal("100");
		
		account.setBalance(actual);
		assertEquals(actual,account.getBalance());
		
	}
	
	@Test
	public void toString_expect_True() {
		long id = 2;
		int userId = 5;
		BigDecimal balance = new BigDecimal("100");

		Account account = new Account(id, userId,balance);
		String expected = "Account: " + id + "\nUser ID: " + userId + "\n Current balance: " + balance;
		assertEquals(account.toString(),expected);

	}

}
