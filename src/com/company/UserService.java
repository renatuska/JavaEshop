package com.company;

import static com.company.Role.ADMIN;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class UserService {

	Storage db;

	public UserService(String path) throws FileNotFoundException {
		db = new FileStorage();
		db.open(path);
	}

	public User getUser(String username, String password) throws FileNotFoundException, UserException {
		for (User user : getAllUsers()) {

			if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
				return user;
			}
		}
		throw new UserException("Neteisingi prisijungimo duomenys");
	}

	public ArrayList<User> getAllUsers() throws FileNotFoundException {
		ArrayList<User> users = new ArrayList<>();

		while (db.hasData()) {
			String username = db.readAsString();
			String password = db.readAsString();
			String role = db.readAsString();
			String name = db.readAsString();
			String surname = db.readAsString();
			String address = db.readAsString();
			int age = db.readAsInteger();
			db.readAsString();
			db.readAsString();

			users.add(new User(username, password, Role.valueOf(role), name, surname, address, age));
		}
		return users;
	}

	public boolean isUserExists(String username) throws FileNotFoundException {
		ArrayList<User> users = getAllUsers();

		for (User user : users) {
			if (user.getUsername().equalsIgnoreCase(username)) {
				return true;
			}
		}
		return false;
	}

	public void addUser(User user) throws IOException {
		writeUser(user);
	}

	public void deleteUserByUsername(String username) throws IOException, UserException {
		ArrayList<User> allUsers = getAllUsers();
		User userToDelete = getUserByUsername(username, allUsers);
		allUsers.remove(userToDelete);
		rewriteAllUsers(allUsers);
	}

	public void deleteAdmin(User adminUser) throws IOException, AdminDeletionException, UserException {
		validateAdminDeletion();
		deleteUserByUsername(adminUser.getUsername());
	}

	private void validateAdminDeletion() throws FileNotFoundException, AdminDeletionException {
		int adminCount = 0;
		for (User user : getAllUsers()) {

			if (user.getRole().equals(ADMIN)) {
				adminCount++;
			}
		}
		if (adminCount <= 1) {
			throw new AdminDeletionException("ADMIN vartotojo istrinti negalima nes tai yra vienintelis admin vartotojas sistemoje");
		}
	}


	private void rewriteAllUsers(ArrayList<User> users) throws IOException {
		FileWriter fw = new FileWriter(path);
		PrintWriter writer = new PrintWriter(fw);

		for (User user : users) {
			writeUser(user);
		}

		writer.close();
	}

	private void writeUser(User user) {
		db.setAsString(user.getUsername());
		db.setAsString(user.getPassword());
		db.setAsString(user.getRole().toString());
		db.setAsString(user.getName());
		db.setAsString(user.getSurname());
		db.setAsString(user.getAddress());
		db.setAsInteger(user.getAge());
		db.setAsString("");
	}

	private User getUserByUsername(String username, ArrayList<User> allUsers) throws UserException {
		Iterator<User> iter = allUsers.iterator();

		while (iter.hasNext()) {

			User userToDelete = iter.next();
			if (userToDelete.getUsername().equalsIgnoreCase(username)) {
				return userToDelete;
			}
		}
		throw new UserException(String.format("Vartotojas su prisijungimo vardu: \'%s\' neegzistuoja", username));
	}
}
