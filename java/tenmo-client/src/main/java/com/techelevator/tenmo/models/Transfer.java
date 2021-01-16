package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {
	private Long id;
	private int typeId;
	private int statusId;
	private int from;
	private int to;
	private BigDecimal amount;
	
	private String toName;
	private String fromName;
	private TransferStatus transferStatus = new TransferStatus();
	private TransferType transferType = new TransferType();
	
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
	
	public void setToName(String toName) {
		this.toName = toName;
	}
	
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	
	public String toStringSum() {
		String limited = fromName + " / " + toName + "          ";
		return  "---------------------------------------------\n" +
				String.format("%1$-13s %2$s %3$10s", "ID: " + id, limited.substring(0, 20), "$"  +  amount.toString())+ "\n";
	}
	
	public String toString() {
		return "\n--------------------------------------------" +
	            "\n Transfer Details" +
	            "\n--------------------------------------------" +
	            "\n ID: " + id +
	            "\n From: " + fromName + 
	            "\n To: " + toName + 
	            "\n Type: " + transferType.getType(typeId) +
	            "\n Status: " + transferStatus.getStatus(statusId) +
	            "\n Amount: $" + amount +
	            "\n--------------------------------------------";
	}
	

	
}
