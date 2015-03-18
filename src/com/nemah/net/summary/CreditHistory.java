package com.nemah.net.summary;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nemah.net.R;
import com.nemah.net.materialmenu.MaterialMenuDrawable;
import com.nemah.net.materialmenu.MaterialMenuIconSherlock;
import com.nemah.net.materialmenu.MaterialMenuDrawable.IconState;
import com.nemah.net.materialmenu.MaterialMenuDrawable.Stroke;
import com.nemah.net.util.TransparentProgressDialog;

public class CreditHistory extends SherlockActivity{

	String TAG_OS = "feeds";
	JSONArray json = null;
	
	private TransparentProgressDialog pd;
	private Handler h;
	private Runnable r;
	
	ActionBar mActionBar;
	MaterialMenuIconSherlock materialMenu;
	private SlidingMenu slidingMenu ;
	int actionBarMenuState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.summary);
		
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
		
		mActionBar.setTitle("Credit History");
		
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
		
		new getCredit().execute();
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
    
	class getCredit extends AsyncTask<String, String, String>{

		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = "http://erpmastersltd.com/Agribooks/get_credit.php";
			ServiceHandler jsonParser = new ServiceHandler();
			
			String json = jsonParser.makeServiceCall(url, ServiceHandler.GET);
			Log.e("Response: ", "> " + json);
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			h.removeCallbacks(r);
			
			if (pd.isShowing() ) {
				
				pd.dismiss();
			}
			
			TableLayout tv=(TableLayout) findViewById(R.id.maintable);
            tv.removeAllViewsInLayout();
            int flag=1;
			if (result != null){
				
				try {
                	
                    JSONObject jsonObj = new JSONObject(result);
                    
                    if (jsonObj != null) {
                    	
                    	JSONArray categories = jsonObj.getJSONArray("feeds");
                    	
                    	for (int i = 0; i < categories.length(); i++) {
                    		
                    		TableRow tr=new TableRow(CreditHistory.this);
                    		
                    		tr.setLayoutParams(new LayoutParams(
                                    LayoutParams.WRAP_CONTENT,
                                    LayoutParams.WRAP_CONTENT));
                    		
                    		if(flag ==1 ){
                    			
                    	        /** Creating a TextView to add to the row **/
                    	        TextView totalloanTV = new TextView(CreditHistory.this);
                    	        totalloanTV.setText("Total Loan");
                    	        totalloanTV.setTextColor(Color.BLACK);
                    	        totalloanTV.setBackgroundResource(R.drawable.card_background);
                    	        totalloanTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        totalloanTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(totalloanTV);  // Adding textView to tablerow.
                    	        
                    	        TextView itemTV = new TextView(CreditHistory.this);
                    	        itemTV.setText("Payment Schedule");
                    	        itemTV.setTextColor(Color.BLACK);
                    	        itemTV.setBackgroundResource(R.drawable.card_background);
                    	        itemTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        itemTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(itemTV);  // Adding textView to tablerow.
                    	        
                    	        TextView instamountTV = new TextView(CreditHistory.this);
                    	        instamountTV.setText("Install. Amount");
                    	        instamountTV.setTextColor(Color.BLACK);
                    	        instamountTV.setBackgroundResource(R.drawable.card_background);
                    	        instamountTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        instamountTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(instamountTV);  // Adding textView to tablerow.
                    	        
                    	        TextView startdateTV = new TextView(CreditHistory.this);
                    	        startdateTV.setText("Start Date");
                    	        startdateTV.setTextColor(Color.BLACK);
                    	        startdateTV.setBackgroundResource(R.drawable.card_background);
                    	        startdateTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        startdateTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(startdateTV);  // Adding textView to tablerow.
                    	        
                    	        TextView enddateTV = new TextView(CreditHistory.this);
                    	        enddateTV.setText("End Date");
                    	        enddateTV.setTextColor(Color.BLACK);
                    	        enddateTV.setBackgroundResource(R.drawable.card_background);
                    	        enddateTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        enddateTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(enddateTV);  // Adding textView to tablerow.
                    	   
                    	        tv.addView(tr);

                                final View vline = new View(CreditHistory.this);
                                vline.setLayoutParams(new       
                                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 2));
                                vline.setBackgroundColor(Color.BLACK);
                                tv.addView(vline); // add line below heading
                                flag=0;
                    			
                    		}else{
                    			
                    			JSONObject catObj = (JSONObject) categories.get(i);
                    			
                    			TextView totalloan = new TextView(CreditHistory.this);
                    			totalloan.setPadding(10, 0, 0, 0);
                    			totalloan.setTextSize(15);
                    			String total_loan = catObj.getString("total_loan");
                    			totalloan.setText(total_loan);
                    			totalloan.setBackgroundResource(R.drawable.cell_shape);
                    			totalloan.setTextColor(Color.RED);
                    			totalloan.setTextSize(15);
                    			tr.addView(totalloan);
                    			
                    			TextView payment_schedule = new TextView(CreditHistory.this);
                    			payment_schedule.setPadding(10, 0, 0, 0);
                    			payment_schedule.setTextSize(15);
                    			String paymentSched = catObj.getString("payment_schedule");
                    			payment_schedule.setText(paymentSched);
                    			payment_schedule.setBackgroundResource(R.drawable.cell_shape);
                    			payment_schedule.setTextColor(Color.RED);
                    			payment_schedule.setTextSize(15);
                    			tr.addView(payment_schedule);
                    			
                    			TextView installment_amount = new TextView(CreditHistory.this);
                    			installment_amount.setPadding(10, 0, 0, 0);
                    			installment_amount.setTextSize(15);
                    			String installAmount = catObj.getString("installment_amount");
                    			installment_amount.setText(installAmount);
                    			installment_amount.setBackgroundResource(R.drawable.cell_shape);
                    			installment_amount.setTextColor(Color.RED);
                    			installment_amount.setTextSize(15);
                    			tr.addView(installment_amount);
                    			
                    			TextView start_date = new TextView(CreditHistory.this);
                    			start_date.setPadding(10, 0, 0, 0);
                    			start_date.setTextSize(15);
                    			String sdate = catObj.getString("start_date");
                    			start_date.setText(sdate);
                    			start_date.setBackgroundResource(R.drawable.cell_shape);
                    			start_date.setTextColor(Color.RED);
                    			start_date.setTextSize(15);
                    			tr.addView(start_date);
                    			
                    			TextView end_date = new TextView(CreditHistory.this);
                    			end_date.setPadding(10, 0, 0, 0);
                    			end_date.setTextSize(15);
                    			String edate = catObj.getString("end_date");
                    			end_date.setText(edate);
                    			end_date.setBackgroundResource(R.drawable.cell_shape);
                    			end_date.setTextColor(Color.RED);
                    			end_date.setTextSize(15);
                    			tr.addView(end_date);
                    			
                    			tv.addView(tr);
                    			
                    			final View vline1 = new View(CreditHistory.this);
                                vline1.setLayoutParams(new                
                                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 1));
                                vline1.setBackgroundColor(Color.WHITE);
                    		}
                  		
                    	}
                    	
                    }
  
				}catch (JSONException e) {
                	
                    e.printStackTrace();
                }
				
			}else {
            	
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
			
		}
	
	}

}
