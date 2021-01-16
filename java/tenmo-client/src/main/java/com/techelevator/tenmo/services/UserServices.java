package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.InsufficientBalanceException;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class UserServices {
	public static String AUTH_TOKEN = "";
	private final String BASE_URL;
	public RestTemplate restTemplate = new RestTemplate();

	public UserServices(String url) {
		BASE_URL = url;
		restTemplate = new RestTemplate();
	}

	public User[] listAllAccounts(String token) {
		ResponseEntity<User[]> response = restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, makeAuthEntity(token),
				User[].class);
		return response.getBody();
	}

	public User getuserByUsername(String token, String username) {
		ResponseEntity<User> response = restTemplate.exchange(BASE_URL + "users/" + username, HttpMethod.GET,
				makeAuthEntity(token), User.class);
		return response.getBody();
	}

	public Account getAccount(String token, String username) {
		ResponseEntity<Account> response = restTemplate.exchange(BASE_URL + "account/" + username, HttpMethod.GET,
				makeAuthEntity(token), Account.class);
		return response.getBody();
	}

	public Transfer[] listTransferHistory(String token, String username) {
		ResponseEntity<Transfer[]> response = restTemplate.exchange(BASE_URL + "users/" + username + "/transfers",
				HttpMethod.GET, makeAuthEntity(token), Transfer[].class);
		return response.getBody();
	}

	public Transfer[] viewPendingRequests(String token, String username) {
		ResponseEntity<Transfer[]> response = restTemplate.exchange(BASE_URL + "users/" + username + "/transfers/requests", 
				HttpMethod.GET, makeAuthEntity(token), Transfer[].class);
		return response.getBody();
	}
	
	public Transfer getTransferById(String token, int id) {
		ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "transfers/" + id, 
				HttpMethod.GET, makeAuthEntity(token), Transfer.class);
		return response.getBody();
	}
	
	public Transfer sendBucks(String token, Transfer transfer) {
		ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "account/send", 
				HttpMethod.POST, makeTransferEntity(token, transfer), Transfer.class);
		return response.getBody();
	}
	
	public Transfer requestBucks(String token, Transfer transfer) {
		ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "account/request", 
				HttpMethod.POST, makeTransferEntity(token, transfer), Transfer.class);
		return response.getBody();
	}
	
	public Transfer rejectBucks(String token, Transfer transfer) {
		ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "account/reject",
				HttpMethod.POST, makeTransferEntity(token, transfer), Transfer.class);
		return response.getBody();
	}
	
	
	private HttpEntity<Transfer> makeTransferEntity(String AUTH_TOKEN, Transfer transfer) {
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.setBearerAuth(AUTH_TOKEN);
		    HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
		    return entity;
		}

	private HttpEntity makeAuthEntity(String AUTH_TOKEN) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity(headers);
		return entity;
	}

}
