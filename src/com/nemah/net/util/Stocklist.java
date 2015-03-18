package com.nemah.net.util;

public class Stocklist {
	
	private String item;
	private int Qty;
	
	public Stocklist(){
		
	}
	
	public Stocklist(int qty,String item){
		this.Qty = qty;
		this.item = item;
	}
	
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public int getQty() {
		return Qty;
	}
	public void setQty(int qty) {
		Qty = qty;
	}
	

}
