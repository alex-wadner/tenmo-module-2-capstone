package com.techelevator.tenmo.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Transfer;

@Component
public class JdbcTransferDao implements TransferDAO {

	JdbcTemplate jdbcTemplate;

	public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Transfer> listAllTransfersTo(int userId) {
		List<Transfer> allTransfers = new ArrayList<Transfer>();
		
		String sql = "SELECT * FROM transfers JOIN accounts ON accounts.account_id = transfers.account_to WHERE accounts.user_id = ?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
		while (result.next()) {
			Transfer transfer = mapRowToTransfer(result);
			allTransfers.add(transfer);
		}

		return allTransfers;
	}
	
	@Override
	public List<Transfer> listAllTransfersFrom(int userId){
		List<Transfer> allTransfers = new ArrayList<Transfer>();
		String sql = "SELECT * FROM transfers JOIN accounts ON accounts.account_id = transfers.account_from WHERE accounts.user_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
		
		while (results.next()) {
			Transfer transfer = mapRowToTransfer(results);
			allTransfers.add(transfer);
		}
		return allTransfers;
	}

	@Override
	public Transfer listTransferById(int id) {
		Transfer transfer = null;
		String sql = "SELECT * FROM transfers WHERE transfer_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

		if (results.next()) {
			transfer = mapRowToTransfer(results);
		}
		return transfer;
	}


	@Override
	public List<Transfer> listTransferByStatus(int status, int userId) {
		List<Transfer> allTransfers = new ArrayList<Transfer>();
		String sql = "SELECT * FROM transfers WHERE transfer_status_id = ? AND account_to = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, status, userId);

		while (results.next()) {
			Transfer transfer = mapRowToTransfer(results);
			allTransfers.add(transfer);
		}
		return allTransfers;
	}

	private Transfer mapRowToTransfer(SqlRowSet results) {
		Transfer transfer = new Transfer();
		transfer.setId(results.getLong("transfer_id"));
		transfer.setTypeId(results.getInt("transfer_type_id"));
		transfer.setStatusId(results.getInt("transfer_status_id"));
		transfer.setFrom(results.getInt("account_from"));
		transfer.setTo(results.getInt("account_to"));
		transfer.setAmount(results.getBigDecimal("amount"));
		return transfer;
	}

}
