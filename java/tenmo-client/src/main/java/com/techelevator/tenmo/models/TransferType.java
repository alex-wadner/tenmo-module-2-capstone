package com.techelevator.tenmo.models;

public class TransferType {

	public static final String REQUEST = "Request";
	public static final String SEND = "Send";	
		
	public TransferType() { } //private constructor prevents this class from being instantiated

	public static boolean isValid(String transferType) {
		return REQUEST.equals(transferType) || SEND.equals(transferType);
	}
	
	public String getType(int id) {
		String[] types = {REQUEST, SEND};
		return types[id - 1];
	}
	
}
