package com.techelevator.tenmo;

import java.math.BigDecimal;
import java.util.Scanner;

import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.AccountTransfer;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AccountServiceException;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.tenmo.services.UserServiceException;
import com.techelevator.view.ConsoleService;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
	private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
//    public RestTemplate restTemplate = new RestTemplate();
    private UserService userService = new UserService(API_BASE_URL);
    public Scanner scanner = new Scanner(System.in);
    private AccountService accountService = new AccountService(API_BASE_URL);
    private User user;
    public static String AUTH_TOKEN = "";
    AccountTransfer theTransfer = new AccountTransfer();
    
    public static void main(String[] args) throws UserServiceException, AccountServiceException {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() throws UserServiceException, AccountServiceException {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() throws UserServiceException, AccountServiceException {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		try {
			System.out.println("");
			System.out.println("Your current balance is: " + accountService.viewCurrentBalance());
		} catch(AccountServiceException e) {
			e.printStackTrace();
			}
	}

	private void viewTransferHistory() throws AccountServiceException {
		AccountTransfer theTransferHistory = new AccountTransfer();
		AccountTransfer[] transferReceivedArray = null;
		AccountTransfer[] transferSentArray = null;
		System.out.println("");
		System.out.println("");
		System.out.println("-----------------------------------");
		System.out.println("Transfers");
		System.out.println("ID        FROM/TO            AMOUNT");
		System.out.println("-----------------------------------");
		
		transferReceivedArray = accountService.transferList2();
		transferSentArray = accountService.transferList();
		
		BigDecimal amount = null;
		Integer id = null;
		for(AccountTransfer transfer : transferSentArray) {
			amount = transfer.getAmount();
			id = transfer.getTransferId();
			String nameFrom = transfer.getOtherUser();
			System.out.println(id + "   FROM: " + nameFrom + " $" + amount);
		}
		for(AccountTransfer transfer : transferReceivedArray) {
			theTransferHistory.setAccountTo(currentUser.getUser().getId());
			amount = transfer.getAmount();
			id = transfer.getTransferId();
			String nameTo = transfer.getOtherUser();
			System.out.println(id + "   TO: "  + nameTo + " $" + amount);
		}

			String prompt = "Enter transfer ID to veiw details (0 to cancel)";
			int account = console.getUserInputInteger(prompt);
			accountService.transferDetails(account);
			for(AccountTransfer transfer : transferSentArray) {
				if(transfer.getTransferId() == account) {
			
			System.out.println("-------------------");
			System.out.println("Transfer Details");
			System.out.println("-------------------");
			System.out.println("Id: " + transfer.getTransferId());
			System.out.println("From: " + transfer.getOtherUser());
			System.out.println("To: " + currentUser.getUser().getUsername());
			
		transfer.setTransferTypeId(2);
		if(transfer.getTransferTypeId() == 1) {
			System.out.println("Transfer Type: Request");
		} else if(transfer.getTransferTypeId() == 2) {
				System.out.println("Transfer Type: Send");
			} else {
			System.out.println("NO TRANSFER TYPE!");
		}
		transfer.setTransferStatusId(2);
		if(transfer.getTransferStatusId() == 1) {
			System.out.println("Transfer Status: Pending");
		} else if(transfer.getTransferStatusId() == 2) {
			System.out.println("Transfer Status: Approved");
		} else if(transfer.getTransferStatusId() == 3) {
			System.out.println("Transfer Status: Rejected");
		} else {
			System.out.println("NO TRANSFER STATUS!");
		}
		System.out.println("Amount: $" + transfer.getAmount());
		}
		}
			for(AccountTransfer transfer : transferReceivedArray) {
				if(transfer.getTransferId() == account) {
			
			System.out.println("-------------------");
			System.out.println("Transfer Details");
			System.out.println("-------------------");
			System.out.println("Id: " + transfer.getTransferId());
			System.out.println("From: " + currentUser.getUser().getUsername());
			System.out.println("To: " + transfer.getOtherUser());
			
		transfer.setTransferTypeId(2);
		if(transfer.getTransferTypeId() == 1) {
			System.out.println("Transfer Type: Request");
		} else if(transfer.getTransferTypeId() == 2) {
				System.out.println("Transfer Type: Send");
			} else {
			System.out.println("NO TRANSFER TYPE!");
		}
		transfer.setTransferStatusId(2);
		if(transfer.getTransferStatusId() == 1) {
			System.out.println("Transfer Status: Pending");
		} else if(transfer.getTransferStatusId() == 2) {
			System.out.println("Transfer Status: Approved");
		} else if(transfer.getTransferStatusId() == 3) {
			System.out.println("Transfer Status: Rejected");
		} else {
			System.out.println("NO TRANSFER STATUS!");
		}
		System.out.println("Amount: $" + transfer.getAmount());
		}
		}
	}
	private void viewPendingRequests() {
	
		
	}

	private void sendBucks() throws UserServiceException, AccountServiceException {
		
		AccountTransfer theTransfer = new AccountTransfer();
		User[] userInfo = userService.list();
		System.out.println("-----------------------------------");
		System.out.println("Users");
		System.out.println("ID      " + "Name");
		System.out.println("-----------------------------------");
		for(User user : userInfo) {
		System.out.println(user.getId() + ",      " + user.getUsername());
		}
		System.out.println("-----------------------------------");
		System.out.println("");
		String prompt = "Enter ID of user you are sending to (0 to cancel): ";
		int accountTo = console.getUserInputInteger(prompt);
		
		if(accountTo != currentUser.getUser().getId()) {
			System.out.println("Your current balance is: " + accountService.viewCurrentBalance());
		} else {
			System.out.println("Invalid ID! Please try again with a valid ID");
			sendBucks();
		}
		String transferAmountString = "Enter Amount";
		BigDecimal accountBalance = accountService.viewCurrentBalance();
		int intTransferAmount = console.getUserInputInteger(transferAmountString);
		BigDecimal transferAmount = BigDecimal.valueOf(intTransferAmount);
		if(accountBalance.compareTo(transferAmount) >= 0) {
			theTransfer.setTransferStatusId(2);
			theTransfer.setTransferTypeId(2);
			theTransfer.setAmount(transferAmount);
			theTransfer.setAccountTo(accountTo);
			theTransfer.setAccountFrom(currentUser.getUser().getId());
			accountService.transferMoney(theTransfer, currentUser);
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("-----------------------------------");
			System.out.println("");
			System.out.println("Success! $" + transferAmount + " was sent to the desired account.");
			System.out.println("");
			System.out.println("-----------------------------------");
		} else {
			System.out.println("Your account balance is too low. Please try again with a valid amount.");
		}
		}

	private void requestBucks() {
	
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
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
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}
 
	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
				//important
				accountService.AUTH_TOKEN = currentUser.getToken();
				userService.AUTH_TOKEN = currentUser.getToken();
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
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
