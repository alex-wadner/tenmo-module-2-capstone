package com.techelevator.tenmo;

import java.math.BigDecimal;

import org.springframework.web.client.RestClientException;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.UserServices;
import com.techelevator.view.ConsoleService;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";

	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN,
			MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS,
			MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS,
			MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;
	private UserServices userServices;

	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL),
				new UserServices(API_BASE_URL));
		app.run();
	}

	public App(ConsoleService console, AuthenticationService authenticationService, UserServices userServices) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.userServices = userServices;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");

		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while (true) {
			String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				System.out.println("\nThanks for using TEnmo. Goodbye!");
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		Account account = userServices.getAccount(currentUser.getToken(), currentUser.getUser().getUsername());
		System.out.println("Your current balance is $" + account.getBalance());
	}

	private void viewTransferHistory() {
		System.out.println("\n---------------------------------------------" +
	                       "\nTransfer      Participants             Amount");
		Transfer[] transfer = userServices.listTransferHistory(currentUser.getToken(),
				currentUser.getUser().getUsername());
		for (Transfer transfers : transfer) {
			transfers.setFromName(userById(transfers.getFrom()).getUsername());
			transfers.setToName(userById(transfers.getTo()).getUsername());
			System.out.println(transfers.toStringSum());
		}
		int choice = console.getUserInputInteger("\nSelect the ID of the transfer to view details (0 to cancel)");
		for (Transfer transfers : transfer) {
			if (transfers.getId() == choice) {
				System.out.println(transfers.toString());
			}
		}
	}

	private void viewPendingRequests() {
		Transfer [] transfer = userServices.viewPendingRequests(currentUser.getToken(), currentUser.getUser().getUsername());
		if (transfer.length == 0) {
			System.out.println("You have no pending requests.");
			mainMenu();
		}
		for (Transfer transfers : transfer) {
			transfers.setFromName(userById(transfers.getFrom()).getUsername());
			transfers.setToName(userById(transfers.getTo()).getUsername());
			System.out.println(transfers.toString());
		}
		
		String[] options = {"Pay Request", "Reject Request", "Return to Main Menu"};
		String choice = (String) console.getChoiceFromOptions(options);
		
		if (choice == options[0]) {
			Transfer transfers = fulfillRequest("pay");
			try {
				transfers = userServices.sendBucks(currentUser.getToken(), transfers);
				transfers.setFromName(userById(transfers.getFrom()).getUsername());
				transfers.setToName(userById(transfers.getTo()).getUsername());
				System.out.println(transfers.toString());
			} catch (NullPointerException e) {
				System.out.println("\nInsufficient Funds. Try again when you have the requested amount.");
			}
		}
		if (choice == options[1]) {
			Transfer transfers = fulfillRequest("reject");
			transfers = userServices.rejectBucks(currentUser.getToken(), transfers);
			transfers.setFromName(userById(transfers.getFrom()).getUsername());
			transfers.setToName(userById(transfers.getTo()).getUsername());
			System.out.println(transfers.toString());
		}
	}
	
	private void sendBucks() {
		User[] allUsers = userServices.listAllAccounts(currentUser.getToken());
		System.out.println(userHeader());
		for (User user : allUsers) {
			System.out.println(user.toString());
		}
		
		int userInput = noFraud("Select the ID of the user you'd like to send money to (0 to cancel)");
		for (User user : allUsers) {
			if (userInput == user.getId()) {
				BigDecimal amount = console.getUserInputBigDecimal("Enter the amount of TE Bucks you wish to send this user");
				Account accountFrom = userServices.getAccount(currentUser.getToken(), currentUser.getUser().getUsername());
				Account accountTo = userServices.getAccount(currentUser.getToken(), user.getUsername());
				try {
					Transfer newTransfer = setNewTransfer(amount, accountTo.getAccountId().intValue(), accountFrom.getAccountId().intValue());
					newTransfer = userServices.sendBucks(currentUser.getToken(), newTransfer);
					newTransfer.setFromName(currentUser.getUser().getUsername());
					newTransfer.setToName(user.getUsername());
					System.out.println(newTransfer.toString());
					break;
				} catch (NullPointerException e) {
					System.out.println("\nInsufficient Funds.");
				} catch (RestClientException e) {
					System.out.println("\nYou can't send $0.\n");
				}
			}
		}
	}

	private void requestBucks() {
		User[] allUsers = userServices.listAllAccounts(currentUser.getToken());
		System.out.println(userHeader());
		for (User user : allUsers) {
			System.out.println(user.toString());
		}
		
		int userInput = noFraud("Select the ID of the user you'd like to request money from (0 to cancel)");
		for (User user : allUsers) {
			if (userInput == user.getId()) {
				BigDecimal amount = console.getUserInputBigDecimal("Enter the amount of TE Bucks you wish to request from this user");
				Account accountFrom = userServices.getAccount(currentUser.getToken(), currentUser.getUser().getUsername());
				Account accountTo = userServices.getAccount(currentUser.getToken(), user.getUsername());
				try {
					Transfer newTransfer = setNewTransfer(amount, accountTo.getAccountId().intValue(), accountFrom.getAccountId().intValue());
					newTransfer = userServices.requestBucks(currentUser.getToken(), newTransfer);
					newTransfer.setToName(user.getUsername());
					newTransfer.setFromName(currentUser.getUser().getUsername());
					System.out.println(newTransfer.toString());
					break;
				} catch (RestClientException e) {
					System.out.println("\nYou can't request $0.\n");
				}
			}
		}
	}
	
	private Transfer setNewTransfer(BigDecimal amount, int to, int from) {
		Transfer newTransfer = new Transfer();
		newTransfer.setAmount(amount);
		newTransfer.setFrom(from);
		newTransfer.setTo(to);
		return newTransfer;
	}
	
	private User userById(int id) {
		
	User[] allUsers = userServices.listAllAccounts(currentUser.getToken());
	for(User user: allUsers) {
		if (id == user.getId()) {
			return user;
		}
	}
	return null;
	}
	
	private Transfer fulfillRequest(String action) {
		Transfer [] transfer = userServices.viewPendingRequests(currentUser.getToken(), currentUser.getUser().getUsername());
		Transfer returnTransfer = null;
		int id = console.getUserInputInteger("Select the Transfer ID you wish to " + action + " (0 to cancel)");
		for (Transfer transfers : transfer) {
			if (transfers.getId() == id) {
				BigDecimal amount = userServices.getTransferById(currentUser.getToken(), id).getAmount();
				transfers.setAmount(amount);
				returnTransfer = transfers;
			}
		}
		return returnTransfer;
	}

	private String userHeader() {
		return "--------------------------------------\r\n" +  
			   "ID             Name\r\n" + 
			   "--------------------------------------";
	}
	
	private int noFraud(String prompt) {
		int userInput = 0;
		while (true) {
			userInput = console.getUserInputInteger(prompt);
			if (userInput != currentUser.getUser().getId() ) {
				return userInput;
			}
		System.out.println("\nYou can't request money from yourself.\n");
		}
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while (!isAuthenticated()) {
			String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
		while (!isRegistered) // will keep looping until user is registered
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				System.out.println("Registration successful. You can now login.");
			} catch (AuthenticationServiceException e) {
				System.out.println("REGISTRATION ERROR: " + e.getMessage());
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) // will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: " + e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}

	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
