package com.android.dutluk;



import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;



import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;

import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class ShowStoryActivity extends Activity {

	ProgressDialog prgDialog;
	TextView errorMsg;

	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_DURATION = "duration";

	ActionBar actionBar;
	MenuInflater inflater;
	Menu m;

	ListView lv;
	ArrayList<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>();

	ProgressDialog dialog;
	Boolean isInput = true;

	String searchedTerm;


	ImageView imageV;
	TextView storyTV;


	String story_id = "" ;

	
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
       
        story_id = comingIntent.getStringExtra("story_id");
        
        RequestParams params = new RequestParams();
		params.put("mail",Utility.userName);
		params.put("story_id", story_id);
        invokeWSforGetStory(params);
        
		lv = (ListView) findViewById(R.id.listView1);
		
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(16, 188, 201)));
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		new MyTask().execute();
        
        
    }
 
	public void invokeWSforGetStory(RequestParams params){
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
		 final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		    final EditText input = new EditText(this);
		    alert.setTitle("Add Comment"); 
		    alert.setView(input);
		    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		            String value = input.getText().toString().trim();
		            
		            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
		        }
		    });

		    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		            dialog.cancel();
		        }
		    });
		    alert.show();   

	}
	private void readData() {
		

		TimelineAdapter adapter = new TimelineAdapter(this, alist);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				try {
					String other_user_id = 0 + "";
					startOtherProfileActivity(other_user_id);

				} catch (Exception e) {
					
					String data = e.getMessage();
				}

			}
		});
		lv.setLongClickable(true);
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
						return isInput;
		
			}
		});

	}
	private class MyTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
		
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
		
			super.onPostExecute(result);
			readData();
		}

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient httpclient = new DefaultHttpClient();
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("email", Utility.userName));
			String url = Utility.SERVER_NAME + "GetStory?" + URLEncodedUtils.format(pairs, "utf-8");
			HttpGet httpget = new HttpGet(url);
			//Log.w("submit", "aa" + url);
			try {
				HttpResponse getResponse = httpclient.execute(httpget);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
				getResponse.getEntity().getContent()));
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
				result.append(line);
				}

				//Log.w("submit", result.toString());
				JSONArray jsonarray = new JSONArray(result.toString());


			    for(int i=0; i<jsonarray.length(); i++){
			        JSONObject obj = jsonarray.getJSONObject(i);

			        String name = obj.getString("content");
			        Log.w("submit", name);
			        HashMap<String, String> map = new HashMap<String, String>();
					map.put(KEY_ID, "a" + i);
					map.put(KEY_TITLE, name);
					map.put(KEY_DURATION, "info"+i);
					alist.add(map);
			    }   
			


				} catch (ClientProtocolException e) {
			
				e.printStackTrace();
				} catch (IOException e) {
				
				e.printStackTrace();
				} catch (JSONException e) {
			
				e.printStackTrace();
				}
				return null;

		}

	}
	public void  startOtherProfileActivity(String other_user_id) {
		
		Intent otherProfileIntent = new Intent(getApplicationContext(),OtherUserProfileActivity.class);
		Bundle b = new Bundle();
		b.putString("other_user_id",other_user_id);
		otherProfileIntent.putExtras(b);
		otherProfileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(otherProfileIntent);
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
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.addStory:
			navigateToAddStoryActivity();
			break;
		case R.id.map:
			navigateToHomeMapActivity();
			break;
		case R.id.ownProfile:
			navigateToProfileActivity();
			break;
		case R.id.advancedSearch:
			navigateToAdvancedSearchActivity();
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	public void navigateToSearchActivity() {
		Intent searchIntent = new Intent(getApplicationContext(),AdvancedSearchActivity.class);
		searchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(searchIntent);
		
	}
	public void navigateToAddStoryActivity() {
		Intent addStoryIntent = new Intent(getApplicationContext(),AddStoryActivity.class);
		addStoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(addStoryIntent);
		
	}
	public void navigateToHomeMapActivity() {
		Intent homeMapIntent = new Intent(getApplicationContext(),HomeMapActivity.class);
		homeMapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(homeMapIntent);
		
	}
	
	public void navigateToProfileActivity() {
		Intent profileIntent = new Intent(getApplicationContext(),ProfileActivity.class);
		profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(profileIntent);
		
	}
	public void navigateToAdvancedSearchActivity() {
		Intent searchIntent = new Intent(getApplicationContext(),AdvancedSearchActivity.class);
		searchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(searchIntent);
		
	}


}


