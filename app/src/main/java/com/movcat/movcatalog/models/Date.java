package com.movcat.movcatalog.models;

public class Date{
	private int month;
	private int year;
	private int day;

	public Date() {
	}

	public Date(int day, int month, int year) {
		this.month = month;
		this.year = year;
		this.day = day;
	}

	public void setMonth(int month){
		this.month = month;
	}

	public int getMonth(){
		return month;
	}

	public void setYear(int year){
		this.year = year;
	}

	public int getYear(){
		return year;
	}

	public void setDay(int day){
		this.day = day;
	}

	public int getDay(){
		return day;
	}

	public int compareTo(Date other) {
		if (year != other.getYear()) {
			return Integer.compare(other.getYear(), year);
		}
		if (month != other.getMonth()) {
			return Integer.compare(other.getMonth(), month);
		}
		return Integer.compare(other.getDay(), day);
	}

	@Override
 	public String toString(){
		return
			"Date{" +
			"month = '" + month + '\'' +
			",year = '" + year + '\'' +
			",day = '" + day + '\'' +
			"}";
		}
}
