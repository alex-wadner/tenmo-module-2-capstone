package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {

	List<Transfer> listAllTransfersTo(int userId);
	
	Transfer listTransferById(int id);
	
	
	List<Transfer> listTransferByStatus(int status, int to);

	List<Transfer> listAllTransfersFrom(int userId);
	
}
