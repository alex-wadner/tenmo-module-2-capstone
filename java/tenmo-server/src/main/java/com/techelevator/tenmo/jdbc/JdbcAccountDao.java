package com.techelevator.tenmo.jdbc;

import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.InsufficientBalanceException;
import com.techelevator.tenmo.model.Transfer;

@Component
public class JdbcAccountDao implements AccountDAO{
	JdbcTemplate jdbcTemplate;
	public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public Account getAccountByAccountId(int id) {
		Account account = null;
		String sql = "SELECT * FROM accounts where Account_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
		
		if (results.next()) {
			account = mapRowToAccount(results);			
		}
		return account;
	}
	
	@Override
	public Account getAccountByUserId(int id) {
		Account account = null;
		String sql = "SELECT * FROM accounts where user_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
		
		if (results.next()) {
			account = mapRowToAccount(results);			
		}
		return account;
	}

	@Override
	public Transfer sendBucks(Transfer transfer) throws InsufficientBalanceException {
		String sqlFrom = "UPDATE accounts SET balance = (balance - ?) WHERE account_id = ?";
		String sqlTo = "UPDATE accounts SET balance = (balance + ?) WHERE account_id = ?";
		String addTransfer = "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?, ?)";
		String sql = "UPDATE transfers SET transfer_status_id = 2 WHERE transfer_id = ?";
		BigDecimal fromBalance = new BigDecimal("0");
		fromBalance = getAccountByAccountId(transfer.getFrom()).getBalance();
		if (transfer.getAmount().compareTo(fromBalance) == 1) {
			return null;
//			throw new InsufficientBalanceException(); // couldn't get this to work, where we called this in the app instead caught a RestClientException. But it catches the NullPointerException???
		} else {
			if (transfer.getId() != null) {
				transfer.setTypeId(1);
				transfer.setStatusId(2);
				jdbcTemplate.update(sqlFrom, transfer.getAmount(), transfer.getTo());
				jdbcTemplate.update(sqlTo, transfer.getAmount(), transfer.getFrom());
				jdbcTemplate.update(sql, transfer.getId());
				return transfer;
			}
			transfer.setId(getNextId());
			transfer.setTypeId(2);
			transfer.setStatusId(2);
			jdbcTemplate.update(sqlFrom, transfer.getAmount(), transfer.getFrom());
			jdbcTemplate.update(sqlTo, transfer.getAmount(), transfer.getTo());
			jdbcTemplate.update(addTransfer, transfer.getId(), transfer.getTypeId(), transfer.getStatusId(), transfer.getFrom(), transfer.getTo(), transfer.getAmount());
			return transfer;
		}
		
	}
	
	@Override
	public Transfer requestBucks(Transfer transfer) {
		String sql = "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?, ?)";
		transfer.setTypeId(1);
		transfer.setStatusId(1);
		transfer.setId(getNextId());
		jdbcTemplate.update(sql, transfer.getId(), transfer.getTypeId(), transfer.getTypeId(), transfer.getFrom(), transfer.getTo(), transfer.getAmount());
		return transfer;
	}
	
	@Override
	public Transfer rejectBucks(Transfer transfer) {
		String sql = "UPDATE transfers SET transfer_status_id = 3 WHERE transfer_id = ?";
		transfer.setStatusId(3);
		transfer.setTypeId(1);
		jdbcTemplate.update(sql, transfer.getId());
		return transfer;
	}
	

	private Account mapRowToAccount(SqlRowSet results) {
		Account account = new Account();
		account.setAccountId(results.getLong("account_id"));
		account.setUserId(results.getInt("user_id"));
		account.setBalance(results.getBigDecimal("balance"));
		return account;
	}
	
	private Long getNextId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_transfer_id')");
		if (nextIdResult.next()) {
			return nextIdResult.getLong(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new transfer");
		}
	}

}
