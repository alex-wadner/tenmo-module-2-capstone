package com.techelevator.tenmo.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class TransferTest {


	@Test
	public void transfer_return_true() {
		long id = 1;
		int typeId = 2;
		int statusId = 1;
		int from = 10;
		int to = 4;
		BigDecimal amount = new BigDecimal("100.0");
		
		Transfer transfer = new Transfer(id, typeId, statusId, from, to, amount);
		String expected = "\n--------------------------------------------" +
	            "\n Transfer Details" +
	            "\n--------------------------------------------" +
	            "\n Id: " + id +
	            "\n TypeId: " + typeId +
	            "\n StatusId: " + statusId +
	            "\n From: " + from + 
	            "\n To: " + to+ 
	            "\n Amount $" + amount;
		String actual = transfer.toString();
		assertEquals(expected, actual);
	}
	@Test
	public void transfer_send_setVariables_return_same() {
		long id = 1;
		int typeId = 2;
		int statusId = 1;
		int from = 10;
		int to = 4;
		BigDecimal amount = new BigDecimal("100.0");
		
		Transfer transfer = new Transfer();
		transfer.setAmount(amount);
		transfer.setFrom(from);
		transfer.setId(id);
		transfer.setStatusId(statusId);
		transfer.setTypeId(typeId);
		transfer.setTo(to);
	
		assertEquals(id,transfer.getId());
		assertEquals(typeId, transfer.getTypeId());
		assertEquals(statusId, transfer.getStatusId());
		assertEquals(from, transfer.getFrom());
		assertEquals(to, transfer.getTo());
		assertEquals(amount, transfer.getAmount());
	}

}
