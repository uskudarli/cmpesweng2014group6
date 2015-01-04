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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;



import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


import android.widget.ListView;
import android.widget.TextView;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class ShowStoryActivity extends Activity implements OnItemSelectedListener{

	ProgressDialog prgDialog;


	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_INFO = "info";

	ActionBar actionBar;
	MenuInflater inflater;
	Menu m;

	ListView lv;
	ArrayList<HashMap<String, String>> commentList = new ArrayList<HashMap<String, String>>();

	ProgressDialog dialog;
	Boolean isInput = true;

	String searchedTerm;


	ImageView imageV;
	TextView storyTV;
	Button placeButton;
	TextView themeTV;
	TextView rateTV;
	Button ownerButton;
	Button rememberThatButton;
	Button commentsButton;
	Button subscribeWriterButton;
	Button subscribePlaceButton;

	String story_id = "" ;
	String type = "";
	String type_send = "";
	String owner = "";
	String storyOwnerID = "";
	String place_id = "";
	String placeName = "";
	boolean isFollowingPlace = false;
	boolean isFollowingUser = false;
	boolean isRemembered = false;
	boolean isRated = false;


	private Spinner rateSpinner;
	ArrayList<String> rateList = new ArrayList<String>();
	String givenRate = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_story);

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


		imageV = (ImageView)findViewById(R.id.imageViewAddStory);
		storyTV =  (TextView)findViewById(R.id.storyShowStory);
		placeButton =  (Button)findViewById(R.id.placeShowStory);
		themeTV =  (TextView)findViewById(R.id.themeShowStory);
		rateTV =  (TextView)findViewById(R.id.rateShowStory);
		ownerButton = (Button) findViewById(R.id.ownerShowStory);
		rememberThatButton = (Button) findViewById(R.id.btnIRememberThat);
		commentsButton = (Button) findViewById(R.id.btnComments);
		subscribeWriterButton = (Button) findViewById(R.id.btnSubscribeWriter);
		subscribePlaceButton = (Button) findViewById(R.id.btnSubscribePlace);

		Intent comingIntent = getIntent();

		type = comingIntent.getStringExtra("type");


		if(type.equals("myStories")){		
			type_send = ""+0;
		}
		if(type.equals("placesStories")){  
			type_send = comingIntent.getStringExtra("placeID");
		}
		if(type.equals("subsStories")){
			type_send = ""+1;
		}
		story_id = comingIntent.getStringExtra("story_id");
		place_id = comingIntent.getStringExtra("place_id");
		placeName = comingIntent.getStringExtra("placeName");
		RequestParams params = new RequestParams();
		params.put("storyId", story_id);
		invokeWSforGetStory(params);



		owner = comingIntent.getStringExtra("owner");
		Utility.IDFromName(owner);
		storyOwnerID = Utility.userIDFromName;



		checkForSubscribePlace();

		subscribePlaceButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				subscribePlaceAction();
			}
		});


		if(owner.equals(Utility.myUserName)){
			Log.e("storyOwner-Utility.myUserName",owner+"-"+ Utility.myUserName);
			subscribeWriterButton.setVisibility(View.INVISIBLE);
			rememberThatButton.setVisibility(View.INVISIBLE);

		}
		else{
			checkForSubscribeWriter();
			subscribeWriterButton.setOnClickListener(new OnClickListener() {		
				@Override
				public void onClick(View v) {			
					subscribeWriterAction();
				}

			});

			checkForRememberThat();
			rememberThatButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					rememberThatAction();
				}

			});

		}

		rateSpinner = (Spinner)findViewById(R.id.spinnerRate);
		ArrayAdapter<String>adapter = new ArrayAdapter<String>(ShowStoryActivity.this,
				android.R.layout.simple_spinner_item,rateList);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		rateList.add("Rate Story");
		rateList.add("1");
		rateList.add("2");
		rateList.add("3");
		rateList.add("4");
		rateList.add("5");


		rateSpinner.setAdapter(adapter);
		rateSpinner.setSelection(0);
		rateSpinner.setOnItemSelectedListener(this);

		lv = (ListView) findViewById(R.id.commentList);


		new MyTask().execute();


	}
	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

		if(position != 0) {
			givenRate = rateList.get(position);
			Log.e("GIVENRATE", "a " + givenRate );
			checkForRate();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
	public void getThemeName(String theme_id){
		RequestParams params = new RequestParams();
		params.put("themeId", theme_id);

		Log.e("themeId", theme_id +  " aaaaaaaa " );

		//http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/GetThemeName?themeId=2(GET)

		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Utility.SERVER_NAME+ "GetThemeName?",params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					// JSON Object
					JSONObject obj = new JSONObject(response);
					// When the JSON response has status boolean value assigned with true

					String temp_theme = obj.getString("Name");
					Log.e("getThemeName.success", "a" + temp_theme);
					themeTV.setText(temp_theme);





				} catch (JSONException e) {

					Log.e("getThemeName.json", "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("getThemeName.404", "Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){
					Log.e("getThemeName.500", "Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("getThemeName.other", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}
	public void checkForRate(){
		RequestParams params = new RequestParams();
		params.put("StoryID", story_id);
		params.put("mail", Utility.myUserName);
		params.put("rate",givenRate);
		Log.e("storyID-mail", story_id +  " aaaaaaaa " + Utility.myUserName + " aaa " + givenRate);

		//http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/RateStory?mail=burcin@burcin.com&StoryID=101&rate=5

		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(Utility.SERVER_NAME+ "RateStory?",params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					// JSON Object
					JSONObject obj = new JSONObject(response);
					// When the JSON response has status boolean value assigned with true

					isRated = obj.getBoolean("result");
					Log.e("checkForRate.success", "a" + isRated);

					changeRate();




				} catch (JSONException e) {

					Log.e("checkForRate.json", "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("checkForRate.404", "Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){
					Log.e("checkForRate.500", "Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("checkForRate.other", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}
	public void changeRate(){
		//http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/GetStoryRate?storyId=101(GET)
		RequestParams params = new RequestParams();
		params.put("storyId", story_id);
		Log.e("storyID", story_id +  " aaaaaaaa ");


		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(Utility.SERVER_NAME+ "GetStoryRate?",params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					// JSON Object
					JSONObject obj = new JSONObject(response);
					// When the JSON response has status boolean value assigned with true
					String temp_rate = obj.getString("message");
					rateTV.setText(temp_rate);
					Log.e("changeRate.success", "a" + temp_rate);



				} catch (JSONException e) {

					Log.e("changeRate.json", "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("changeRate.404", "Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){
					Log.e("changeRate.500", "Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("changeRate.other", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});

	}
	public void checkForRememberThat(){
		RequestParams params = new RequestParams();
		params.put("storyId", story_id);
		params.put("userId", Utility.myUserID);
		Log.e("storyID-myUserID", story_id +  " aaaaaaaa " + Utility.myUserID);

		//http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/IsRemembered?storyId=101&userId=122
		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(Utility.SERVER_NAME+ "IsRemembered?",params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					// JSON Object
					JSONObject obj = new JSONObject(response);
					// When the JSON response has status boolean value assigned with true

					isRemembered = obj.getBoolean("result");
					Log.e("checkForRememberThat.success", "a" + isRemembered);
					changeRemember();


				} catch (JSONException e) {

					Log.e("checkForRememberThat.json", "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("checkForRememberThat.404", "Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){
					Log.e("checkForRememberThat.500", "Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("checkForRememberThat.other", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}
	public void changeRemember(){
		if(isRemembered)
			rememberThatButton.setText("Not Remember That");
		else 
			rememberThatButton.setText("Remember That");
	}
	public void rememberThatAction(){

		RequestParams params = new RequestParams();
		params.put("email", Utility.myUserName);
		params.put("StoryId", story_id);

		if(isRemembered){
			//http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/RememberStory?email=burcin@burcin.com&func=unremember&StoryId=102(POST)
			params.put("func", "unremember");
			Log.e("rememberThatAction", "unremember");
		}
		else {
			//http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/RememberStory?email=burcin@burcin.com&func=remember&StoryId=102(POST)
			params.put("func", "remember");	
			Log.e("rememberThatAction", "remember");
		}


		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(Utility.SERVER_NAME + "RememberStory?",params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {

				// Hide Progress Dialog
				prgDialog.hide();
				try {
					// JSON Object
					JSONObject obj = new JSONObject(response);
					// When the JSON response has status boolean value assigned with true

					String msg = obj.getString("message");
					Log.e("rememberThatAction.success", msg);
					isRemembered = !isRemembered;
					changeRemember();


				} catch (JSONException e) {

					Log.e("rememberThatAction.json", "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("rememberThatAction.404", "Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){

					Log.e("rememberThatAction.500", "Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("rememberThatAction.other", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}
	public void checkForSubscribeWriter(){
		RequestParams params = new RequestParams();
		params.put("followerId", Utility.myUserID);
		params.put("followedId", storyOwnerID);
		Log.e("myUserID-storyOwnerID", Utility.myUserID +  " aaaaaaaa " +  storyOwnerID);
		//http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/isFollowingUser?followerId=112&followedId=122
		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Utility.SERVER_NAME+ "isFollowingUser?",params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					// JSON Object
					JSONObject obj = new JSONObject(response);
					// When the JSON response has status boolean value assigned with true

					isFollowingUser = obj.getBoolean("result");
					Log.e("checkForSubscribeWriter.success", "a" + isFollowingUser);
					changeFollowingWriter();


				} catch (JSONException e) {

					Log.e("checkForSubscribeWriter.json", "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("checkForSubscribeWriter.404", "Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){
					Log.e("checkForSubscribeWriter.500", "Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("checkForSubscribeWriter.other", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}
	public void changeFollowingWriter(){
		if(isFollowingUser)
			subscribeWriterButton.setText("Unsubscribe Writer");
		else 
			subscribeWriterButton.setText("Subscribe Writer");
	}
	public void subscribeWriterAction(){
		RequestParams params = new RequestParams();
		params.put("email", Utility.myUserName);
		params.put("userId", storyOwnerID);

		if(isFollowingUser){
			//http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/Subscribe?email=burcin@burcin.com&func=unsubscribe&userId=101(POST)
			params.put("func", "unsubscribe");
			Log.e("subscribeWriterAction", "unsubscribeUser");
		}
		else {
			//http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/Subscribe?email=burcin@burcin.com&func=subscribe&userId=101(POST)
			params.put("func", "subscribe");	
			Log.e("subscribeWriterAction", "subscribeUser");
		}


		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(Utility.SERVER_NAME + "Subscribe?",params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {

				// Hide Progress Dialog
				prgDialog.hide();
				try {
					// JSON Object
					JSONObject obj = new JSONObject(response);
					// When the JSON response has status boolean value assigned with true

					String msg = obj.getString("message");
					Log.e("subscribeWriterAction.success", msg);
					isFollowingUser = !isFollowingUser;
					changeFollowingWriter();

				} catch (JSONException e) {

					Log.e("subscribeWriterAction.json", "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("subscribeWriterAction.404", "Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){
					Log.e("subscribeWriterAction.500", "Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("subscribeWriterAction.other", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}

	public void checkForSubscribePlace(){
		RequestParams params = new RequestParams();
		params.put("userId", Utility.myUserID);
		params.put("placeId", place_id);
		Log.e("myUserID-placeId", Utility.myUserID +  " aaaaaaaa " +  place_id);
		//http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/isFollowingPlace?userId=112&placeId=122
		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Utility.SERVER_NAME+ "isFollowingPlace?",params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					// JSON Object
					JSONObject obj = new JSONObject(response);
					// When the JSON response has status boolean value assigned with true

					isFollowingPlace = obj.getBoolean("result");
					Log.e("checkForSubscribePlace.success", "a" + isFollowingPlace);
					changeFollowingPlace();


				} catch (JSONException e) {

					Log.e("checkForSubscribePlace.json", "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("checkForSubscribePlace.404", "Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){
					Log.e("checkForSubscribePlace.500", "Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("checkForSubscribePlace.other", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}
	public void changeFollowingPlace(){
		if(isFollowingPlace)
			subscribePlaceButton.setText("Unsubscribe Place");
		else 
			subscribePlaceButton.setText("Subscribe Place");
	}
	public void subscribePlaceAction(){
		RequestParams params = new RequestParams();
		params.put("email", Utility.myUserName);
		params.put("placeId",place_id);

		if(isFollowingPlace){
			//http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/Subscribe?email=burcin@burcin.com&func=unsubscribePlace&placeId=101(post)		
			params.put("func", "unsubscribePlace");
			Log.e("subscribePlaceAction", "unsubscribePlace");
		}
		else {
			//http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/Subscribe?email=burcin@burcin.com&func=subscribePlace&placeId=101(POST)
			params.put("func", "subscribePlace");	
			Log.e("subscribePlaceAction", "subscribePlace");
		}


		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(Utility.SERVER_NAME+ "Subscribe?",params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {

				// Hide Progress Dialog
				prgDialog.hide();
				try {
					// JSON Object
					JSONObject obj = new JSONObject(response);
					// When the JSON response has status boolean value assigned with true

					String msg = obj.getString("message");
					Log.e("subscribePlaceAction.success", msg);
					isFollowingPlace = !isFollowingPlace;
					changeFollowingPlace();

				} catch (JSONException e) {

					Log.e("subscribePlaceAction.json", "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("subscribePlaceAction.404", "Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){
					Log.e("subscribePlaceAction.500", "Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("subscribePlaceAction.other", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}




	public void invokeWSforGetStory(RequestParams params){
		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Utility.SERVER_NAME+ "GetOneStory?",params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					// JSON Object
					JSONObject obj = new JSONObject(response);
					// When the JSON response has status boolean value assigned with true

					// Set Default Values for Edit View controls
					setDefaultValues(obj);
					// Display successfully registered message using Toast
					Log.e("invokeWSforGetStory.success", "You can successfully see story!");

				} catch (JSONException e) {

					Log.e("invokeWSforGetStory.json", "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("invokeWSforGetStory.404",  "Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){
					Log.e("invokeWSforGetStory.500","Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("invokeWSforGetStory.other", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}
	// düzelt:)
	public void setDefaultValues(JSONObject obj) throws JSONException{
		ownerButton.setText(owner);
		placeButton.setText(placeName);
		storyTV.setText(obj.getString("content"));
		getThemeName(obj.getString("themeId"));

		rateTV.setText(obj.getString("avgRate"));

		// image için de yaz bir þeyler
	}



	public void commentsAction(View view){
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		alert.setTitle("Add Comment"); 
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString().trim();
				RequestParams params = new RequestParams();
				params.put("mail", Utility.myUserName);
				params.put("storyId", story_id);
				params.put("comment", value);
				invokeWSforAddComment(params);
				//Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		alert.show();   

	}
	public void seeOwnerProfile(View view){ 
		if(!owner.equals(Utility.myUserName)){
			startOtherProfileActivity(storyOwnerID);
		}
	}
	public void seePlaceProfile(View view){ 
		navigateToTimelineActivityForPlaces(place_id);

	}
	public void navigateToTimelineActivityForPlaces(String placeID){
		Intent timelineIntent = new Intent(getApplicationContext(),TimelineActivity.class);
		Bundle b = new Bundle();
		b.putString("type","placesStories");
		b.putString("placeID",placeID);
		timelineIntent.putExtras(b);
		timelineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(timelineIntent);
	}
	public void invokeWSforAddComment(RequestParams params){
		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(Utility.SERVER_NAME+ "AddComment?",params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					// JSON Object
					JSONObject obj = new JSONObject(response);
					// When the JSON response has status boolean value assigned with true
					commentList.clear();
					new MyTask().execute();
					// Display successfully registered message using Toast
					Log.e("invokeWSforAddComment.success", "You can successfully add comment!");

				} catch (JSONException e) {

					Log.e("invokeWSforAddComment.json", "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("invokeWSforAddComment.404", "Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){
					Log.e("invokeWSforAddComment.500", "Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("invokeWSforAddComment.other", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}


	private void readComments() {



		if(commentList.size() > 1) {
			LayoutParams lp = (LayoutParams) lv.getLayoutParams();
			lp.height = 120;
			lv.setLayoutParams(lp);
		}
		TimelineAdapter adapter = new TimelineAdapter(this, commentList, "comment");

		lv.setAdapter(adapter);
		lv.setOnTouchListener(new ListView.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					// Disallow ScrollView to intercept touch events.
					v.getParent().requestDisallowInterceptTouchEvent(true);
					break;

				case MotionEvent.ACTION_UP:
					// Allow ScrollView to intercept touch events.
					v.getParent().requestDisallowInterceptTouchEvent(false);
					break;
				}

				// Handle ListView touch events.
				v.onTouchEvent(event);
				return true;
			}


		});
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				try {

					//startOtherProfileActivity(storyOwnerID);

				} catch (Exception e) {

					e.printStackTrace();
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
			Log.e("COMMENT LIST IN POST", commentList.toString());
			readComments();
		}

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient httpclient = new DefaultHttpClient();
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("storyId", story_id));
			pairs.add(new BasicNameValuePair("userId","0"));  // for getting all comments of a story
			String url = Utility.SERVER_NAME + "GetComment?" + URLEncodedUtils.format(pairs, "utf-8");
			HttpGet httpget = new HttpGet(url);

			try {
				HttpResponse getResponse = httpclient.execute(httpget);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						getResponse.getEntity().getContent()));
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}

				Log.e("COMMENTS ON GET", result.toString());
				JSONArray jsonarray = new JSONArray(result.toString());


				for(int i=0; i<jsonarray.length(); i++){
					JSONObject obj = jsonarray.getJSONObject(i);


					String content = obj.getString("Comment");				
					String info = obj.getString("UserMail");
					int id = obj.getInt("StoryID");
					HashMap<String, String> map = new HashMap<String, String>();

					map.put(KEY_ID, "" + id);
					map.put(KEY_TITLE, content);
					map.put(KEY_INFO, info);
					commentList.add(map);


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



	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		inflater = getMenuInflater();
		inflater.inflate(R.menu.showstory_menu, menu);

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


