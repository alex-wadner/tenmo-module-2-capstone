package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
	private Long id;
	private int typeId;
	private int statusId;
	private int from;
	private int to;
	private BigDecimal amount;
	
	public Transfer() {
	}
	
	public Transfer(Long transferID, int transferTypeID, int transferStatusID, int accountFrom, int accountTo, BigDecimal transferAmount) {
		this.id = transferID;
		this.typeId = transferTypeID;
		this.statusId = transferStatusID;
		this.from = accountFrom;
		this.to = accountTo;
		this.amount = transferAmount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String toString() {
		return "\n--------------------------------------------" +
	            "\n Transfer Details" +
	            "\n--------------------------------------------" +
	            "\n Id: " + id +
	            "\n TypeId: " + typeId +
	            "\n StatusId: " + statusId +
	            "\n From: " + from + 
	            "\n To: " + to+ 
	            "\n Amount $" + amount;
	}

	
}
