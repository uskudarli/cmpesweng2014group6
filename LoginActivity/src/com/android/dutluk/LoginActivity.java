package com.android.dutluk;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LoginActivity extends Activity {
	ActionBar actionBar;
	ProgressDialog prgDialog;


	EditText emailET;
	EditText pwdET;

	String mail = "";
	String password = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		if(Utility.isReseted){
			Utility.isReseted = false;
			showPopUp();
		}
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 0, 0)));
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		// Instantiate Progress Dialog object
		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        
		emailET = (EditText)findViewById(R.id.emailLogin);
		pwdET = (EditText)findViewById(R.id.passwordLogin);


        if (!Utility.myUserName.equals("")){
        	emailET.setText(Utility.myUserName);
        	mail = Utility.myUserName;
        }
      
	}
	public void showPopUp() {
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);	
		alert.setTitle("The new password is sent to your e-mail address."); 
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
				return;
			}
		});
		alert.show();   
	}
	public void loginUser(View view){
		// Get Email Edit View Value
		
		if(Utility.myUserName.equals("")){
			mail = emailET.getText().toString();
			Utility.myUserName = mail;
		}
		
		// Get Password Edit View Value
		password = pwdET.getText().toString();
		// Instantiate Http Request Param Object
		RequestParams params = new RequestParams();
		// When Email Edit View and Password Edit View have values other than Null
		if(Utility.isNotNull(mail) && Utility.isNotNull(password)){
			// When Email entered is Valid
			if(Utility.validateEmail(mail)){
				// Put Http parameter username with value of Email Edit View control
				params.put("mail", mail);
				// Put Http parameter password with value of Password Edit Value control
				params.put("password", password);
				// Invoke RESTful Web Service with Http parameters
				invokeWS(params);
			} 
			// When Email is invalid
			else{
				Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
			}
		} 
		// When any of the Edit View control left blank
		else{
			Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
		}
		
	}

	
	public void invokeWS(RequestParams params){
		// Show Progress Dialog
		 prgDialog.show();
		 // Make RESTful webservice call using AsyncHttpClient object
		 AsyncHttpClient client = new AsyncHttpClient();		 
         client.post(Utility.SERVER_NAME + "Login", params ,new AsyncHttpResponseHandler() {
        	 // When the response returned by REST has Http response code '200'
             @Override
             public void onSuccess(String response) {
            	 // Hide Progress Dialog
            	 prgDialog.hide();
                 try {
                	 	 // JSON Object
                         JSONObject obj = new JSONObject(response);
                         // When the JSON response has status boolean value assigned with true
                         if(obj.getBoolean("result")){
                        	 Log.e("invokeWSLogin.success", "You are successfully logged in!");
                        	 // Navigate to Home screen
                        	 navigatetoHomeMapActivity();
                         } 
                         // Else display error message
                         else{
                        
                        	 Log.e("invokeWSLogin.error" , obj.getString("message"));
                         }
                 } catch (JSONException e) {
                     
                	 Log.e("invokeWSLogin.json" ,"Error Occured [Server's JSON response might be invalid]!");
                     e.printStackTrace();
                     
                 }
             }
             // When the response returned by REST has Http response code other than '200'
             @Override
             public void onFailure(int statusCode, Throwable error,
                 String content) {
                 // Hide Progress Dialog 
                 prgDialog.hide();
                 // When Http response code is '404'
                 if(statusCode == 404){
                	 Log.e("invokeWSLogin.404" , "Requested resource not found");
                 } 
                 // When Http response code is '500'
                 else if(statusCode == 500){
                	 Log.e("invokeWSLogin.500" , "Something went wrong at server end");
                 } 
                 // When Http response code other than 404, 500
                 else{
                     Log.e("invokeWSLogin.other" ,"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
                 }
             }
         });
	}

	public void navigatetoHomeMapActivity(){
		Intent homeMapIntent = new Intent(getApplicationContext(),HomeMapActivity.class);
		homeMapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(homeMapIntent);
	}
	
	public void navigatetoRegisterActivity(View view){
		Intent loginIntent = new Intent(getApplicationContext(),RegisterActivity.class);
		loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(loginIntent);
	}
	public void navigateToForgotPasswordActivity(View view){
		Intent forgotPwdIntent = new Intent(getApplicationContext(),ForgotPwdActivity.class);
		forgotPwdIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(forgotPwdIntent);
	}
	
}
