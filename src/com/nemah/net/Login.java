package com.nemah.net;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.nemah.net.util.TransparentProgressDialog;
import com.nemah.net.util.UserFunctions;

public class Login extends SherlockActivity{

	BootstrapEditText username,password;
	
	BootstrapButton btnlogin;
	Button btnLinkToRegisterScreen;
	ActionBar mActionBar;
	
	// JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    
    private TransparentProgressDialog pd;
	private Handler h;
	private Runnable r;
	
	public static final String MyPREFERENCES = "MyPrefs" ;
    
    public static final String pusername = "username"; 
    public static final String user_id = "userid"; 
    SharedPreferences sharedpreferences;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		mActionBar = getSupportActionBar();
	    username = (BootstrapEditText)findViewById(R.id.login_username);
		password = (BootstrapEditText)findViewById(R.id.login_password);
		btnlogin = (BootstrapButton)findViewById(R.id.btnlogin);
		btnLinkToRegisterScreen = (Button)findViewById(R.id.btnLinkToRegisterScreen);
		
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
		
		btnlogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                new AttemptLogin().execute();
				
			}
		});
		
		btnLinkToRegisterScreen.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Login.this,Register.class);
				startActivity(intent);
				finish();
				
			}
		});
	}
	
	class AttemptLogin extends AsyncTask<String, String, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pd.show();
            
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String user = username.getText().toString();
			String pass = password.getText().toString();
			
			UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.loginUser(user, pass);
            
            int success;
			
            try{
            	
        		success = json.getInt(KEY_SUCCESS);
        		
        		if (success == 1) {
        			
                	Log.d("Login Successful!", json.toString());
                	String res_name = json.getString(KEY_NAME);
                	String res_uid = json.getString(KEY_UID);
                	Intent i = new Intent(Login.this, MainActivity.class);

                	SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
           		 	editor.putString(pusername, res_name);
           		 	editor.putString(user_id, res_uid);
           		 	editor.commit();
                	
    				startActivity(i);
    				finish();
                	return json.getString(TAG_MESSAGE);
                	
                }else{
                	
                	Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);
                	
                }
             
	        }catch (JSONException e) {
	        	
	            e.printStackTrace();
	        }
	            
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			// TODO Auto-generated method stub
	
			h.removeCallbacks(r);
			
			if (pd.isShowing() ) {
				
				pd.dismiss();
			}
            if (file_url != null){
            	
            	Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
            }
            
		}
		
	}

}
