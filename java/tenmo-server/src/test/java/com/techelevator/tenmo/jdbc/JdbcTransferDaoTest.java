package com.techelevator.tenmo.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.dao.UserSqlDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.InsufficientBalanceException;
import com.techelevator.tenmo.model.Transfer;

class JdbcTransferDaoTest {
	static SingleConnectionDataSource dataSource;
	private AccountDAO dao;
	private UserDAO userDao;
	private TransferDAO tranDao;
	private JdbcTemplate jdbcTemplate;

	
	@BeforeAll
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	@AfterAll
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}
	
	@AfterEach
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@BeforeEach
	public void setup() {
		jdbcTemplate = new JdbcTemplate(dataSource); 
		dao = new JdbcAccountDao(jdbcTemplate);
		userDao = new UserSqlDAO(jdbcTemplate);
		tranDao = new JdbcTransferDao(jdbcTemplate);
	}
	
	@Test
	public void test_returns_transfers_to() throws InsufficientBalanceException {
		userDao.create("sender", "sender"); 
		int userIdSender = userDao.findIdByUsername("sender");
		Account acctSender = dao.getAccountByUserId(userIdSender);
		
		userDao.create("reciever", "reciever"); 
		int userIdReciever = userDao.findIdByUsername("reciever");
		Account acctReciever = dao.getAccountByUserId(userIdReciever);
		
		BigDecimal amount = new BigDecimal("250");
		
		Transfer transfer = new Transfer();
		transfer.setFrom(acctSender.getAccountId().intValue());
		transfer.setTo(acctReciever.getAccountId().intValue());
		transfer.setAmount(amount);

		Transfer transfer2 = new Transfer();
		transfer2.setFrom(acctSender.getAccountId().intValue());
		transfer2.setTo(acctReciever.getAccountId().intValue());
		transfer2.setAmount(amount);
		
		transfer = dao.sendBucks(transfer);
		transfer2 = dao.sendBucks(transfer2);
		
		List<Transfer>tranTo = tranDao.listAllTransfersTo(userIdReciever);
		assertEquals(tranTo.size(), 2);
	}
	
	@Test 
	public void test_returns_transfers_from() throws InsufficientBalanceException {
		userDao.create("sender", "sender"); 
		int userIdSender = userDao.findIdByUsername("sender");
		Account acctSender = dao.getAccountByUserId(userIdSender);
		
		userDao.create("reciever", "reciever"); 
		int userIdReciever = userDao.findIdByUsername("reciever");
		Account acctReciever = dao.getAccountByUserId(userIdReciever);
		
		BigDecimal amount = new BigDecimal("250");
		
		Transfer transfer = new Transfer();
		transfer.setFrom(acctSender.getAccountId().intValue());
		transfer.setTo(acctReciever.getAccountId().intValue());
		transfer.setAmount(amount);

		Transfer transfer2 = new Transfer();
		transfer2.setFrom(acctSender.getAccountId().intValue());
		transfer2.setTo(acctReciever.getAccountId().intValue());
		transfer2.setAmount(amount);
		
		transfer = dao.sendBucks(transfer);
		transfer2 = dao.sendBucks(transfer2);
		
		List<Transfer> tranFrom = tranDao.listAllTransfersFrom(userIdSender);
		assertEquals(tranFrom.size(), 2);
	}
	
	@Test void test_returns_transfer_by_id() throws InsufficientBalanceException {
		userDao.create("sender", "sender"); 
		int userIdSender = userDao.findIdByUsername("sender");
		Account acctSender = dao.getAccountByUserId(userIdSender);
		
		userDao.create("reciever", "reciever"); 
		int userIdReciever = userDao.findIdByUsername("reciever");
		Account acctReciever = dao.getAccountByUserId(userIdReciever);
		
		BigDecimal amount = new BigDecimal("250");
		
		Transfer transfer = new Transfer();
		transfer.setFrom(acctSender.getAccountId().intValue());
		transfer.setTo(acctReciever.getAccountId().intValue());
		transfer.setAmount(amount);

		Transfer transfer2 = new Transfer();
		transfer2.setFrom(acctSender.getAccountId().intValue());
		transfer2.setTo(acctReciever.getAccountId().intValue());
		transfer2.setAmount(amount);
		
		transfer = dao.sendBucks(transfer);
		transfer2 = dao.sendBucks(transfer2);
		
		long id = transfer.getId();
		assertEquals(id, tranDao.listTransferById(transfer.getId().intValue()).getId());
	}
	
	@Test
	public void test_returns_pending_transfers() throws InsufficientBalanceException {
		userDao.create("sender", "sender"); 
		int userIdSender = userDao.findIdByUsername("sender");
		Account acctSender = dao.getAccountByUserId(userIdSender);
		
		userDao.create("reciever", "reciever"); 
		int userIdReciever = userDao.findIdByUsername("reciever");
		Account acctReciever = dao.getAccountByUserId(userIdReciever);
		
		BigDecimal amount = new BigDecimal("250");
		
		Transfer transfer = new Transfer();
		transfer.setFrom(acctSender.getAccountId().intValue());
		transfer.setTo(acctReciever.getAccountId().intValue());
		transfer.setAmount(amount);

		Transfer transfer2 = new Transfer();
		transfer2.setFrom(acctSender.getAccountId().intValue());
		transfer2.setTo(acctReciever.getAccountId().intValue());
		transfer2.setAmount(amount);
		
		transfer = dao.sendBucks(transfer);
		transfer2 = dao.sendBucks(transfer2);
		
		assertEquals(2, tranDao.listTransferByStatus(2, transfer.getTo()).size());
	}


}
