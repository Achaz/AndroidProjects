package com.nemah.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nemah.net.materialmenu.MaterialMenuDrawable;
import com.nemah.net.materialmenu.MaterialMenuIconSherlock;
import com.nemah.net.materialmenu.MaterialMenuDrawable.IconState;
import com.nemah.net.materialmenu.MaterialMenuDrawable.Stroke;
import com.nemah.net.summary.ServiceHandler;
import com.nemah.net.util.Business;
import com.nemah.net.util.TransparentProgressDialog;
import com.nemah.net.util.UserFunctions;

public class Customers extends SherlockActivity{
	ActionBar mActionBar;
	MaterialMenuIconSherlock materialMenu;
	private SlidingMenu slidingMenu ;
	int actionBarMenuState;
	private ArrayList<Business> BusinessList;
	Spinner  business,payment_type;
	private String URL_BUSINESS = "http://www.erpmastersltd.com/Agribooks/get_business.php";
	private TransparentProgressDialog pd;
	private Handler h;
	private Runnable r;
	public static final String MyPREFERENCES = "MyPrefs" ;
	public static final String username = "username"; 
    public static final String user_id = "userid"; 
    private static String KEY_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
    BootstrapEditText customer_name,customer_tel,customer_email,customer_invoice_period,customer_location;
    BootstrapButton btn_customer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer);
		business =(Spinner)findViewById(R.id.business);
		payment_type =(Spinner)findViewById(R.id.cust_payment_type);
		customer_name =(BootstrapEditText)findViewById(R.id.customer_name);
		customer_tel =(BootstrapEditText)findViewById(R.id.customer_tel);
		customer_email =(BootstrapEditText)findViewById(R.id.customer_email);
		customer_invoice_period = (BootstrapEditText)findViewById(R.id.customer_invoice_period);
		customer_location =(BootstrapEditText)findViewById(R.id.customer_location);
		btn_customer =(BootstrapButton)findViewById(R.id.btn_customer);
		
		btn_customer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new PostData().execute();
			}
		});
		
		materialMenu = new MaterialMenuIconSherlock(this, Color.GRAY, Stroke.THIN);
		slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        //slidingMenu.setShadowDrawable(R.color.et_white);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);
		mActionBar= getSupportActionBar();
		mActionBar.setTitle("Customers");
		BusinessList = new ArrayList<Business>();
		
		h = new Handler();
		pd = new TransparentProgressDialog(this, R.drawable.loading_throbber_icon);
		r =new Runnable() {
			@Override
			public void run() {
				if (pd.isShowing()) {
					pd.dismiss();
				}
			}
		};
		new getBusiness().execute();
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if ( slidingMenu.isMenuShowing()) {
			
            slidingMenu.toggle();
            
            
        }else {
        	materialMenu.animatePressedState(MaterialMenuDrawable.IconState.BURGER);
            super.onBackPressed();
        }
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		materialMenu.syncState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		materialMenu.onSaveInstanceState(outState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ( keyCode == KeyEvent.KEYCODE_MENU ) {
			
            this.slidingMenu.toggle();
            materialMenu.animatePressedState(MaterialMenuDrawable.IconState.BURGER);
            return true;
        }
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == android.R.id.home){
			
			 this.slidingMenu.toggle();
			 actionBarMenuState = generateState(actionBarMenuState);
	         materialMenu.animatePressedState(intToState(actionBarMenuState));
	       
	         return true;
	         
		}else{
			
			materialMenu.animatePressedState(MaterialMenuDrawable.IconState.BURGER);
			return true;
		}
	}
	
	public static int generateState(int previous) {
		
        int generated = new Random().nextInt(2);
        return generated != previous ? generated : generateState(previous);
        
    }

    public static IconState intToState(int state) {
    	
        switch (state) {
        
            case 0:
                return IconState.BURGER;
                
            case 1:
                return IconState.ARROW;

        }
        
        throw new IllegalArgumentException("Must be a number [0,3)");
    }
    private void populateSpinner() {
    	
    	List<String> lables = new ArrayList<String>();
    	for (int i = 0; i < BusinessList.size(); i++) {
    		lables.add(BusinessList.get(i).getBusiness());
    	}
    	// Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        business.setAdapter(spinnerAdapter);
    }
    class PostData extends AsyncTask<String, String, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			business =(Spinner)findViewById(R.id.business);
			payment_type =(Spinner)findViewById(R.id.cust_payment_type);
			customer_name =(BootstrapEditText)findViewById(R.id.customer_name);
			customer_tel =(BootstrapEditText)findViewById(R.id.customer_tel);
			customer_email =(BootstrapEditText)findViewById(R.id.customer_email);
			customer_invoice_period = (BootstrapEditText)findViewById(R.id.customer_invoice_period);
			customer_location =(BootstrapEditText)findViewById(R.id.customer_location);
			
			String cust_business = String.valueOf(business.getSelectedItem());
			String cust_payment_type = String.valueOf(payment_type.getSelectedItem());
			String cust_name = customer_name.getText().toString();
			String cust_tel = customer_tel.getText().toString();
			String cust_email = customer_email.getText().toString();
			String cust_invoice_period = customer_invoice_period.getText().toString();
			String cust_location = customer_location.getText().toString();
			
			SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
			String uid = prefs.getString(user_id, "");
			String user = prefs.getString(username,"");
			
			UserFunctions userFunction = new UserFunctions();
			
			JSONObject json = userFunction.postCustomer(cust_business, cust_payment_type, cust_name, cust_tel, cust_email, cust_invoice_period, cust_location,uid,user);
			
			try{
				
				int res = json.getInt(KEY_SUCCESS);
				
				if (res == 1) {
					
                	Log.d("Customer Posted!", json.toString());   
			
                	return json.getString(TAG_MESSAGE);
                	
                }else{
                	
                	Log.d("Customer Post Failure!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);
                	
                }

			}catch (Exception e){
			
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			h.removeCallbacks(r);
			customer_name =(BootstrapEditText)findViewById(R.id.customer_name);
			customer_tel =(BootstrapEditText)findViewById(R.id.customer_tel);
			customer_email =(BootstrapEditText)findViewById(R.id.customer_email);
			customer_invoice_period = (BootstrapEditText)findViewById(R.id.customer_invoice_period);
			customer_location =(BootstrapEditText)findViewById(R.id.customer_location);
			
			if (pd.isShowing() ) {
				
				pd.dismiss();
				customer_name.setText("");
				customer_tel.setText("");
				customer_email.setText("");
				customer_invoice_period.setText("");
				customer_location.setText("");
			}
			
		}
	
    }
    class getBusiness extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
			String uid = prefs.getString(user_id, "");
			
			String NEW_URL_BUSINESS = URL_BUSINESS +"?uid="+uid;
			ServiceHandler jsonParser = new ServiceHandler();
			
			
			String json = jsonParser.makeServiceCall(NEW_URL_BUSINESS, ServiceHandler.GET);
			
			Log.e("Response: ", "> " + json);
			if (json != null) {
            	
                try {
                	
                    JSONObject jsonObj = new JSONObject(json);
                    
                    if (jsonObj != null) {
                    	
                        JSONArray categories = jsonObj.getJSONArray("businesses");                        
 
                        for (int i = 0; i < categories.length(); i++) {
                        	
                            JSONObject catObj = (JSONObject) categories.get(i);
                            
                            Business bus = new Business(catObj.getInt("id"), catObj.getString("business"));
                         
                            BusinessList.add(bus);
                         
                        }
                    }
 
                } catch (JSONException e) {
                	
                    e.printStackTrace();
                }
 
            } else {
            	
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			h.removeCallbacks(r);
			
			if (pd.isShowing() ) {
				
				pd.dismiss();
				populateSpinner();
			}
			
		}

    }

}
