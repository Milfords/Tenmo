package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Account {

	private int accountId;
	private int userId;
	private BigDecimal balance;
	
	public Account(int accountId, int userId, BigDecimal balance) {
		this.accountId = accountId;
		this.balance = balance;
		this.userId = userId;
	}
	
	public Account() {
		
	}

	public int getAmountId() {
		return accountId;
	}

	public void setAmountId(int amountId) {
		this.accountId = amountId;
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
	
	
	
}
