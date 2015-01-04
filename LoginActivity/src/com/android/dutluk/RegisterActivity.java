package com.android.dutluk;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RegisterActivity extends Activity {
	ActionBar actionBar;
	ProgressDialog prgDialog;


	EditText nameET;
	EditText emailET;
	EditText pwdET;
	
	
	String name= "";
	String email= "";
	String password = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
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
        
		nameET = (EditText)findViewById(R.id.nameRegister);		
		emailET = (EditText)findViewById(R.id.emailRegister);	
		pwdET = (EditText)findViewById(R.id.passwordRegister);

	}

	public void registerUser(View view){
		// Get NAme ET control value
		name = nameET.getText().toString();
		// Get Email ET control value
		email = emailET.getText().toString();
		// Get Password ET control value
		password = pwdET.getText().toString();
		// Instantiate Http Request Param Object
		RequestParams params = new RequestParams();
		// When Name Edit View, Email Edit View and Password Edit View have values other than Null
		if(Utility.isNotNull(name) && Utility.isNotNull(email) && Utility.isNotNull(password)){
			// When Email entered is Valid
			if(Utility.validateEmail(email)){
				// Put Http parameter name with value of Name Edit View control
				params.put("name", name);
				// Put Http parameter username with value of Email Edit View control
				params.put("email", email);
				// Put Http parameter password with value of Password Edit View control
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
        client.get(Utility.SERVER_NAME + "Register",params ,new AsyncHttpResponseHandler() {
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
                        	 // Set Default Values for Edit View controls
                        	 setDefaultValues();
                        	 // Display successfully registered message using Toast
                        	 Toast.makeText(getApplicationContext(), "You are successfully registered!", Toast.LENGTH_LONG).show();
                        	 navigatetoLoginActivity();
                         } 
                         // Else display error message
                         else{
                        	
                        	 Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                         }
                 } catch (JSONException e) {
                  
                     Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
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
                     Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                 } 
                 // When Http response code is '500'
                 else if(statusCode == 500){
                     Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                 } 
                 // When Http response code other than 404, 500
                 else{
                     Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                 }
             }
         });
	}
	
	public void setDefaultValues(){
		nameET.setText("");
		emailET.setText("");
		pwdET.setText("");
	}
	
	public void navigatetoLoginActivity(){
		Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
		Utility.myUserName = email;
		Utility.IDFromName(email);
		Utility.myUserID = Utility.userIDFromName;
		// Clears History of Activity
		loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(loginIntent);
	}
	
	
	
	
}
