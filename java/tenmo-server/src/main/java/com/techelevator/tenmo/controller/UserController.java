package com.techelevator.tenmo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.InsufficientBalanceException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

	private UserDAO userDao;
	private AccountDAO accountDao;
	private TransferDAO transferDao;
	
	public UserController(UserDAO userDao, AccountDAO accountDao, TransferDAO transferDao) {
		this.userDao = userDao;
		this.accountDao = accountDao;
		this.transferDao = transferDao;
	}
	
	@RequestMapping(path = "users", method = RequestMethod.GET)
	public List<User> getAllUsers() {
		return userDao.findAll();
	}
	
	@RequestMapping(path = "users/{username}", method = RequestMethod.GET)
	public User getUserByUsername(@PathVariable String username) {
		return userDao.findByUsername(username);
	}
	
//	@RequestMapping(path = "users/search", method = RequestMethod.GET) //postman call is: http://localhost:8080/users/search?username=admin
//	public int getIdByUsername(@RequestParam String username) {
//		return userDao.findIdByUsername(username);
//	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "users", method = RequestMethod.POST)
	public boolean create(String username, String password) {
		return userDao.create(username, password);
	}
	
	@RequestMapping(path = "account/{username}", method = RequestMethod.GET)
	public Account getBalance(@PathVariable String username) {
		int id = userDao.findIdByUsername(username);
		Account account = accountDao.getAccountByUserId(id);
		return account;
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "account/send", method = RequestMethod.POST)
	public Transfer sendBucks(@RequestBody Transfer transfer) throws InsufficientBalanceException {
		return accountDao.sendBucks(transfer);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "account/request", method = RequestMethod.POST)
	public Transfer requestBucks(@RequestBody Transfer transfer) {
		return accountDao.requestBucks(transfer);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "account/reject", method = RequestMethod.POST)
	public Transfer rejectBucks(@RequestBody Transfer transfer) {
		return accountDao.rejectBucks(transfer);
	}
	
	@RequestMapping(path = "users/{username}/transfers/to", method = RequestMethod.GET)
	public List<Transfer> listAllTransfersTo(@PathVariable String username) {
		int id = userDao.findIdByUsername(username);
		return transferDao.listAllTransfersTo(id);
	}
	
	@RequestMapping(path = "users/{username}/transfers/from", method = RequestMethod.GET)
	public List<Transfer> listAllTransfersFrom(@PathVariable String username) {
		int id = userDao.findIdByUsername(username);
		return transferDao.listAllTransfersFrom(id);
	}
	
	@RequestMapping(path = "users/{username}/transfers", method = RequestMethod.GET)
	public List<Transfer> listAllTransfers(@PathVariable String username) {
		int id = userDao.findIdByUsername(username);
		List<Transfer> all = new ArrayList<Transfer>();
		all.addAll(transferDao.listAllTransfersTo(id));
		all.addAll(transferDao.listAllTransfersFrom(id));
		return all;
	}
	@RequestMapping(path = "transfers/{id}", method = RequestMethod.GET)
	public Transfer listTransferById(@PathVariable int id) {
		return transferDao.listTransferById(id);
	}
	
	@RequestMapping(path = "users/{username}/transfers/requests", method = RequestMethod.GET)
	public List<Transfer> listTransferByStatus(@PathVariable String username){
		int type = 1;
		int userId = userDao.findIdByUsername(username);
		return transferDao.listTransferByStatus(type, userId);
	}
	
}
