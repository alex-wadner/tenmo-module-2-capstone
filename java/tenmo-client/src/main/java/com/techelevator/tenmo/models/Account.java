package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Account {

	private Long id;
	private int userId;
	private BigDecimal balance;
	
	public Account() {
	}
	
	public Account(Long accountId, int userId, BigDecimal balance) {
		this.id = accountId;
		this.userId = userId;
		this.balance = balance;
	}

	public Long getAccountId() {
		return id;
	}

	public void setAccountId(Long accountId) {
		this.id = accountId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "Account: " + id + "\nUser ID: " + userId + "\n Current balance: " + balance;
	}
	
	
}
