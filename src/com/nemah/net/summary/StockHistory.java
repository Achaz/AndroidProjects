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

public class StockHistory extends SherlockActivity{

	String TAG_OS = "feeds";
	JSONArray json = null;
	
	TableLayout tl;
    TableRow tr;
   
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
		
		mActionBar.setTitle("Stock");
		
		
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
			String url = "http://www.erpmastersltd.com/Agribooks/get_stock.php?purchase_type=stock";

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
                    		
                    		TableRow tr=new TableRow(StockHistory.this);
                    		
                    		tr.setLayoutParams(new LayoutParams(
                                    LayoutParams.WRAP_CONTENT,
                                    LayoutParams.WRAP_CONTENT));
                    		
                    		if(flag ==1 ){
                    			
                    	        /** Creating a TextView to add to the row **/
                    	        TextView expenseTV = new TextView(StockHistory.this);
                    	        expenseTV.setText("Item");
                    	        expenseTV.setBackgroundResource(R.drawable.card_background);
                    	    	expenseTV.setTextColor(Color.BLACK);
                    	    	expenseTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	    	expenseTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(expenseTV);  // Adding textView to tablerow.
                    	        
                    	        TextView itemTV = new TextView(StockHistory.this);
                    	        itemTV.setText("Qty");
                    	        itemTV.setBackgroundResource(R.drawable.card_background);
                    	        itemTV.setTextColor(Color.BLACK);
                    	        itemTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        itemTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(itemTV);  // Adding textView to tablerow.
                    	        
                    	        TextView amountTV = new TextView(StockHistory.this);
                    	        amountTV.setText("Price");
                    	        amountTV.setBackgroundResource(R.drawable.card_background);
                    	        amountTV.setTextColor(Color.BLACK);
                    	        amountTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        amountTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(amountTV);  // Adding textView to tablerow.
                    	        
                    	        TextView dateTV = new TextView(StockHistory.this);
                    	        dateTV.setText("Supplier");
                    	        dateTV.setBackgroundResource(R.drawable.card_background);
                    	        dateTV.setTextColor(Color.BLACK);
                    	        dateTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        dateTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(dateTV);  // Adding textView to tablerow.
                    	        
                    	        TextView paymentTypeTV = new TextView(StockHistory.this);
                    	        paymentTypeTV.setText("Purchase Date");
                    	        paymentTypeTV.setBackgroundResource(R.drawable.card_background);
                    	        paymentTypeTV.setTextColor(Color.BLACK);
                    	        paymentTypeTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    	        paymentTypeTV.setPadding(10, 0, 0, 0);
                    	        tr.addView(paymentTypeTV);  // Adding textView to tablerow.
                    	   
                    	        tv.addView(tr);

                                final View vline = new View(StockHistory.this);
                                vline.setLayoutParams(new       
                                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 2));
                                vline.setBackgroundColor(Color.BLACK);
                                tv.addView(vline); // add line below heading
                                flag=0;
                    			
                    		}else{
                    			
                    			JSONObject catObj = (JSONObject) categories.get(i);
                    			
                    			TextView itemTV = new TextView(StockHistory.this);
                    			itemTV.setPadding(10, 0, 0, 0);
                    			itemTV.setTextSize(15);
                    			itemTV.setBackgroundResource(R.drawable.cell_shape);
                    			String item = catObj.getString("item");
                    			itemTV.setText(item);
                    			itemTV.setTextColor(Color.RED);
                    			itemTV.setTextSize(15);
                    			tr.addView(itemTV);
                    			
                    			TextView Qty = new TextView(StockHistory.this);
                    			Qty.setPadding(10, 0, 0, 0);
                    			Qty.setTextSize(15);
                    			Qty.setBackgroundResource(R.drawable.cell_shape);
                    			String quantity = catObj.getString("quantity");
                    			Qty.setText(quantity);
                    			Qty.setTextColor(Color.RED);
                    			Qty.setTextSize(15);
                    			tr.addView(Qty);
                    			
                    			TextView priceTV = new TextView(StockHistory.this);
                    			priceTV.setPadding(10, 0, 0, 0);
                    			priceTV.setTextSize(15);
                    			priceTV.setBackgroundResource(R.drawable.cell_shape);
                    			String price = catObj.getString("price");
                    			priceTV.setText(price);
                    			priceTV.setTextColor(Color.RED);
                    			priceTV.setTextSize(15);
                    			tr.addView(priceTV);
                    			
                    			TextView supplierTV = new TextView(StockHistory.this);
                    			supplierTV.setPadding(10, 0, 0, 0);
                    			supplierTV.setTextSize(15);
                    			supplierTV.setBackgroundResource(R.drawable.cell_shape);
                    			String supplier = catObj.getString("supplier");
                    			supplierTV.setText(supplier);
                    			supplierTV.setTextColor(Color.RED);
                    			supplierTV.setTextSize(15);
                    			tr.addView(supplierTV);
                    			
                    			TextView purchase_date = new TextView(StockHistory.this);
                    			purchase_date.setPadding(10, 0, 0, 0);
                    			purchase_date.setTextSize(15);
                    			purchase_date.setBackgroundResource(R.drawable.cell_shape);
                    			String pdate = catObj.getString("purchase_date");
                    			purchase_date.setText(pdate);
                    			purchase_date.setTextColor(Color.RED);
                    			purchase_date.setTextSize(15);
                    			tr.addView(purchase_date);
                    			
                    			tv.addView(tr);
                    			
                    			final View vline1 = new View(StockHistory.this);
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
