package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountsDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.AccountTransfer;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.User;

@RestController
@PreAuthorize("isAuthenticated()")

public class AccountController {
	
	private AccountsDAO dao;
	private UserDAO userDao;
	
	public AccountController(AccountsDAO accountsDao, UserDAO userDao) {
		this.dao = accountsDao;
		this.userDao = userDao;
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@RequestMapping(path = "accounts/transfer", method = RequestMethod.PUT)
	public void transferMoney(@Valid @RequestBody AccountTransfer transfer) {
		dao.transferMoneyTotal(transfer);
	}
	
	@RequestMapping(path = "balance", method = RequestMethod.GET)
	public BigDecimal getBalance(Principal username) {
		String usernameString = username.getName();
		int userId = userDao.findIdByUsername(usernameString);
		return dao.getBalance(userId);

	}
	
	@RequestMapping(path = "accounts/transfer/history/sent", method = RequestMethod.GET)
	public List<AccountTransfer> transferList(Principal principal) {
		
		return dao.getTransferHistory(principal);
	}

	@RequestMapping(path = "accounts/transfer/history/received", method = RequestMethod.GET)
	public List<AccountTransfer> transferList2(Principal principal) {
		
		 String usernameIdString = principal.getName();
			int userId = userDao.findIdByUsername(usernameIdString);
		return dao.getTransferHistoryReceived(principal);
	}
	
	@RequestMapping(path = "accounts/transfer/history/details/{transferId}", method = RequestMethod.GET)
	public List<AccountTransfer> detailsList( @PathVariable int transferId, Principal principal) {
		
		User user = userDao.findByUsername(principal.getName());
		
		return dao.getTransferDetails(user.getId(), transferId);
	}
	
	
	
}
