package com.android.dutluk;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LoginActivity extends Activity {

	ProgressDialog prgDialog;
	TextView errorMsg;

	EditText emailET;
	EditText pwdET;

	String mail = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	
		errorMsg = (TextView)findViewById(R.id.errorLogin);
		// Instantiate Progress Dialog object
		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        
		emailET = (EditText)findViewById(R.id.emailLogin);
		pwdET = (EditText)findViewById(R.id.passwordLogin);

        Intent registerIntent = getIntent();
        mail = registerIntent.getStringExtra("mail");
        emailET.setText(mail);
	}
	
	public void loginUser(View view){
		// Get Email Edit View Value
		String email = emailET.getText().toString();
		// Get Password Edit View Value
		String password = pwdET.getText().toString();
		// Instantiate Http Request Param Object
		RequestParams params = new RequestParams();
		// When Email Edit View and Password Edit View have values other than Null
		if(Utility.isNotNull(email) && Utility.isNotNull(password)){
			// When Email entered is Valid
			if(Utility.validateEmail(email)){
				// Put Http parameter username with value of Email Edit View control
				params.put("mail", email);
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
         client.get("http://192.168.43.17:9999/useraccount/login/dologin",params ,new AsyncHttpResponseHandler() {
        	 // When the response returned by REST has Http response code '200'
             @Override
             public void onSuccess(String response) {
            	 // Hide Progress Dialog
            	 prgDialog.hide();
                 try {
                	 	 // JSON Object
                         JSONObject obj = new JSONObject(response);
                         // When the JSON response has status boolean value assigned with true
                         if(obj.getBoolean("status")){
                        	 Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        	 // Navigate to Home screen
                        	 navigatetoProfileActivity(obj.getString("mail"));
                         } 
                         // Else display error message
                         else{
                        	 errorMsg.setText(obj.getString("error_msg"));
                        	 Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                         }
                 } catch (JSONException e) {
                     // TODO Auto-generated catch block
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

	public void navigatetoProfileActivity(String mail){
		Intent profileIntent = new Intent(getApplicationContext(),ProfileActivity.class);
		Bundle b = new Bundle();
		b.putString("mail",mail);
		profileIntent.putExtras(b);
		profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(profileIntent);
	}
	
	public void navigatetoRegisterActivity(View view){
		Intent loginIntent = new Intent(getApplicationContext(),RegisterActivity.class);
		loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(loginIntent);
	}
	
}
