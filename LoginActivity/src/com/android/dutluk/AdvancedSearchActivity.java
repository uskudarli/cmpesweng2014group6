package com.android.dutluk;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AdvancedSearchActivity extends Activity {

	ProgressDialog prgDialog;
	TextView errorMsg;

	EditText userET;
	EditText placeET;
	EditText storyET;
	String userTag = "";
	String placeTags = "";
	String storyTags = "";
	
	 private AutoCompleteTextView searchUserTags;
     private MultiAutoCompleteTextView searchPlaceTags;
     private MultiAutoCompleteTextView searchStoryTags;
     String item[]={
    		   "January", "February", "March", "April",
    		   "May", "June", "July", "August",
    		   "September", "October", "November", "December"
    		 };
	
	@SuppressLint("CutPasteId")
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
		searchUserTags = (AutoCompleteTextView) findViewById(R.id.searchUserTags);
		searchPlaceTags = (MultiAutoCompleteTextView) findViewById(R.id.searchPlaceTags);
		searchStoryTags = (MultiAutoCompleteTextView) findViewById(R.id.searchStoryTags);
		
		 ArrayAdapter<String> userTagAdapter = new ArrayAdapter<String>(this,
                 android.R.layout.simple_dropdown_item_1line,item);
		 searchUserTags.setAdapter(userTagAdapter);

		 ArrayAdapter<String> placeTagsAdapter = new ArrayAdapter<String>(this,
		                 android.R.layout.simple_dropdown_item_1line, item);
		
		 searchPlaceTags.setAdapter(placeTagsAdapter);
		 searchPlaceTags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		 
		 ArrayAdapter<String> storyTagsAdapter = new ArrayAdapter<String>(this,
                 android.R.layout.simple_dropdown_item_1line, item);

		 searchStoryTags.setAdapter(storyTagsAdapter);
		 searchStoryTags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
 
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
                        	 Toast.makeText(getApplicationContext(), "You are successfully get user list!", Toast.LENGTH_LONG).show();
                        	 JSONArray jsonarray = new JSONArray(obj.toString());

                        	int length = jsonarray.length();
                        	Utility.userList = new String [length];
             			    for(int i=0; i<length; i++){
             			        JSONObject obj2 = jsonarray.getJSONObject(i);

             			        String name = obj2.getString("content");
             			        Utility.userList[i] = name;

             			    }   
                        
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
                        	 Toast.makeText(getApplicationContext(), "You are successfully get story list!", Toast.LENGTH_LONG).show();
                        	 
                        	 JSONArray jsonarray = new JSONArray(obj.toString());

                         	int length = jsonarray.length();
                         	Utility.storyTagsList= new String [length];
              			    for(int i=0; i<length; i++){
              			        JSONObject obj2 = jsonarray.getJSONObject(i);

              			        String name = obj2.getString("content");
              			        Utility.storyTagsList[i] = name;

              			    }  
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
                        	 Toast.makeText(getApplicationContext(), "You are successfully get place list!", Toast.LENGTH_LONG).show();
                        	 JSONArray jsonarray = new JSONArray(obj.toString());

                          	int length = jsonarray.length();
                          	Utility.placeTagsList= new String [length];
               			    for(int i=0; i<length; i++){
               			        JSONObject obj2 = jsonarray.getJSONObject(i);

               			        String name = obj2.getString("content");
               			        Utility.placeTagsList[i] = name;

               			    }  
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
