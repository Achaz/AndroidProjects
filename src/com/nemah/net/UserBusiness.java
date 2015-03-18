package com.nemah.net;

import java.util.Random;

import org.json.JSONObject;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

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
import com.nemah.net.util.TransparentProgressDialog;
import com.nemah.net.util.UserFunctions;

public class UserBusiness extends SherlockActivity{
	
	ActionBar mActionBar;
	MaterialMenuIconSherlock materialMenu;
	private SlidingMenu slidingMenu ;
	int actionBarMenuState;
	private TransparentProgressDialog pd;
	private Handler h;
	private Runnable r;
	public static final String MyPREFERENCES = "MyPrefs" ;
	public static final String username = "username"; 
    public static final String user_id = "userid"; 
    private static String KEY_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	BootstrapButton btn_register_business;
	BootstrapEditText user_business;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business);
		
		btn_register_business =(BootstrapButton)findViewById(R.id.btn_register_business);
		user_business =(BootstrapEditText)findViewById(R.id.user_business);
		
		btn_register_business.setOnClickListener(new View.OnClickListener() {
			
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
		mActionBar.setTitle("Business Entry");
		
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
			String bus = user_business.getText().toString();
			SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
			String uid = prefs.getString(user_id, "");
			String user = prefs.getString(username,"");
			
			UserFunctions userFunction = new UserFunctions();
			
			JSONObject json = userFunction.postBusiness(bus, user, uid);
			
			try{
				
				int res = json.getInt(KEY_SUCCESS);
				
				if (res == 1) {
					
                	Log.d("Business Posted!", json.toString());   
			
                	return json.getString(TAG_MESSAGE);
                	
                }else{
                	
                	Log.d("Business Post Failure!", json.getString(TAG_MESSAGE));
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
				user_business =(BootstrapEditText)findViewById(R.id.user_business);
				user_business.setText("");
				
           }
			
		}

	}

}
