package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


import com.techelevator.tenmo.model.AccountTransfer;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.TransferDTO;

@Component
public class AccountsSqlDAO implements AccountsDAO {
	private UserDAO dao;
	private JdbcTemplate jdbcTemplate;
	
	public AccountsSqlDAO(JdbcTemplate jdbcTemplate, UserDAO dao) {
		this.dao = dao;
        this.jdbcTemplate = jdbcTemplate;
    }
	
	@Override
	public void transferMoneyTotal(AccountTransfer transfer) {
		transferMoney(transfer);
		transferHistory(transfer);
	}

	public void transferMoney(AccountTransfer transfer) {
		String sql = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?";
		jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccountFrom());
		
		String sql2 = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?";
		jdbcTemplate.update(sql2, transfer.getAmount(), transfer.getAccountTo());
	}
	
	public void transferHistory(AccountTransfer transfer) {
		String sql = "INSERT INTO transfers(transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
				"VALUES(DEFAULT, ?, ?, ?, ?, ?) ";
		jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
	}


	@Override
	public BigDecimal getBalance(int userId) {
		BigDecimal balance = null;
		String sql = "SELECT balance FROM accounts WHERE account_id = ?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
		if (result.next()) {
		balance = result.getBigDecimal(1);
		}
		return balance;
	}
	
	public List<AccountTransfer> getTransferHistory(Principal principal) {
		List<AccountTransfer> transferList = new ArrayList<>();
		
		String sql = "SELECT transfers.transfer_id, users.username, transfers.amount FROM transfers " + 
				"JOIN accounts ON accounts.account_id = transfers.account_from " + 
				"LEFT OUTER JOIN users ON users.user_id = accounts.user_id " + 
				"WHERE transfers.account_to = ?";
		int userId = dao.findIdByUsername(principal.getName());
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
		while(results.next()) {
			AccountTransfer theTransfers = new AccountTransfer();
			theTransfers.setTransferId(results.getInt("transfer_id"));
			theTransfers.setOtherUser(results.getString("username"));
			theTransfers.setAmount(results.getBigDecimal("amount"));
			transferList.add(theTransfers);
		}
		return transferList;
	}
	@Override
	public List<AccountTransfer> getTransferHistoryReceived(Principal principal) {
		List<AccountTransfer> transferList = new ArrayList<>();
		String sql = "SELECT transfers.transfer_id, users.username, transfers.amount FROM transfers " + 
				"JOIN accounts ON accounts.account_id = transfers.account_to " + 
				"LEFT OUTER JOIN users ON users.user_id = accounts.user_id " + 
				"WHERE transfers.account_from = ?";
		
		int userId = dao.findIdByUsername(principal.getName());
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
		while(results.next()) {
			AccountTransfer theTransfers = new AccountTransfer();
			theTransfers.setTransferId(results.getInt("transfer_id"));
			theTransfers.setOtherUser(results.getString("username"));
			theTransfers.setAmount(results.getBigDecimal("amount"));
			transferList.add(theTransfers);
		}
		return transferList;
	}
	
	
	
	@Override
	public List<AccountTransfer> getTransferDetails(Long userId, int transferId) {
		List<AccountTransfer> transferDetails = new ArrayList<>();
		String sql = "SELECT * FROM transfers  JOIN users ON transfers.account_from = users.user_id WHERE user_id = ? AND transfer_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, transferId);
		while(results.next()) {
			AccountTransfer theDetails = mapToRowTransferHistory(results);
			transferDetails.add(theDetails);
		}
		return transferDetails;
	}
	
	private AccountTransfer mapToRowTransferHistory(SqlRowSet rowSet) {
		AccountTransfer theTransferHistory = new AccountTransfer();
		theTransferHistory.setTransferId(rowSet.getInt("transfer_id"));
		theTransferHistory.setTransferTypeId(rowSet.getInt("transfer_type_id"));
		theTransferHistory.setTransferStatusId(rowSet.getInt("transfer_status_id"));
		theTransferHistory.setAccountFrom(rowSet.getInt("account_from"));
		theTransferHistory.setAccountTo(rowSet.getInt("account_to"));
		theTransferHistory.setAmount(rowSet.getBigDecimal("amount"));
		return theTransferHistory;
	}
	
	private void mapToRowTransfer(SqlRowSet rowSet) {
		TransferDTO transfer = new TransferDTO();
		transfer.setAmount(rowSet.getBigDecimal("amount"));
		transfer.setAccountTo(rowSet.getInt("accountTo"));
	}
	
	private void mapToRowAccounts(SqlRowSet rowSet) {
		Accounts user = new Accounts();
		user.setBalance(rowSet.getBigDecimal("amount"));
		user.setUserId(rowSet.getInt("userId"));
	}
	
	private int getNextTransferId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_transfer_id')");
		if(nextIdResult.next()) {
			return nextIdResult.getInt(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new transfer");
		}
	}
}
