package com.techelevator.tenmo.models;

public class TransferStatus {

	public static final String PENDING = "Pending";
	public static final String APPROVED = "Approved";
	public static final String REJECTED = "Rejected";
		
	public TransferStatus() { } //private constructor prevents this class from being instantiated

	public static boolean isValid(String transferStatus) {
		return PENDING.equals(transferStatus) || APPROVED.equals(transferStatus) || REJECTED.equals(transferStatus);
	}
	
	public String getStatus(int id) {
		String[] statuses = {PENDING, APPROVED, REJECTED};
		return statuses[id - 1];
	}
	
}
