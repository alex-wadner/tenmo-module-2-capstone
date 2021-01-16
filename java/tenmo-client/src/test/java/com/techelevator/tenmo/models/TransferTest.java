package com.techelevator.tenmo.models;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;


public class TransferTest {

	@Test
	public void transfer_return_true() {
		long id = 1;
		int typeId = 2;
		int statusId = 1;
		int from = 10;
		int to = 4;
		String toName = "Fred";
		String fromName = "Bob";
		BigDecimal amount = new BigDecimal("100.0");
		
		Transfer transfer = new Transfer(id, typeId, statusId, from, to, amount);
		transfer.setFromName(fromName);
		transfer.setToName(toName);
		String expected = "\n--------------------------------------------" +
	            "\n Transfer Details" +
	            "\n--------------------------------------------" +
	            "\n ID: " + id +
	            "\n From: " + fromName + 
	            "\n To: " + toName + 
	            "\n Type: " + "Send" +
	            "\n Status: " + "Pending" +
	            "\n Amount: $" + amount +
	            "\n--------------------------------------------";
		String actual = transfer.toString();
		assertEquals(expected, actual);
	}
	
	@Test
	public void transfer_list_sum_displays_correct() {
		long id = 1;
		int typeId = 2;
		int statusId = 1;
		int from = 10;
		int to = 4;
		String toName = "Fred";
		String fromName = "Bob";
		BigDecimal amount = new BigDecimal("100.0");
		
		Transfer transfer = new Transfer(id, typeId, statusId, from, to, amount);
		transfer.setFromName(fromName);
		transfer.setToName(toName);
		String limited = fromName + " / " + toName + "          ";
		String expected = "---------------------------------------------\n" +
				String.format("%1$-13s %2$s %3$10s", "ID: " + id, limited.substring(0, 20), "$"  +  amount.toString())+ "\n";
		String actual = transfer.toStringSum();
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
	
		assertEquals(id,transfer.getId().longValue());
		assertEquals(typeId, transfer.getTypeId());
		assertEquals(statusId, transfer.getStatusId());
		assertEquals(from, transfer.getFrom());
		assertEquals(to, transfer.getTo());
		assertEquals(amount, transfer.getAmount());
	}

}
