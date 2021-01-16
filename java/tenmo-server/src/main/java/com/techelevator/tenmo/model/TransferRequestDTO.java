package com.techelevator.tenmo.model;

import com.techelevator.tenmo.model.TransferType;

public class TransferRequestDTO {
	
	private Integer from;
	private Integer to;
	private int amount;
	private String type;
	
	public TransferRequestDTO() {
	}
	
	public TransferRequestDTO(Integer fromUserId, Integer toUserId, int amount, String transferType) {
		validateTransferType(transferType);
		this.from = fromUserId;
		this.to = toUserId;
		this.amount = amount;
		this.type = transferType;
	}

	public Integer getUserFrom() {
		return from;
	}
	
	public void setUserFrom(Integer userFrom) {
		this.from = userFrom;
	}

	public Integer getUserTo() {
		return to;
	}

	public void setUserTo(Integer userTo) {
		this.to = userTo;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getTransferType() {
		return type;
	}
	
	public void setTransferType(String transferType) {
		validateTransferType(transferType);
		this.type = transferType;
	}

	private void validateTransferType(String transferType) {
		if(!TransferType.isValid(transferType)) {
			throw new IllegalArgumentException(transferType+" is not a valid transferType");
		}
	}

}
