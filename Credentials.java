package com.tomitg.www;

import java.util.Calendar;

public class Credentials {
	private String username;
	private String password;
	private boolean clockedIn;
	private String clockTime;
	
	public Credentials(){
	}
	
	/**
	 * @author <strong>Laguardia</strong> - Tried this constructor for testing purposes
	 * @param username
	 * @param password
	 */
	public Credentials(String username, String password){
		setUsername(username);
		setPassword(password);
	}
	
	public Credentials(String username, String password, boolean clockedIn){
		setUsername(username);
		setPassword(password);
		setClockedIn(clockedIn);
	}
	
	public void setUsername(String val){
		this.username = val;
	}
	
	public void setPassword(String val){
		this.password = val;
	}
	
	public void setClockedIn(boolean val){
		this.clockedIn = val;
	}
	
	public String getNowDateString(){
		//returns string in format: YYYY-mm-DD hh:MM:SS.Mil 
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int hour = Calendar.getInstance().get(Calendar.HOUR);
		int min = Calendar.getInstance().get(Calendar.MINUTE);
		int sec = Calendar.getInstance().get(Calendar.SECOND);
		int mili = Calendar.getInstance().get(Calendar.MILLISECOND);
		int ampm = Calendar.getInstance().get(Calendar.AM_PM);
		
		return year + "-" + (month + 1) + "-" + day + " - " 
				+ ((hour < 10) ? ("0" + hour) : hour) + ":"
			    + ((min < 10) ? ("0" + min) : min) + ":" 
				+ ((sec < 10) ? ("0" + sec) : sec) + "." 
			    + ((mili < 10) ? ("0" + mili) : mili) + " " 
				+ ((ampm < 1) ? "AM" : "PM"); 
	}
	
	public long getNowEpochSeconds(){
		return (Calendar.getInstance().getTimeInMillis() / 1000);
	}
	
	public String getClockTime(){
		return clockTime;
	}
	
	public void setClockTime(String val){
		clockTime = val;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public boolean getClockedIn(){
		return clockedIn;
	}
	
	public String toString(){
		return username + ", " + password;
	}
}
