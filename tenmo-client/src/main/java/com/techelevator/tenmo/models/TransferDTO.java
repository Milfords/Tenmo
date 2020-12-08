package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class TransferDTO {

		private BigDecimal amount;
		private int accountTo;
		
		public BigDecimal getAmount() {
			return amount;
		}
		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}
		public int getAccountTo() {
			return accountTo;
		}
		public void setAccountTo(int accountTo) {
			this.accountTo = accountTo;
	}
		
}
