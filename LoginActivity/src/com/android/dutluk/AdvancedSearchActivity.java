package com.android.dutluk;



import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AdvancedSearchActivity extends Activity {
	ActionBar actionBar;
	ProgressDialog prgDialog;

	Boolean isInput = true;
	EditText userET;
	EditText placeET;
	EditText storyET;
	String userTag = "";
	String placeTag = "";
	String storyTag = "";
	String type = "";
	String searched = "";
	ArrayList<Integer> storyIDs = new ArrayList<Integer>();
	ArrayList<Integer> placeIDList = new ArrayList<Integer>();
	ArrayList<String> placeNameList = new ArrayList<String>();
	ArrayList<String> ownerList = new ArrayList<String>();

	ArrayList<Integer> placeIDs = new ArrayList<Integer>();

	ArrayList<HashMap<String, String>> searchList = new ArrayList<HashMap<String, String>>();
	ArrayList<String> searchItems = new ArrayList<String>();

	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_INFO = "info";

	ListView lv;
	MenuInflater inflater;
	Menu m;

	String [] items = new String[10];
	String choice = "false";
	@SuppressLint("CutPasteId")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(0,0,0)));
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);

		// Instantiate Progress Dialog object
		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
		prgDialog.setMessage("Please wait...");
		// Set Cancelable as False
		prgDialog.setCancelable(false);

		userET = (EditText)findViewById(R.id.searchUserTags);
		placeET = (EditText)findViewById(R.id.searchPlaceTags);
		storyET = (EditText)findViewById(R.id.searchStoryTags);

		lv = (ListView) findViewById(R.id.listViewSearch);



	}

	public void searchAction(View view){

		userTag = userET.getText().toString();
		placeTag = placeET.getText().toString();
		storyTag = storyET.getText().toString();
		if(Utility.isNotNull(userTag) || Utility.isNotNull(storyTag) || Utility.isNotNull(placeTag))
			callDialog();



	}
	public void callDialog() {

		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final TextView input = new TextView(this);
		alert.setMessage("Do you want to make a semantic search? This operation might take a while.");
		alert.setTitle("Semantic Search"); 
		alert.setView(input);
		alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				choice = "true";
			}
		});

		alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				choice = "false";
				dialog.dismiss();
			}
		});
		alert.show();
		makeRequest();

	}
	public void makeRequest() {
		int check = 0 ;
		RequestParams params = new RequestParams();
		

		if(Utility.isNotNull(userTag)){
			params.put("isSemantic",  "false");
			//http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/Search?term=burcin&func=user&isSemantic=false(POST)
			params.put("func", "user");
			params.put("term", userTag);
			searched = userTag;
			invokeWSforUser(params);	
			check++;
			userET.setText("");
			userTag = "";
		} 

		if(Utility.isNotNull(storyTag)){
			params.put("isSemantic",  choice);
			params.put("func", "story");
			params.put("term", storyTag);
			searched = storyTag;
			invokeWSforStory(params);
			check++;
			storyET.setText("");
			storyTag = "";

		}
		if(Utility.isNotNull(placeTag)){
			params.put("isSemantic",  choice);
			params.put("func", "place");
			params.put("term", placeTag);
			searched = placeTag;
			invokeWSforPlace(params);
			check++;
			placeET.setText("");
			placeTag = "";
		}
		if (check != 1){
			Toast.makeText(getApplicationContext(), "Please fill one field at once!", Toast.LENGTH_LONG).show();
		}
	}
	public void invokeWSforUser(RequestParams params){
		
		
		//http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/Search?term=burcin&func=user&isSemantic=false(POST)
		searchList.clear();
		storyIDs.clear();
		ownerList.clear();
		placeIDs.clear(); // for place search
		placeIDList.clear(); // for story search
		placeNameList.clear(); // for story search
		// Show Progress Dialog
		prgDialog.show();
		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();		 
		client.post(Utility.SERVER_NAME + "Search?", params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					JSONArray jsonarray = new JSONArray(response);
					Log.e("invokeWSforUser.SEARCH_RESULT", response.toString());

					for(int i=0; i<jsonarray.length(); i++){
						JSONObject obj = jsonarray.getJSONObject(i);

						String story_content = obj.getString("Name");

						String owner = obj.getString("Email");
						
						
						int user_id = obj.getInt("UserId");
						
						
						String placeName ="";
						int place_id = -1;
						HashMap<String, String> map = new HashMap<String, String>();
						storyIDs.add(user_id);
						ownerList.add(owner);
						placeNameList.add(placeName);
						placeIDList.add(place_id);
						map.put(KEY_ID, "" + user_id);
						map.put(KEY_TITLE, story_content);
						map.put(KEY_INFO, owner);
						searchList.add(map);

					}   
					readSearchList("user");




				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e("invokeWSforUser.json" , "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("invokeWSforUser.404" ,  "Requested resource not found");
				} 
				// When Http response code is '500'
				else if(statusCode == 500){
					Log.e("invokeWSforUser.500" ,  "Something went wrong at server end");
				} 
				// When Http response code other than 404, 500
				else{
					Log.e("invokeWSforUser.other" ,  "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}
	public void invokeWSforStory(RequestParams params){
		searchList.clear();
		storyIDs.clear();
		ownerList.clear();
		placeIDs.clear(); // for place search
		placeIDList.clear(); // for story search
		placeNameList.clear(); // for story search
		// Show Progress Dialog
		prgDialog.show();
		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();		 
		client.post(Utility.SERVER_NAME + "Search?", params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					JSONArray jsonarray = new JSONArray(response);
					Log.e("invokeWSforStory.SEARCH_RESULT", response.toString());

					for(int i=0; i<jsonarray.length(); i++){
						JSONObject obj = jsonarray.getJSONObject(i);

						String story_content = obj.getString("content");

						String owner = obj.getString("mail");
						int story_id = obj.getInt("storyId");
						String placeName = obj.getString("placeName");
						int place_id = obj.getInt("placeId");
						HashMap<String, String> map = new HashMap<String, String>();
						storyIDs.add(story_id);
						ownerList.add(owner);
						placeNameList.add(placeName);
						placeIDList.add(place_id);
						map.put(KEY_ID, "" + story_id);
						map.put(KEY_TITLE, story_content);
						map.put(KEY_INFO, owner);
						searchList.add(map);

					}   
					readSearchList("story");


				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e("invokeWSforStory.json" , "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("invokeWSforStory.404" , "Requested resource not found");
				} 
				// When Http response code is '500'
				else if(statusCode == 500){
					Log.e("invokeWSforStory.500" , "Something went wrong at server end");
				} 
				// When Http response code other than 404, 500
				else{
					Log.e("invokeWSforStory.other" , "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}
	public void invokeWSforPlace(RequestParams params){
		searchList.clear();
		storyIDs.clear();
		ownerList.clear();
		placeIDs.clear(); // for place search
		placeIDList.clear(); // for story search
		placeNameList.clear(); // for story search
		// Show Progress Dialog
		prgDialog.show();
		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();		 
		client.post(Utility.SERVER_NAME + "Search?", params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					JSONArray jsonarray = new JSONArray(response);
					Log.e("invokeWSforPlace.SEARCH_RESULT", response.toString());

					for(int i=0; i<jsonarray.length(); i++){
						JSONObject obj = jsonarray.getJSONObject(i);

						String place_name = obj.getString("Name");

						String lat = obj.getString("Latitude");
						String longi = obj.getString("Longtitude");
						int place_id = obj.getInt("PlaceID");
						HashMap<String, String> map = new HashMap<String, String>();

						placeIDs.add(place_id);
						map.put(KEY_ID, "" + place_id);
						map.put(KEY_TITLE, place_name);
						map.put(KEY_INFO, "Lat: " + lat + "  Long: " + longi);
						searchList.add(map);
						//	searchItems.add(place_name);

					}
					readSearchList("place");
				}

				catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e("invokeWSforPlace.json" , "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("invokeWSforPlace.404" , "Requested resource not found");
				} 
				// When Http response code is '500'
				else if(statusCode == 500){
					Log.e("invokeWSforPlace.500" , "Something went wrong at server end");
				} 
				// When Http response code other than 404, 500
				else{
					Log.e("invokeWSforPlace.other" , "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}
	public void callAddStoryDialog() {

		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final TextView input = new TextView(this);
		alert.setMessage("There is no result for this search. Do you want to add a new story with this tag?");
		alert.setTitle("NO RESULT"); 
		alert.setView(input);
		alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				navigateToAddStoryActivity();
			}
		});

		alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				dialog.dismiss();
			}
		});
		alert.show();   

	}
	private void readSearchList(final String typeUserOrStoryOrPlace) {


		TimelineAdapter adapter = new TimelineAdapter(this, searchList , "search");
		int size = searchList.size() ;
		if(size == 0) {
			callAddStoryDialog();

		}
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
				Log.e("arg2", arg2+"");
				try {

					String owner = "";

					if(typeUserOrStoryOrPlace.equals("story")){
						owner = ownerList.get(arg2);
						Log.e("story", arg2+"");
						startShowStoryActivity(""+ storyIDs.get(arg2), owner,""+placeIDList.get(arg2),placeNameList.get(arg2));
					}
					else if(typeUserOrStoryOrPlace.equals("place")){
						Log.e("place", arg2+"");
						navigateToTimelineActivityForPlaces(""+ placeIDs.get(arg2));
					}
					else {
						owner = ownerList.get(arg2);
						Log.e("user", arg2+"");

						navigateToProfileActivity(owner);


					}
					if (owner.equals("") && typeUserOrStoryOrPlace.equals("place")){
						type = "placesStories";
					}
					
					if (owner.equals("") && typeUserOrStoryOrPlace.equals("user")){
						type = "subsStories";
					}
					else if (owner.equals(Utility.myUserName)) {
						type = "myStories";

					}
					else {
						type = "subsStories";
					}
				} catch (Exception e) {

					Log.e("SEARCH EXCEPTION", e.getMessage());
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

	public void navigateToAddStoryActivity() {
		Intent addStoryIntent = new Intent(getApplicationContext(),AddStoryActivity.class);
		Bundle b = new Bundle();
		b.putString("tags",searched);
		addStoryIntent.putExtras(b);
		addStoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(addStoryIntent);

	}

	public void navigateToProfileActivity(String user_name) {
		Intent profileIntent = new Intent(getApplicationContext(),ProfileActivity.class);
		Bundle b = new Bundle();
		b.putString("user_name",user_name);
		profileIntent.putExtras(b);
		profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(profileIntent);

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
	public void  startShowStoryActivity(String story_id, String owner, String place_id, String placeName) {


		Utility.IDFromName(owner);
		String storyOwnerID = Utility.userIDFromName;

		Intent showStoryIntent = new Intent(getApplicationContext(),ShowStoryActivity.class);
		Bundle b = new Bundle();
		b.putString("story_id",story_id);
		b.putString("owner", owner);
		b.putString("storyOwnerID", storyOwnerID);
		b.putString("place_id",place_id);
		b.putString("placeName", placeName);
		showStoryIntent.putExtras(b);
		// Clears History of Activity
		showStoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(showStoryIntent);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		inflater = getMenuInflater();
		inflater.inflate(R.menu.advancedsearch_menu, menu);

		return super.onCreateOptionsMenu(menu);

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.ownProfile:
			navigateToProfileActivity(Utility.myUserName);
			break;

		case R.id.recommendation:
			navigateToRecommendationActivity();
			break;
		case R.id.map:
			navigateToHomeMapActivity();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void navigateToRecommendationActivity() {
		Intent recomIntent = new Intent(getApplicationContext(),RecommendationActivity.class);
		recomIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(recomIntent);

	}

	public void navigateToHomeMapActivity() {
		Intent homeMapIntent = new Intent(getApplicationContext(),HomeMapActivity.class);
		homeMapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(homeMapIntent);

	}


}
