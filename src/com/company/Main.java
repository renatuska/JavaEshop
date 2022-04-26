package com.company;

import static com.company.Role.ADMIN;
import static com.company.Role.CUSTOMER;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

	private static final String NEATPAZINTA_IVESTIS = "Neatpažinta įvestis";

	private static final Scanner SC = new Scanner(System.in);

	static UserService userService = new UserService("src/com/company/userdata");

	static StockService stockService;

	static {
		try {
			stockService = new StockService("src/com/company/stockdata");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	static User loggedUser = null;
	static boolean isRunning = true;

	public static void main(String[] args) throws IOException {
		{
//		Scanner sc = new Scanner(System.in);

		while (isRunning) {
			if (loggedUser == null) {
				printLoginMenu();

			} else {
				if (loggedUser.getRole().equals("ADMIN")) {
					printAdminUserMenu();
				} else if (loggedUser.getRole().equals("CUSTUMER")) {
					printCustumerMenu();
				} else {
					System.out.println("Vartotojas neegzistuoja");
				}
			}
		}
	}

		try {
			while (true) {
				printLoginMenu();
				String loginChoice = SC.nextLine();

				switch (loginChoice) {
					case "1": //Prisijungti
						System.out.print("Įveskite prisijungimo vardą: ");
						String username = SC.nextLine();
						System.out.print("Įveskite slaptažodį: ");
						String password = SC.nextLine();

						try {
							User loggedInUser = userService.getUser(username, password);
							userMenu(loggedInUser, userService);
						} catch (UserException e) {
							System.out.println(e.getMessage());
						}
						break;
					case "2": //Registruotis
						registerNewUser(userService);
						break;
					case "3": //Prekių peržiūra
						printAllStocks(stockService.getAllStocks());
						break;
					case "4": //Atsijungti
						return;
					default:
						System.out.println(NEATPAZINTA_IVESTIS);
						break;
				}
			}
		} catch (IOException e) {
			System.out.println("ERROR nerastas duomenu failas");
		}

	}


	private static void userMenu(User user, UserService userService) throws IOException {
		if (user.getRole().equals(CUSTOMER)) {

			custumerUserMenu(user);
		} else if (user.getRole().equals(ADMIN)) {

			adminUserMenu(user);
		}
	}

	private static void adminUserMenu(User adminUser) throws IOException {
		while (true) {
			printAdminUserMenu();
			String choice = SC.nextLine();

			switch (choice) {
				case "1": //Peržiurėti savo informaciją (mato savo rolę, slaptažodžio - ne)
					printUserForAdmin(adminUser);
					break;
				case "2": //Peržiūrėti visų vartotojų informaciją (mato visų vartotojų roles, slaptažodžių - ne)
					ArrayList<User> allUsers = userService.getAllUsers();
					printAllUsers(allUsers);
					break;
				case "3": //Pridėti naują vartotoją
					addNewUser(userService);
					break;
				case "4": //Ištrinti egzistuojantį vartotoją
					System.out.println("--------Vartotojo trynimas-------------");
					System.out.print("Iveskite trinamo vartotojo prisijungimo varda: ");
					String usernameToDelete = SC.nextLine();

					try {
						deleteUser(adminUser, userService, usernameToDelete);
						System.out.printf("Vartotojas \'%s\' sekmingai istrintas\n", usernameToDelete);
						return;
					} catch (AdminDeletionException | UserException e) {
						System.out.println(e.getMessage());
					}

					break;
				case "5": //Patvirtinti prekių užsakymą

					break;
				case "6": //Papildyti prekių likučius
					addNewStock(stockService);
					break;
				case "7": //Peržiūrėti prekių judėjimą (likutis prad=ioje + gavimas - pardavimas = likutis pabaigoje)

					break;
				case "8": //Pardavimų peržiūra

					break;
				case "9": //Pajamų, sąnaudų ir pelno peržiūra

					break;
				case "10": //Pelningumo ataskaita pagal pozicijas

					break;
				case "11":
					return;
				default:
					System.out.println(NEATPAZINTA_IVESTIS);
					break;
			}
		}
	}

	private static void deleteUser(User adminUser, UserService userService, String usernameToDelete) throws IOException, AdminDeletionException, UserException {
		if (!adminUser.getUsername().equalsIgnoreCase(usernameToDelete)) {
			userService.deleteUserByUsername(usernameToDelete);
			return;
		}

		System.out.println("Jus bandote istrinti save, ar tikrai to norite [T/N]");
		String deleteChoice = SC.nextLine();

		if (!(deleteChoice.equalsIgnoreCase("T") || deleteChoice.equalsIgnoreCase("taip"))) {
			System.out.println("Vartotojas netrinamas");
			return;
		}
		userService.deleteAdmin(adminUser);
	}

	private static void addNewUser(UserService userService) throws IOException {
		System.out.println("------------Naujo vartotojo kūrimas------------");
		String username = getValidUsername(userService);
		System.out.print("Įveskite slpatažodį: ");
		String password = SC.nextLine();
		System.out.print("Įveskite vardą: ");
		String name = SC.nextLine();
		System.out.print("Įveskite pavardę: ");
		String surname = SC.nextLine();
		System.out.print("Įveskite pristatymo adresą: ");
		String address = SC.nextLine();
		int age = getAge();
		SC.nextLine();
		Role role = getRoleForNewUser();
		userService.addUser(new User(username, password, role, name, surname, address, age));
		System.out.println("--------------Vartotojas sėkmingai užregistruotas------------------");
	}

	private static void registerNewUser(UserService userService) throws IOException {
		System.out.println("------------Naujo vartotojo registracija------------");
		String username = getValidUsername(userService);
		System.out.print("Įveskite slpatažodį: ");
		String password = SC.nextLine();
		System.out.print("Įveskite vardą: ");
		String name = SC.nextLine();
		System.out.print("Įveskite pavardę: ");
		String surname = SC.nextLine();
		System.out.print("Įveskite pristatymo adresą: ");
		String address = SC.nextLine();
		int age = getAge();
		User user = new User(username, password, name, surname, address, age);
		userService.addUser(user);
		System.out.println("Vartotojas sėkmingai užregistruotas");
		System.out.println();
		custumerUserMenu(user);
	}

	private static int getAge() {
		while (true) {
			try {
				System.out.print("Įveskite amzių: ");
				int age = SC.nextInt();
				if (!(age <= 99 && age >= 1)) {
					throw new InputMismatchException();
				}
				SC.nextLine();
				return age;
			} catch (InputMismatchException e) {
				System.out.println("Amžius turi būti skaičius 1 - 99");
				SC.nextLine();
			}

		}
	}

	private static String getValidUsername(UserService userService) throws FileNotFoundException {
		String username = "";
		while (true) {
			System.out.println("************************************************************************************************************");
			System.out.print("Įveskite prisijungimo vardą: ");
			username = SC.nextLine();
			if (!userService.isUserExists(username)) {
				return username;
			}
			System.out.printf("Vartotojo vardas \'%s\' užimtas\n", username);
		}
	}

	private static Role getRoleForNewUser() {
		System.out.println("Pasirinkite rolę:");

		while (true) {
			System.out.println("[1] Custumer");
			System.out.println("[2] Admin");
			String choice = SC.nextLine();

			switch (choice) {
				case "1":
					return CUSTOMER;
				case "2":
					return ADMIN;
				default:
					System.out.println(NEATPAZINTA_IVESTIS);
			}
		}
	}

	private static void printAllUsers(ArrayList<User> users) {
		for (User user : users) {
			printUserForAdmin(user);
		}
	}

	private static void printUserForAdmin(User adminUser) {
		System.out.println("************************************************************************************************************");
		System.out.println("Prisijungimo vardas: " + adminUser.getUsername());
		System.out.println("Vardas: " + adminUser.getName());
		System.out.println("Pavardė: " + adminUser.getSurname());
		System.out.println("Adresas: " + adminUser.getAddress());
		System.out.println("Amžius: " + adminUser.getAge());
		System.out.println("Rolė: " + adminUser.getRole());
		System.out.println();
	}


	private static void custumerUserMenu(User custumerUser) throws FileNotFoundException {
		while (true) {
			printCustumerMenu();
			String choice = SC.nextLine();

			switch (choice) {
				case "1": //Peržiurėti savo informaciją (savo rolės ir slaptažodžio nemato
					printCustumer(custumerUser);
					break;
				case "2": //Prekių peržiūra
					printAllStocks(stockService.getAllStocks());
					break;
				case "3": //Pirkimų užsakymų sukūrimas
					createBasketForCustumer();
					break;
				case "4": //Pirkimų istorija
					break;
				case "5":
					return;
				default:
					System.out.println(NEATPAZINTA_IVESTIS);
					break;
			}
		}
	}

	private static void createBasketForCustumer() {
		System.out.println("************************************************************************************************************");
		System.out.println("Įveskite perkamos prekės ID: ");
		String itemID = SC.nextLine();
		System.out.print("Įveskite perkamos prekės kiekį: ");
		int itemQt = Integer.parseInt(SC.nextLine());

		Stock stock = stockService.getStockbyId(itemID);
		if(stock != null) {
			if(stock.getItemQt() > itemQt) {
				use
			} else {
				//turiu tik tike o turi n
			}
		} else {
			//User nera tokios
		}
	}

	public static void printAllStocks(ArrayList<Stock> stocks) {
		for (Stock stock : stocks) {
			printStock(stock);
		}
	}

	public static void printStock(Stock stock) {
		System.out.println("************************************************************************************************************");
		System.out.println("Prekės pavadinimas: " + stock.getItemName());
		System.out.println("Prekės kaina: " + stock.getItemPrice());
		System.out.println("Prekių kiekis sandėlyje: " + stock.getItemQt());
		System.out.println();
	}








	private static void addNewStock(StockService stockService) throws IOException {
		System.out.println("************************************************************************************************************");
		System.out.print("Įveskite prekės ID: ");
		String itemId = SC.nextLine();
		System.out.print("Įveskite prekės pavadinimą: ");
		String itemName = SC.nextLine();
		System.out.print("Įveskite prekės savikainą: ");
		double itemCosts = SC.nextDouble();
		System.out.print("Įveskite prekės kiekį: ");
		int itemQt = SC.nextInt();
		SC.nextLine();
		stockService.addStock(new Stock(itemId, itemName, itemCosts, itemQt));
		System.out.println("----------Prekės atsargos sėkmingai patalpintos į prekybos sandėlį----------");
	}



	private static void printCustumer(User user) {
		System.out.println("************************************************************************************************************");
		System.out.println("Prisijungimo vardas: " + user.getUsername());
		System.out.println("Vardas: " + user.getName());
		System.out.println("Pavardė: " + user.getSurname());
		System.out.println("Adresas: " + user.getAddress());
		System.out.println("Amžius: " + user.getAge());
		System.out.println("--------------------------------------");
	}

	//-------------------------------------------------------------------------------MENIU------------------------------------------------------------------------------
	private static void printAdminUserMenu() {
		System.out.println("************************************************************************************************************");
		System.out.println("[1] Peržiurėti savo informaciją");
		System.out.println("[2] Peržiūrėti visų vartotojų informaciją");
		System.out.println("[3] Pridėti naują vartotoją");
		System.out.println("[4] Ištrinti egzistuojantį vartotoją");
		System.out.println("[5] Patvirtinti prekių užsakymą");
		System.out.println("[6] Papildyti prekių likučius");
		System.out.println("[7] Peržiūrėti prekių judėjimą");
		System.out.println("[8] Pardavimų peržiūra");
		System.out.println("[9] Pajamų, sąnaudų ir pelno peržiūra");
		System.out.println("[10] Pelningumo ataskaita pagal pozicijas");
		System.out.println("[11] Atsijungti");
	}

	private static void printCustumerMenu() {
		System.out.println("************************************************************************************************************");
		System.out.println("[1] Peržiūrėti savo informaciją");
		System.out.println("[2] Prekių peržiūra");
		System.out.println("[3] Pirkimų užsakymų sukūrimas");
		System.out.println("[4] Pirkimų istorija");
		System.out.println("[5] Atsijungti");
	}

	private static void printLoginMenu() {
		System.out.println("[1] Prisijungti");
		System.out.println("[2] Registruotis");
		System.out.println("[3] Prekių peržiūra");
		System.out.println("[4] Atsijungti");
	}
}
