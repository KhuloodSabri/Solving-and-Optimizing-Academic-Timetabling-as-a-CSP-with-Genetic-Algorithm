package Basics;

import java.util.ArrayList;

public class Course {
	private static int count=0;
	private int id;
	private String name;
	private char type;
	private String symbol;
	private int numOfSections;
	private int year;
	private Room labRoom;	
	private ArrayList <Doctor> doctors = new ArrayList<>();
	
	public Course() {
		super();
		this.id= count++;
		
	}

	
	public Course(String name, String symbol, int numOfSections) {
		super();
		this.id= count++;
		this.name = name;
		this.symbol = symbol;
		this.numOfSections = numOfSections;
		this.getYear();
		calcType();
	}
	
	public int getYear() {
		this.year = this.symbol.charAt(4)-'0';
		return this.year;
	}


	public static int getCount() {
		return count;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public void calcType() {
		int num = this.symbol.charAt(5)-'0';
		if (num==1) {
			this.type = 'l';
		}else {
			this.type = 'c';
		}
		
	}
	
	public char getType () {
		return this.type;
	}


	public String getSymbol() {
		return symbol;
	}


	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	public int getNumOfSections() {
		return numOfSections;
	}


	public void setNumOfSections(int numOfSections) {
		this.numOfSections = numOfSections;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public ArrayList<Doctor> getDoctors() {
		return doctors;
	}


	public void setDoctors(ArrayList<Doctor> doctors) {
		this.doctors = doctors;
	}


	public Room getLabRoom() {
		return labRoom;
	}


	public void setLabRoom(Room labRoom) {
		this.labRoom = labRoom;
	}
	
	

}
