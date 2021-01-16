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
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.dao.UserSqlDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.InsufficientBalanceException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

class JdbcAccountDaoTest {
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
		/*
		 * The following line disables autocommit for connections returned by this
		 * DataSource. This allows us to rollback any changes after each test
		 */
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
	public void getAccountTest_returns_UserId() {
		User user = new User();
		userDao.create("test", "test"); 
		int userId = userDao.findIdByUsername("test");

		Account acct = dao.getAccountByUserId(userId);
		assertEquals(userId, acct.getUserId());
		
	}
	
	@Test
	public void sendBuckTest_sends_money_return_transfer() throws InsufficientBalanceException {
		userDao.create("sender", "sender"); 
		int userIdSender = userDao.findIdByUsername("sender");
		userDao.create("reciever", "reciever"); 
		int userIdReciever = userDao.findIdByUsername("reciever");
		Account acctSender = dao.getAccountByUserId(userIdSender);
		Account acctReciever = dao.getAccountByUserId(userIdReciever);
		BigDecimal amount = new BigDecimal("250");
		Transfer transfer = new Transfer();
		transfer.setFrom(acctSender.getAccountId().intValue());
		transfer.setTo(acctReciever.getAccountId().intValue());
		transfer.setAmount(amount);
		transfer = dao.sendBucks(transfer);
		
		Account acctSenderAfter = dao.getAccountByUserId(userIdSender);
		Account acctRecieverAfter = dao.getAccountByUserId(userIdReciever);
		BigDecimal afterSender = acctSenderAfter.getBalance();
		BigDecimal afterReciever = acctRecieverAfter.getBalance();
		
		assertEquals(new BigDecimal("750.00"), afterSender);
		assertEquals(new BigDecimal("1250.00"), afterReciever);
	}
	
	
	@Test
	public void requestBucksTest() {
		userDao.create("sender", "sender"); 
		int userIdSender = userDao.findIdByUsername("sender");
		userDao.create("reciever", "reciever"); 
		int userIdReciever = userDao.findIdByUsername("reciever");
		Account acctSender = dao.getAccountByUserId(userIdSender);
		Account acctReciever = dao.getAccountByUserId(userIdReciever);
		BigDecimal amount = new BigDecimal("250");
		Transfer transfer = new Transfer();
		transfer.setFrom(acctSender.getAccountId().intValue());
		transfer.setTo(acctReciever.getAccountId().intValue());
		transfer.setAmount(amount);
		transfer = dao.requestBucks(transfer);
		int statusPending = 1;
		assertEquals(transfer.getStatusId(), statusPending);
	}
	
	@Test
	public void approveRequestTest() throws InsufficientBalanceException {
		userDao.create("sender", "sender"); 
		int userIdSender = userDao.findIdByUsername("sender");
		userDao.create("reciever", "reciever"); 
		int userIdReciever = userDao.findIdByUsername("reciever");
		Account acctSender = dao.getAccountByUserId(userIdSender);
		Account acctReciever = dao.getAccountByUserId(userIdReciever);
		BigDecimal amount = new BigDecimal("250");
		Transfer transfer = new Transfer();
		transfer.setFrom(acctSender.getAccountId().intValue());
		transfer.setTo(acctReciever.getAccountId().intValue());
		transfer.setAmount(amount);
		transfer = dao.requestBucks(transfer);
		transfer = dao.sendBucks(transfer);
		
		assertEquals(2, transfer.getStatusId());
		
	}
	
	@Test
	public void rejectBucksTest() {
		userDao.create("sender", "sender"); 
		int userIdSender = userDao.findIdByUsername("sender");
		userDao.create("reciever", "reciever"); 
		int userIdReciever = userDao.findIdByUsername("reciever");
		Account acctSender = dao.getAccountByUserId(userIdSender);
		Account acctReciever = dao.getAccountByUserId(userIdReciever);
		BigDecimal amount = new BigDecimal("250");
		Transfer transfer = new Transfer();
		transfer.setFrom(acctSender.getAccountId().intValue());
		transfer.setTo(acctReciever.getAccountId().intValue());
		transfer.setAmount(amount);
		transfer = dao.rejectBucks(transfer);
		int statusReject = 3;
		assertEquals(transfer.getStatusId(), statusReject);
	}
	

}
