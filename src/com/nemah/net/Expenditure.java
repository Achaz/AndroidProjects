package com.nemah.net;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.nemah.net.util.DateTimePicker;
import com.nemah.net.util.TransparentProgressDialog;
import com.nemah.net.util.UserFunctions;
import com.nemah.net.util.DateTimePicker.DateWatcher;

public class Expenditure extends SherlockActivity implements DateWatcher{

	MaterialMenuIconSherlock materialMenu;
	private SlidingMenu slidingMenu ;
	Spinner business,exp_payment_type,expense_type;
	BootstrapButton btn_expense;
	int actionBarMenuState;
	BootstrapEditText expdate,item_spenton,exp_amount;
	ActionBar mactionbar;
	private ArrayList<Business> BusinessList;
	private String URL_BUSINESS = "http://www.erpmastersltd.com/Agribooks/get_business.php";
	private TransparentProgressDialog pd;
	private Handler h;
	private Runnable r;
	public static final String MyPREFERENCES = "MyPrefs" ;
	public static final String username = "username"; 
    public static final String user_id = "userid"; 
    private static String KEY_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expenses);
		expdate = (BootstrapEditText)findViewById(R.id.expDate);
		item_spenton =(BootstrapEditText)findViewById(R.id.item_spenton);
		exp_amount =(BootstrapEditText)findViewById(R.id.exp_amount);
		business =(Spinner)findViewById(R.id.business);
		exp_payment_type =(Spinner)findViewById(R.id.exp_payment_type);
		expense_type =(Spinner)findViewById(R.id.expense_type);
		btn_expense =(BootstrapButton)findViewById(R.id.btn_expense);
		 btn_expense.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new PostData().execute();
			}
		});
		mactionbar = getSupportActionBar();
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
        mactionbar.setTitle("Expenditure");
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
			
			expdate = (BootstrapEditText)findViewById(R.id.expDate);
			item_spenton =(BootstrapEditText)findViewById(R.id.item_spenton);
			exp_amount =(BootstrapEditText)findViewById(R.id.exp_amount);
			business =(Spinner)findViewById(R.id.business);
			exp_payment_type =(Spinner)findViewById(R.id.exp_payment_type);
			expense_type =(Spinner)findViewById(R.id.expense_type);
			
			String expenditure_date = expdate.getText().toString();
			String expenditure_item = item_spenton.getText().toString();
			String expenditure_amount = exp_amount.getText().toString();
			String exp_business = String.valueOf(business.getSelectedItem());
			String exp_payType = String.valueOf(exp_payment_type.getSelectedItem());
			String expenditure_type = String.valueOf(expense_type.getSelectedItem());
			
			SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
			String uid = prefs.getString(user_id, "");
			String user = prefs.getString(username,"");
			
			UserFunctions userFunction = new UserFunctions();
			
			JSONObject json = userFunction.postExpenditure(expenditure_date, expenditure_item, expenditure_amount, exp_business,  exp_payType, expenditure_type, uid, user);
			
			try{
				
				int res = json.getInt(KEY_SUCCESS);
				
				if (res == 1) {
					
                	Log.d("Expenditure posted Posted!", json.toString());   
			
                	return json.getString(TAG_MESSAGE);
                	
                }else{
                	
                	Log.d("Expenditure Post Failure!", json.getString(TAG_MESSAGE));
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
			
			if (pd.isShowing() ) {
				
				pd.dismiss();
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
    @SuppressLint("InflateParams")
	public void button_click(View view){  
    	
    	// Create the dialog
    	final Dialog mDateTimeDialog = new Dialog(this);
		// Inflate the root layout
		final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater().inflate(R.layout.date_time_dialog, null);
		// Grab widget instance
		final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
		mDateTimePicker.setDateChangedListener(this); 
		 
		// Update demo TextViews when the "OK" button is clicked 
		((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mDateTimePicker.clearFocus();
				// TODO Auto-generated method stub 
				String result_string = mDateTimePicker.getMonth() + "/" + String.valueOf(mDateTimePicker.getDay()) + "/" + String.valueOf(mDateTimePicker.getYear())
						+ "  " + String.valueOf(mDateTimePicker.getHour()) + ":" + String.valueOf(mDateTimePicker.getMinute());
//				if(mDateTimePicker.getHour() > 12) result_string = result_string + "PM";
//				else result_string = result_string + "AM";
				expdate.setText(result_string);
				mDateTimeDialog.dismiss();
			}
		});

		// Cancel the dialog when the "Cancel" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDateTimeDialog.cancel();
			}
		});

		// Reset Date and Time pickers when the "Reset" button is clicked
	
		((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDateTimePicker.reset();
			}
		});
		  
		// Setup TimePicker
		// No title on the dialog window
		mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the dialog content view
		mDateTimeDialog.setContentView(mDateTimeDialogView);
		// Display the dialog
		mDateTimeDialog.show();				
    }
    @Override
	public void onDateChanged(Calendar c) {
		// TODO Auto-generated method stub
		Log.e("",
				"" + c.get(Calendar.MONTH) + " " + c.get(Calendar.DAY_OF_MONTH)
						+ " " + c.get(Calendar.YEAR));
		
	}
	

}
