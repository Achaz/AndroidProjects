package com.nemah.net;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.nemah.net.util.UserFunctions;

public class Register extends SherlockActivity{

	
	BootstrapEditText username,password,email,telephone,location,name,openingBal;
	BootstrapButton register;
	ProgressDialog pDialog;
	// JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    TextView registerErrorMsg;
    ActionBar mActionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		mActionBar = getSupportActionBar();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		mActionBar.setTitle("Register");
		username = (BootstrapEditText)findViewById(R.id.reg_username);
		password = (BootstrapEditText)findViewById(R.id.reg_password);
		email = (BootstrapEditText)findViewById(R.id.email);
		openingBal = (BootstrapEditText)findViewById(R.id.opening_bal);
		telephone = (BootstrapEditText)findViewById(R.id.tel);
		location = (BootstrapEditText)findViewById(R.id.location);
		
		name = (BootstrapEditText)findViewById(R.id.reg_name);
		register = (BootstrapButton)findViewById(R.id.btn_register);
		registerErrorMsg = (TextView)findViewById(R.id.register_error);
		
		register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				AlertDialog.Builder builder1 = new AlertDialog.Builder(Register.this);
	            builder1.setMessage("Please check if information you've entered is correct.");
	            builder1.setCancelable(true);
	            
	            builder1.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
	            	
	                public void onClick(DialogInterface dialog, int id) {
	                   // dialog.cancel();
	                    new PostData().execute();
	                }
	            });
	            
	            builder1.setNegativeButton("No",new DialogInterface.OnClickListener() {
	            	
	                public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	                }
	            });

	            AlertDialog alert11 = builder1.create();
	            alert11.show();

			}
		});
		
		
	}
	public void signOut() {
    	
        Intent intent = new Intent(Register.this, Login.class);

    	if(Build.VERSION.SDK_INT >= 11){
    		
    	 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
    	 
    	}else{
    		
    	 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	 
    	}
    	startActivity(intent);
    }
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		signOut();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

	    case android.R.id.home:
	         Intent login = new Intent(Register.this, Login.class);
	         startActivity(login);
	         break;

	    default:
	    	
		return super.onOptionsItemSelected(item);
		
		}
		return false;
	}

	private class PostData extends AsyncTask<String, String, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Posting Data ...");
            pDialog.setCancelable(false);
            pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String user = username.getText().toString();
			String pass = password.getText().toString();
			String mail = email.getText().toString();
			String user_name = name.getText().toString();
			String tel = telephone.getText().toString();
			String loc = location.getText().toString();
			String opening_bal = openingBal.getText().toString();
			
			
			UserFunctions userFunction = new UserFunctions();

			JSONObject json = userFunction.registerUser(user,pass,mail,tel,loc,user_name,opening_bal);
			
			try{
	
					int res = json.getInt(KEY_SUCCESS);
					
					if (res == 1) {
	                	Log.d("User Created!", json.toString());   
						Intent newIntent = new Intent(Register.this, Login.class);
						startActivity(newIntent);
	                //	finish();
	                	return json.getString(TAG_MESSAGE);
	                }else{
	                	Log.d("Login Failure!", json.getString(TAG_MESSAGE));
	                	return json.getString(TAG_MESSAGE);
	                	
	                }
					

			}catch (Exception e){
				
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			// TODO Auto-generated method stub
			pDialog.dismiss();
            if (file_url != null){
            	Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
            }
		
		}
		
	}

}
