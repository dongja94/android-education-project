package com.example.samples2database;

public class Person {
	long _id = -1;
	String name;
	int age;
	String address;
	
	@Override
	public String toString() {
		return name+"("+age+")";
	}
}
