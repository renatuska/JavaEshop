package com.company;

import java.util.ArrayList;

import static com.company.Role.CUSTOMER;

public class User extends Entity  {

	private String username;
	private String password;
	private Role role;
	private String name;
	private String surname;
	private String address;
	private int age;
	private ArrayList<Stock> basket;


	public User(String username, String password, Role role, String name, String surname, String address, int age) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.age = age;
		this.address = address;
		this.role = role;
		this.basket = new ArrayList<Stock>();
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Role getRole() {
		return role;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getAddress() {
		return address;
	}

	public int getAge() {
		return age;
	}

	@Override
	public String print() {
		return "User{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", role='" + role + '\'' +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				", address='" + address + '\'' +
				", age=" + age +
				'}';
	}

	public void addToBasket(Stock stock){
		this.basket.add(stock);
	}
	public void removeFromBasket(Stock stock){
		this.basket.remove(stock);
	}
}
