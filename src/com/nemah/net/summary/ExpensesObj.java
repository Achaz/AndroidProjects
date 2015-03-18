package com.nemah.net.summary;

public class ExpensesObj {
	
	private String date;
	private String reason;
	private String amount;
	private String paymentType;
	private String expenseType;
	
	public ExpensesObj(){
		
	}
	public ExpensesObj(String date,String reason,String amount,String paymentType,String expenseType){
		
		this.date = date;
		this.reason = reason;
		this.amount = amount;
		this.paymentType = paymentType;
		this.expenseType = expenseType;
		
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getExpenseType() {
		return expenseType;
	}
	public void setExpenseType(String expenseType) {
		this.expenseType = expenseType;
	}
	
	

}
