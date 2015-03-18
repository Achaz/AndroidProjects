package com.nemah.net.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;

public class UserFunctions {
	
	private JSONParser jsonParser;
   
    private static String loginURL = "http://www.erpmastersltd.com/Agribooks/webservice/login.php";
    private static String registerURL = "http://www.erpmastersltd.com/Agribooks/webservice/register.php";
    private static String creditURL = "http://www.erpmastersltd.com/Agribooks/webservice/credit.php";
    private static String purchasesURL = "http://www.erpmastersltd.com/Agribooks/webservice/purchases.php";
    private static String salesURL = "http://www.erpmastersltd.com/Agribooks/webservice/sales.php";
    private static String expURL = "http://www.erpmastersltd.com/Agribooks/webservice/expenditure.php";
    private static String customerURL ="http://www.erpmastersltd.com/Agribooks/webservice/customer.php";
    private static String supplierURL ="http://www.erpmastersltd.com/Agribooks/webservice/Supplier.php";
    private static String businessURL ="http://www.erpmastersltd.com/Agribooks/webservice/business.php";
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
    
    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String name, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("password", password));
        
        JSONObject json = jsonParser.makeHttpRequest(
        		loginURL, "POST", params);
    
     // check your log for json response
        Log.d("Login attempt", json.toString());
        
        return json;
    }
    
    /**
     * function make Login Request
     * @param name
     * @param name
     * @param password
     * */
    public JSONObject registerUser(String name, String email, String password, String tel, String location,String username,String opening_bal){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
 
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("tel", tel));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("user_name", username));
        params.add(new BasicNameValuePair("location", location));
        params.add(new BasicNameValuePair("opening_bal", opening_bal));
       
        
        JSONObject json = jsonParser.makeHttpRequest(
        		registerURL, "POST", params);
        
     // full json response
        Log.d("Register attempt", json.toString());
         
   
        return json;
    }
    public JSONObject postBusiness(String business,String user,String uid){
    	
    	List<NameValuePair>params = new ArrayList<NameValuePair>();
    	
    	params.add(new BasicNameValuePair("username", user));
    	params.add(new BasicNameValuePair("uid", uid));
    	params.add(new BasicNameValuePair("business", business));
    	
    	JSONObject json = jsonParser.makeHttpRequest(
   			 businessURL, "POST", params);
 
    	return json;
 
    }
    public JSONObject postCredit(String totalAmount,String installAmount,String paySchedule,String startdate,String enddate, String user, String uid,String business){
    	
    	List<NameValuePair>params = new ArrayList<NameValuePair>();
   
    	params.add(new BasicNameValuePair("totalAmount", totalAmount));
    	params.add(new BasicNameValuePair("installAmount", installAmount));
    	params.add(new BasicNameValuePair("paySchedule", paySchedule));
    	params.add(new BasicNameValuePair("startDate", startdate));
    	params.add(new BasicNameValuePair("endDate", enddate));
    	params.add(new BasicNameValuePair("user", user));
    	params.add(new BasicNameValuePair("uid", uid));
    	params.add(new BasicNameValuePair("business", business));
    	
    	
    	 JSONObject json = jsonParser.makeHttpRequest(
    			 creditURL, "POST", params);
  
    	return json;
    }
    public JSONObject postCustomer(String business,String payment_type,String customer_name,String customer_tel,String customer_email,String customer_invoice_period,String cust_location,String uid,String username){
    	
    	List<NameValuePair>params = new ArrayList<NameValuePair>();
    	
    	params.add(new BasicNameValuePair("business", business));
    	params.add(new BasicNameValuePair("payment_type", payment_type));
    	params.add(new BasicNameValuePair("customer_name", customer_name));
    	params.add(new BasicNameValuePair("customer_tel", customer_tel));
    	params.add(new BasicNameValuePair("customer_email", customer_email));
    	params.add(new BasicNameValuePair("customer_invoice_period", customer_invoice_period));
    	params.add(new BasicNameValuePair("cust_location", cust_location));
    	params.add(new BasicNameValuePair("uid", uid));
    	params.add(new BasicNameValuePair("username", username));
    	
    	JSONObject json = jsonParser.makeHttpRequest(
    			customerURL, "POST", params);
    	
  
    	return json;
    	
    }
    public JSONObject postPurchase(String item,int qty,int price, String user,String uid,String business, String purchase_date,String purchase_type,String supplier){
    	
    	int total = qty*price;
    	
    	String post_qty = Integer.toString(qty);
    	String post_price = Integer.toString(price);
    	String post_total = Integer.toString(total);
    	List<NameValuePair>params = new ArrayList<NameValuePair>();
  
    	params.add(new BasicNameValuePair("item", item));
    	params.add(new BasicNameValuePair("price", post_price));
    	params.add(new BasicNameValuePair("qty", post_qty));
    	params.add(new BasicNameValuePair("total", post_total));
    	params.add(new BasicNameValuePair("user", user));
    	params.add(new BasicNameValuePair("uid", uid));
    	params.add(new BasicNameValuePair("business", business));
    	params.add(new BasicNameValuePair("purchase_type", purchase_type));
    	params.add(new BasicNameValuePair("purchase_date", purchase_date));
    	params.add(new BasicNameValuePair("supplier", supplier));
    	
    	JSONObject json = jsonParser.makeHttpRequest(
    			purchasesURL, "POST", params);
    	
  
    	return json;
    }
    
    public JSONObject postSales(String sales_bus, int qty, int price,String user, String uid,String spayment_type,String sItem, String sales_date,String sales_invoice,String customer,String metric,String vat){
    	
    	int total = qty*price;
    	
    	String post_sales_qty = Integer.toString(qty);
    	String post_sales_price = Integer.toString(price);
    	String post_sales_total = Integer.toString(total);
    	
    	List<NameValuePair>params = new ArrayList<NameValuePair>();
   
    	params.add(new BasicNameValuePair("qty", post_sales_qty));
    	params.add(new BasicNameValuePair("price", post_sales_price));
    	params.add(new BasicNameValuePair("total", post_sales_total));
    	params.add(new BasicNameValuePair("item", sItem));
    	params.add(new BasicNameValuePair("uid", uid));
    	params.add(new BasicNameValuePair("user", user));
    	params.add(new BasicNameValuePair("business", sales_bus));
    	params.add(new BasicNameValuePair("sales_invoice", sales_invoice));
    	params.add(new BasicNameValuePair("sales_date", sales_date));
    	params.add(new BasicNameValuePair("customer", customer));
    	params.add(new BasicNameValuePair("metric", metric));
    	params.add(new BasicNameValuePair("spayment_type", spayment_type));
    	params.add(new BasicNameValuePair("vat", vat));
    	JSONObject json = jsonParser.makeHttpRequest(
    			salesURL, "POST", params);
    	
  
    	return json;
    }
    public JSONObject postSupplier(String supp_business,String payment_type,String email,String name,String tel,String invoice_period,String supp_Location,String uid,String user){
    	
    	List<NameValuePair>params = new ArrayList<NameValuePair>();
    	
    	params.add(new BasicNameValuePair("supp_business", supp_business));
    	params.add(new BasicNameValuePair("payment_type", payment_type));
    	params.add(new BasicNameValuePair("email", email));
    	params.add(new BasicNameValuePair("name", name));
    	params.add(new BasicNameValuePair("tel", tel));
    	params.add(new BasicNameValuePair("invoice_period", invoice_period));
    	params.add(new BasicNameValuePair("supp_Location", supp_Location));
    	params.add(new BasicNameValuePair("uid", uid));
    	params.add(new BasicNameValuePair("user", user));
    	
    	JSONObject json = jsonParser.makeHttpRequest(
    			supplierURL, "POST", params);
    	
    
    	return json;
    	
    }
    public JSONObject postExpenditure (String expDate, String item_spenton, String exp_amount,String business ,String exp_payment_type,String expense_type,String uid,String user){
    	
    	List<NameValuePair>params = new ArrayList<NameValuePair>();
  
    	params.add(new BasicNameValuePair("expDate", expDate));
    	params.add(new BasicNameValuePair("item_spenton", item_spenton));
    	params.add(new BasicNameValuePair("expAmount", exp_amount));
    	params.add(new BasicNameValuePair("uid", uid));
    	params.add(new BasicNameValuePair("user", user));
    	params.add(new BasicNameValuePair("business", business));
    	params.add(new BasicNameValuePair("exp_payment_type", exp_payment_type));
    	params.add(new BasicNameValuePair("expense_type", expense_type));
    	
    	JSONObject json = jsonParser.makeHttpRequest(
    			expURL, "POST", params);
    	
    
    	return json;
    	
    }
    
}
