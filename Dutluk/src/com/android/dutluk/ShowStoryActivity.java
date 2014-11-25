package com.android.dutluk;



import org.json.JSONException;
import org.json.JSONObject;


import android.os.Bundle;

import android.app.Activity;

import android.app.ProgressDialog;

import android.content.Intent;

import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class ShowStoryActivity extends Activity {

	ProgressDialog prgDialog;
	TextView errorMsg;
	

	ImageView imageV;
	TextView storyTV;


	String story_id = "" ;
	String mail = "";
	

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_story);
        
        errorMsg = (TextView)findViewById(R.id.errorShowStory);
     	// Instantiate Progress Dialog object
 		prgDialog = new ProgressDialog(this);
 		// Set Progress Dialog Text
	    prgDialog.setMessage("Please wait...");
	    // Set Cancelable as False
	    prgDialog.setCancelable(false);
         
   
     	imageV = (ImageView)findViewById(R.id.imageViewAddStory);
     	storyTV =  (TextView)findViewById(R.id.storyShowStory);
     
        
        Intent comingIntent = getIntent();
        mail = comingIntent.getStringExtra("mail");
        story_id = comingIntent.getStringExtra("story_id");
        
        RequestParams params = new RequestParams();
		params.put("mail",mail);
		params.put("story_id", story_id);
        invokeWSforGET(params);
    }
 
	public void invokeWSforGET(RequestParams params){
		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.43.17:9999/getStory",params ,new AsyncHttpResponseHandler() {
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
                        	 // Set Default Values for Edit View controls
                        	 setDefaultValues(obj);
                        	 // Display successfully registered message using Toast
                        	 Toast.makeText(getApplicationContext(), "You can successfully see story!", Toast.LENGTH_LONG).show();
                         } 
                         // Else display error message
                         else{
                        	 errorMsg.setText(obj.getString("error_msg"));
                        	 Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                         }
                 } catch (JSONException e) {
                    
                     Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                     e.printStackTrace();
                     
                 }
             }
             // When the response returned by REST has HTTP response code other than '200'
             @Override
             public void onFailure(int statusCode, Throwable error,
                 String content) {
                 // Hide Progress Dialog
                 prgDialog.hide();
                 // When HTTP response code is '404'
                 if(statusCode == 404){
                     Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                 } 
                 // When HTTP response code is '500'
                 else if(statusCode == 500){
                     Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                 } 
                 // When HTTP response code other than 404, 500
                 else{
                     Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                 }
             }
         });
	}
	// düzelt:)
	public void setDefaultValues(JSONObject obj) throws JSONException{
		storyTV.setText(obj.getString("story_id"));
		// image için de yaz bir þeyler
	}
	// düzelt:) 
	public void iRememberThatAction(View view){
//		Intent iRememberThatIntent = new Intent(getApplicationContext(),IRememberThatActivity.class);
//		iRememberThatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(iRememberThatIntent);
	}
	// düzelt:) 
	public void commentsAction(View view){
//		Intent commentsIntent = new Intent(getApplicationContext(),CommentsActivity.class);
//		commentsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(commentsIntent);
	}
	// düzelt:) 
	public void subscribeWriterAction(View view){
//		Intent subscribeWriterIntent = new Intent(getApplicationContext(),SubscribeWriterActivity.class);
//		subscribeWriterIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(subscribeWriterIntent);
	}
	// düzelt:) 
	public void subscribePlaceAction(View view){
//		Intent subscribePlaceIntent = new Intent(getApplicationContext(),SubscibePlaceActivity.class);
//		subscribePlaceIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(subscribePlaceIntent);
	}

}


