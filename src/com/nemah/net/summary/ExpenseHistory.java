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

public class ExpenseHistory extends SherlockActivity{

	String TAG_OS = "feeds";
	JSONArray json = null;
	
	TableLayout tl;
    TableRow tr;
    TextView expensetype,item,amount,date,paymenttype;
    
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
		
		mActionBar.setTitle("Expenses History");
		
		
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
	
		new getExpenses().execute();
		
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
    
    class getExpenses extends AsyncTask<String, String, String>{

    	
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = "http://erpmastersltd.com/Agribooks/get_expenses.php";

			TableLayout tv=(TableLayout) findViewById(R.id.maintable);
            tv.removeAllViewsInLayout();

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
			 //addData();
			
			TableLayout tv=(TableLayout) findViewById(R.id.maintable);
            tv.removeAllViewsInLayout();
            int flag=1;
			if (result != null){
				
				try {
                	
                    JSONObject jsonObj = new JSONObject(result);
                    
                    if (jsonObj != null) {
                    	
                    	JSONArray categories = jsonObj.getJSONArray("feeds");
                    	for (int i = 0; i < categories.length(); i++) {
                    		
                    		TableRow tr=new TableRow(ExpenseHistory.this);
                    		
                    		tr.setLayoutParams(new LayoutParams(
                                    LayoutParams.WRAP_CONTENT,
                                    LayoutParams.WRAP_CONTENT));
                    		
                    		if(flag ==1 ){
                    			
                    	        /** Creating a TextView to add to the row **/
                    	        TextView expenseTV = new TextView(ExpenseHistory.this);
                    	        expenseTV.setText("Expense Type");
                    	        expenseTV.setBackgroundResource(R.drawable.card_background);
                    	    	expenseTV.setTextColor(Color.BLACK);
                    	    	expenseTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	    	expenseTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(expenseTV);  // Adding textView to tablerow.
                    	        
                    	        TextView itemTV = new TextView(ExpenseHistory.this);
                    	        itemTV.setText("Item");
                    	        itemTV.setTextColor(Color.BLACK);
                    	        itemTV.setBackgroundResource(R.drawable.card_background);
                    	        itemTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        itemTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(itemTV);  // Adding textView to tablerow.
                    	        
                    	        TextView amountTV = new TextView(ExpenseHistory.this);
                    	        amountTV.setText("Amount");
                    	        amountTV.setBackgroundResource(R.drawable.card_background);
                    	        amountTV.setTextColor(Color.BLACK);
                    	        amountTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        amountTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(amountTV);  // Adding textView to tablerow.
                    	        
                    	        TextView dateTV = new TextView(ExpenseHistory.this);
                    	        dateTV.setText("Date");
                    	        dateTV.setTextColor(Color.BLACK);
                    	        dateTV.setBackgroundResource(R.drawable.card_background);
                    	        dateTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        dateTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(dateTV);  // Adding textView to tablerow.
                    	        
                    	        TextView paymentTypeTV = new TextView(ExpenseHistory.this);
                    	        paymentTypeTV.setText("Payment Type");
                    	        paymentTypeTV.setTextColor(Color.BLACK);
                    	        paymentTypeTV.setBackgroundResource(R.drawable.card_background);
                    	        paymentTypeTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        paymentTypeTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(paymentTypeTV);  // Adding textView to tablerow.
                    	   
                    	        tv.addView(tr);

                                final View vline = new View(ExpenseHistory.this);
                                vline.setLayoutParams(new       
                                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 2));
                                vline.setBackgroundColor(Color.BLACK);
                                tv.addView(vline); // add line below heading
                                flag=0;
                    			
                    		}else{
                    			
                    			JSONObject catObj = (JSONObject) categories.get(i);
                    			
                    			expensetype = new TextView(ExpenseHistory.this);
                    			expensetype.setPadding(10, 0, 0, 0);
                    			expensetype.setTextSize(15);
                    			expensetype.setBackgroundResource(R.drawable.cell_shape);
                    			String expense_type = catObj.getString("expense_type");
                    			expensetype.setText(expense_type);
                    			expensetype.setTextColor(Color.RED);
                    			expensetype.setTextSize(15);
                    			tr.addView(expensetype);
                    			
                    			item = new TextView(ExpenseHistory.this);
                    			item.setPadding(10, 0, 0, 0);
                    			item.setTextSize(15);
                    			item.setBackgroundResource(R.drawable.cell_shape);
                    			String item_type = catObj.getString("item");
                    			item.setText(item_type);
                    			item.setTextColor(Color.RED);
                    			item.setTextSize(15);
                    			tr.addView(item);
                    			
                    			amount = new TextView(ExpenseHistory.this);
                    			amount.setPadding(10, 0, 0, 0);
                    			amount.setTextSize(15);
                    			amount.setBackgroundResource(R.drawable.cell_shape);
                    			String amount_t = catObj.getString("amount");
                    			amount.setText(amount_t);
                    			amount.setTextColor(Color.RED);
                    			amount.setTextSize(15);
                    			tr.addView(amount);
                    			
                    			date = new TextView(ExpenseHistory.this);
                    			date.setPadding(10, 0, 0, 0);
                    			date.setTextSize(15);
                    			date.setBackgroundResource(R.drawable.cell_shape);
                    			String date_t = catObj.getString("date");
                    			date.setText(date_t);
                    			date.setTextColor(Color.RED);
                    			date.setTextSize(15);
                    			tr.addView(date);
                    			
                    			paymenttype = new TextView(ExpenseHistory.this);
                    			paymenttype.setPadding(10, 0, 0, 0);
                    			paymenttype.setTextSize(15);
                    			paymenttype.setBackgroundResource(R.drawable.cell_shape);
                    			String paymenttype_t = catObj.getString("payment_type");
                    			paymenttype.setText(paymenttype_t);
                    			paymenttype.setTextColor(Color.RED);
                    			paymenttype.setTextSize(15);
                    			tr.addView(paymenttype);
                    			
                    			tv.addView(tr);
                    			
                    			final View vline1 = new View(ExpenseHistory.this);
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
