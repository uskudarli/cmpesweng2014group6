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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;

import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;



public class TimelineActivity extends Activity {

	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_INFO = "info";

	ActionBar actionBar;
	MenuInflater inflater;
	Menu m;

	ListView lv;
	ArrayList<HashMap<String, String>> storyList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> searchList = new ArrayList<HashMap<String, String>>();

	ProgressDialog prgDialog;
	Boolean isInput = true;
	LinearLayout ll;
	String searchedTerm;
	String type = "";
	String type_send = "";
	String user_name = "";
	ArrayList<Integer> storyIDs = new ArrayList<Integer>();
	ArrayList<Integer> placeIDList = new ArrayList<Integer>();
	ArrayList<String> placeNameList = new ArrayList<String>();
	ArrayList<String> ownerList = new ArrayList<String>();
	
	ArrayList<Integer> placeIDs = new ArrayList<Integer>();
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);

		Intent comingIntent = getIntent();
		type = comingIntent.getStringExtra("type");
		
	
		if(type.equals("myStories")){		
			type_send = ""+0;
			user_name = comingIntent.getStringExtra("user_name");
		}
		if(type.equals("placesStories")){  
			type_send = comingIntent.getStringExtra("placeID");
		}
		if(type.equals("subsStories")){
			type_send = ""+1;
			user_name = comingIntent.getStringExtra("user_name");
		}


		lv = (ListView) findViewById(R.id.listView1);

		// Instantiate Progress Dialog object
		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
		prgDialog.setMessage("Please wait...");
		// Set Cancelable as False
		prgDialog.setCancelable(false);
		

		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 0, 0)));
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		new MyTask().execute();

	}


	private void readStoryList() {


		TimelineAdapter adapter = new TimelineAdapter(this, storyList, "story");
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				try {

						startShowStoryActivity(""+ storyIDs.get(arg2), ownerList.get(arg2),""+placeIDList.get(arg2),placeNameList.get(arg2));

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
	
	private class MyTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			readStoryList();
		}

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient httpclient = new DefaultHttpClient();
			storyList.clear();
			storyIDs.clear();
			ownerList.clear();
			placeIDList.clear();
			placeNameList.clear();
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("email", user_name));
			pairs.add(new BasicNameValuePair("type", type_send));
			String url = Utility.SERVER_NAME + "GetStory?" + URLEncodedUtils.format(pairs, "utf-8");		
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

				Log.e("ALL STORIES", result.toString());
				JSONArray jsonarray = new JSONArray(result.toString());


				for(int i=0; i<jsonarray.length(); i++){
					JSONObject obj = jsonarray.getJSONObject(i);
					String content = obj.getString("content");
					String story = "";
					if(content.length()<50)
						story = content;
					else 
						story = content.substring(0,50)+ "...";
					String owner = obj.getString("mail");
					int story_id = obj.getInt("storyId");
					String placeName = obj.getString("placeName");
					int place_id = obj.getInt("placeId");
					HashMap<String, String> map = new HashMap<String, String>();
					storyIDs.add(story_id);
					ownerList.add(owner);
					placeNameList.add(placeName);
					placeIDList.add(place_id);
					map.put(KEY_ID,""+ story_id);
					map.put(KEY_TITLE, story);
					map.put(KEY_INFO, owner);
					storyList.add(map);
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
		inflater.inflate(R.menu.timeline_menu, menu);
		final android.widget.SearchView searchView = (android.widget.SearchView) menu
				.findItem(R.id.search).getActionView();
		searchView.setSubmitButtonEnabled(true);
		if(type_send.equals("0") || type_send.equals("1") )
			searchView.setQueryHint("Search Story");
		else 
			searchView.setQueryHint("Search Place");	
		searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				AsyncHttpClient client = new AsyncHttpClient();
				searchList.clear();
				storyIDs.clear();
				ownerList.clear();
				placeIDs.clear(); // for place search
				placeIDList.clear(); // for story search
				placeNameList.clear(); // for story search
				if(type_send.equals("0") || type_send.equals("1") ){
					
					client.post(Utility.SERVER_NAME + "Search?func=story&isSemantic=false&term=" + query, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
						
							try {
								JSONArray jsonarray = new JSONArray(response);
								Log.e("SEARCH_RESULT", response.toString());

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
								readSearchList();

							} catch (JSONException e) {

								e.printStackTrace();
							}
						}
					});
				}
				else {
					
					client.post(Utility.SERVER_NAME + "Search?func=place&isSemantic=false&term=" + query, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							try {
						
								JSONArray jsonarray = new JSONArray(response);
								Log.e("SEARCH_RESULT", response.toString());

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
								}   
								readSearchList();

							} catch (JSONException e) {

								e.printStackTrace();
							}
						}
					});
				}
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				return false;
			}
		});
		MenuItem menuItem = menu.findItem(R.id.search);
		m = menu;
		menuItem.setOnActionExpandListener(new OnActionExpandListener() {

			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {

				m.findItem(R.id.addStory);
				m.findItem(R.id.addStory).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				m.findItem(R.id.map);
				m.findItem(R.id.map).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				m.findItem(R.id.ownProfile);
				m.findItem(R.id.ownProfile).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				m.findItem(R.id.advancedSearch);
				m.findItem(R.id.advancedSearch).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {


				m.findItem(R.id.addStory);
				m.findItem(R.id.addStory).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				m.findItem(R.id.map);
				m.findItem(R.id.map).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				m.findItem(R.id.ownProfile);
				m.findItem(R.id.ownProfile).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				m.findItem(R.id.advancedSearch);
				m.findItem(R.id.advancedSearch).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				return true;
			}
		});
		return super.onCreateOptionsMenu(menu);

	}
	private void readSearchList() {


		TimelineAdapter adapter = new TimelineAdapter(this, searchList , "search");
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				try {
					if(type_send.equals("0") || type_send.equals("1") )
						startShowStoryActivity(""+ storyIDs.get(arg2), ownerList.get(arg2),""+placeIDList.get(arg2),placeNameList.get(arg2));
					else 
						navigateToTimelineActivityForPlaces(""+ placeIDs.get(arg2));
						

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


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.search:			
			break;
		case R.id.addStory:
			navigateToAddStoryActivity();
			break;
		case R.id.ownProfile:
			navigateToProfileActivity(Utility.myUserName);
			break;
		case R.id.map:
			navigateToHomeMapActivity();
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
		Bundle b = new Bundle();
		b.putString("tags","");
		addStoryIntent.putExtras(b);
		addStoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(addStoryIntent);

	}
	public void navigateToHomeMapActivity() {
		Intent homeMapIntent = new Intent(getApplicationContext(),HomeMapActivity.class);
		homeMapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(homeMapIntent);

	}
	public void navigateToProfileActivity(String user_name) {
		Intent profileIntent = new Intent(getApplicationContext(),ProfileActivity.class);
		Bundle b = new Bundle();
		b.putString("user_name",user_name);
		profileIntent.putExtras(b);
		profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(profileIntent);

	}
	public void navigateToAdvancedSearchActivity() {
		Intent searchIntent = new Intent(getApplicationContext(),AdvancedSearchActivity.class);
		searchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(searchIntent);
		
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
	


}
