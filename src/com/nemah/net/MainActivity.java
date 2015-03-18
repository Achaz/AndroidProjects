package com.nemah.net;

import java.util.Random;

import com.actionbarsherlock.app.SherlockActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nemah.net.adapters.BaseInflaterAdapter;
import com.nemah.net.adapters.CardItemData;
import com.actionbarsherlock.view.MenuItem;
import com.nemah.net.adapters.inflaters.CardInflater;
import com.nemah.net.materialmenu.MaterialMenuDrawable;
import com.nemah.net.materialmenu.MaterialMenuIconSherlock;
import com.nemah.net.materialmenu.MaterialMenuDrawable.IconState;
import com.nemah.net.materialmenu.MaterialMenuDrawable.Stroke;
import com.nemah.net.summary.CreditHistory;
import com.nemah.net.summary.ExpenseHistory;
import com.nemah.net.summary.SalesHistory;
import com.nemah.net.summary.StockHistory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends SherlockActivity implements OnItemClickListener {

	MaterialMenuIconSherlock materialMenu;
	private SlidingMenu slidingMenu ;
	int actionBarMenuState;
	// Create and populate a List of planet names.  
    String[] planets = new String[] { "Sales", "Stock", "Expenditure", "Credit History"};   
    Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.listview);

		ListView list = (ListView)findViewById(R.id.list_view);

		list.addHeaderView(new View(this));
		list.addFooterView(new View(this));
		
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
		
		BaseInflaterAdapter<CardItemData> adapter = new BaseInflaterAdapter<CardItemData>(new CardInflater());
		
		for (int i =0 ; i<planets.length;i++){
			
			CardItemData data = new CardItemData(planets[i]);
			adapter.addItem(data, false);
		}
		
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(this);
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
    
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		switch (position) {
		
			case 1:
				
				Intent sales = new Intent(MainActivity.this, SalesHistory.class);
				startActivity(sales);
				
				break;
				
			case 2:
				
				Intent stock = new Intent(MainActivity.this, StockHistory.class);
				startActivity(stock);
				
				break;
				
			case 3:
				
				Intent expenditure = new Intent(MainActivity.this, ExpenseHistory.class);
				startActivity(expenditure);
				
				break;
				
			case 4:
				
				Intent credit = new Intent(MainActivity.this, CreditHistory.class);
				startActivity(credit);
				
				break;
	
			default:
				break;
		}
		
	}
	
}
