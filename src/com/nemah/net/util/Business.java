package com.nemah.net.util;

public class Business {
	
	private int id;
    private String business;
    
    
    public Business(){
    	
    }
    public Business(int id,String business){
    	this.id = id;
    	this.business = business;
    }
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
    
    
}
