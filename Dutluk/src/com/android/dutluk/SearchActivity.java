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

public class SearchActivity extends Activity {

	ProgressDialog prgDialog;
	TextView errorMsg;

	EditText userET;
	EditText placeET;
	EditText storyET;
	String userTag = "";
	String placeTags = "";
	String storyTags = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
	
		errorMsg = (TextView)findViewById(R.id.errorSearch);
		// Instantiate Progress Dialog object
		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        
		userET = (EditText)findViewById(R.id.searchUserTags);
		placeET = (EditText)findViewById(R.id.searchPlaceTags);
		storyET = (EditText)findViewById(R.id.searchStoryTags);

     
	}
	
	public void searchAction(View view){
		
		userTag = userET.getText().toString();
		placeTags = placeET.getText().toString();
		storyTags = storyET.getText().toString();
		int check = 0 ;
		RequestParams params = new RequestParams();
	
		if(Utility.isNotNull(userTag)){
					
			params.put("userTag", userTag);
			invokeWSforUser(params);	
			check++;
		} 
		
		if(Utility.isNotNull(storyTags)){
			params.put("storyTags", storyTags);
			invokeWSforStory(params);
			check++;
					
		}
		if(Utility.isNotNull(placeTags)){
			params.put("placeTags", placeTags);
			invokeWSforPlace(params);
			check++;
		}
		if (check != 1){
			Toast.makeText(getApplicationContext(), "Please fill one field at once!", Toast.LENGTH_LONG).show();
		}
		
		
	}
	
	public void invokeWSforUser(RequestParams params){
		// Show Progress Dialog
		 prgDialog.show();
		 // Make RESTful webservice call using AsyncHttpClient object
		 AsyncHttpClient client = new AsyncHttpClient();		 
         client.post(Utility.SERVER_NAME + "GetUserList", params ,new AsyncHttpResponseHandler() {
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
                        	 Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                       
                         } 
                         // Else display error message
                         else{
                        	 errorMsg.setText(obj.getString("message"));
                        	 Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
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
	public void invokeWSforStory(RequestParams params){
		// Show Progress Dialog
		 prgDialog.show();
		 // Make RESTful webservice call using AsyncHttpClient object
		 AsyncHttpClient client = new AsyncHttpClient();		 
         client.post(Utility.SERVER_NAME + "GetStoryList", params ,new AsyncHttpResponseHandler() {
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
                        	 Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                       
                         } 
                         // Else display error message
                         else{
                        	 errorMsg.setText(obj.getString("message"));
                        	 Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
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
	public void invokeWSforPlace(RequestParams params){
		// Show Progress Dialog
		 prgDialog.show();
		 // Make RESTful webservice call using AsyncHttpClient object
		 AsyncHttpClient client = new AsyncHttpClient();		 
         client.post(Utility.SERVER_NAME + "GetPlaceList", params ,new AsyncHttpResponseHandler() {
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
                        	 Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                       
                         } 
                         // Else display error message
                         else{
                        	 errorMsg.setText(obj.getString("message"));
                        	 Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
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

	
	
}
