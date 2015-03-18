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

public class SalesHistory extends SherlockActivity{

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
		
		mActionBar.setTitle("Sales");
		
		
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
		
		new getSales().execute();
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
    
	class getSales extends AsyncTask<String, String, String>{

		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = "http://www.erpmastersltd.com/Agribooks/getSales.php";

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
			TableLayout tv=(TableLayout) findViewById(R.id.maintable);
            tv.removeAllViewsInLayout();
            int flag=1;
			if (result != null){
				
				try {
                	
                    JSONObject jsonObj = new JSONObject(result);
                    
                    if (jsonObj != null) {
                    	
                    	JSONArray categories = jsonObj.getJSONArray("feeds");
                    	for (int i = 0; i < categories.length(); i++) {
                    		
                    		TableRow tr=new TableRow(SalesHistory.this);
                    		
                    		tr.setLayoutParams(new LayoutParams(
                                    LayoutParams.WRAP_CONTENT,
                                    LayoutParams.WRAP_CONTENT));
                    		
                    		if(flag ==1 ){
                    			
                    	        /** Creating a TextView to add to the row **/
                    	        TextView expenseTV = new TextView(SalesHistory.this);
                    	        expenseTV.setText("Item");
                    	        expenseTV.setBackgroundResource(R.drawable.card_background);
                    	    	expenseTV.setTextColor(Color.BLACK);
                    	    	expenseTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	    	expenseTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(expenseTV);  // Adding textView to tablerow.
                    	        
                    	        TextView itemTV = new TextView(SalesHistory.this);
                    	        itemTV.setText("Qty");
                    	        itemTV.setBackgroundResource(R.drawable.card_background);
                    	        itemTV.setTextColor(Color.BLACK);
                    	        itemTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        itemTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(itemTV);  // Adding textView to tablerow.
                    	        
                    	        TextView amountTV = new TextView(SalesHistory.this);
                    	        amountTV.setText("Metric");
                    	        amountTV.setBackgroundResource(R.drawable.card_background);
                    	        amountTV.setTextColor(Color.BLACK);
                    	        amountTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        amountTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(amountTV);  // Adding textView to tablerow.
                    	        
                    	        TextView dateTV = new TextView(SalesHistory.this);
                    	        dateTV.setText("Payment");
                    	        dateTV.setBackgroundResource(R.drawable.card_background);
                    	        dateTV.setTextColor(Color.BLACK);
                    	        dateTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        dateTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(dateTV);  // Adding textView to tablerow.
                    	        
                    	        TextView paymentTypeTV = new TextView(SalesHistory.this);
                    	        paymentTypeTV.setText("Sales Date");
                    	        paymentTypeTV.setBackgroundResource(R.drawable.card_background);
                    	        paymentTypeTV.setTextColor(Color.BLACK);
                    	        paymentTypeTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        paymentTypeTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(paymentTypeTV);  // Adding textView to tablerow.
                    	        
                    	        TextView invoiceTV = new TextView(SalesHistory.this);
                    	        invoiceTV.setText("Invoice No.");
                    	        invoiceTV.setBackgroundResource(R.drawable.card_background);
                    	        invoiceTV.setTextColor(Color.BLACK);
                    	        invoiceTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        invoiceTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(invoiceTV);  // Adding textView to tablerow.
                    	        
                    	        TextView paymethodTV = new TextView(SalesHistory.this);
                    	        paymethodTV.setText("Payment Method");
                    	        paymethodTV.setBackgroundResource(R.drawable.card_background);
                    	        paymethodTV.setTextColor(Color.BLACK);
                    	        paymethodTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        paymethodTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(paymethodTV);  // Adding textView to tablerow.
                    	   
                    	        tv.addView(tr);

                                final View vline = new View(SalesHistory.this);
                                vline.setLayoutParams(new       
                                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 2));
                                vline.setBackgroundColor(Color.BLACK);
                                tv.addView(vline); // add line below heading
                                flag=0;
                    			
                    		}else{
                    			
                    			JSONObject catObj = (JSONObject) categories.get(i);
                    			
                    			TextView itemTV = new TextView(SalesHistory.this);
                    			itemTV.setPadding(10, 0, 0, 0);
                    			itemTV.setTextSize(15);
                    			itemTV.setBackgroundResource(R.drawable.cell_shape);
                    			String item = catObj.getString("item");
                    			itemTV.setText(item);
                    			itemTV.setTextColor(Color.RED);
                    			itemTV.setTextSize(15);
                    			tr.addView(itemTV);
                    			
                    			TextView Qty = new TextView(SalesHistory.this);
                    			Qty.setPadding(10, 0, 0, 0);
                    			Qty.setTextSize(15);
                    			Qty.setBackgroundResource(R.drawable.cell_shape);
                    			String quantity = catObj.getString("quantity");
                    			Qty.setText(quantity);
                    			Qty.setTextColor(Color.RED);
                    			Qty.setTextSize(15);
                    			tr.addView(Qty);
                    			
                    			TextView metricTV = new TextView(SalesHistory.this);
                    			metricTV.setPadding(10, 0, 0, 0);
                    			metricTV.setTextSize(15);
                    			metricTV.setBackgroundResource(R.drawable.cell_shape);
                    			String metric = catObj.getString("metric");
                    			metricTV.setText(metric);
                    			metricTV.setTextColor(Color.RED);
                    			metricTV.setTextSize(15);
                    			tr.addView(metricTV);
                    			
                    			TextView money_madeTV = new TextView(SalesHistory.this);
                    			money_madeTV.setPadding(10, 0, 0, 0);
                    			money_madeTV.setTextSize(15);
                    			money_madeTV.setBackgroundResource(R.drawable.cell_shape);
                    			String money_made = catObj.getString("money_made");
                    			money_madeTV.setText(money_made);
                    			money_madeTV.setTextColor(Color.RED);
                    			money_madeTV.setTextSize(15);
                    			tr.addView(money_madeTV);
                    			
                    			TextView sales_datetv = new TextView(SalesHistory.this);
                    			sales_datetv.setPadding(10, 0, 0, 0);
                    			sales_datetv.setTextSize(15);
                    			sales_datetv.setBackgroundResource(R.drawable.cell_shape);
                    			String sales_date = catObj.getString("sales_date");
                    			sales_datetv.setText(sales_date);
                    			sales_datetv.setTextColor(Color.RED);
                    			sales_datetv.setTextSize(15);
                    			tr.addView(sales_datetv);
                    			
                    			TextView invoice_numbertv = new TextView(SalesHistory.this);
                    			invoice_numbertv.setPadding(10, 0, 0, 0);
                    			invoice_numbertv.setTextSize(15);
                    			invoice_numbertv.setBackgroundResource(R.drawable.cell_shape);
                    			String invoice_number = catObj.getString("invoice_number");
                    			invoice_numbertv.setText(invoice_number);
                    			invoice_numbertv.setTextColor(Color.RED);
                    			invoice_numbertv.setTextSize(15);
                    			tr.addView(invoice_numbertv);
                    			
                    			TextView payment_methodstv = new TextView(SalesHistory.this);
                    			payment_methodstv.setPadding(10, 0, 0, 0);
                    			payment_methodstv.setTextSize(15);
                    			payment_methodstv.setBackgroundResource(R.drawable.cell_shape);
                    			String payment_methods = catObj.getString("payment_methods");
                    			payment_methodstv.setText(payment_methods);
                    			payment_methodstv.setTextColor(Color.RED);
                    			payment_methodstv.setTextSize(15);
                    			tr.addView(payment_methodstv);
             
                    			tv.addView(tr);
                    			
                    			final View vline1 = new View(SalesHistory.this);
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
