package com.nemah.net.util;

public class StockQty {
	
	private int id;
	private int quantity;
	
	public StockQty(){
		
	}
	
	public StockQty(int id,int qty){
		this.id = id;
		this.quantity =qty;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
