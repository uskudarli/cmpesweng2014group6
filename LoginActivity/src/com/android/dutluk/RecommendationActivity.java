package com.android.dutluk;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
import android.view.MotionEvent;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;



public class RecommendationActivity extends Activity {

	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_INFO = "info";
	ProgressDialog prgDialog;
	ActionBar actionBar;
	MenuInflater inflater;
	Menu m;

	ListView lvTopRated;
	ListView lvRecomForYou;
	Boolean isInput = true;

	ArrayList<Integer> storyIDsTopRated = new ArrayList<Integer>();
	ArrayList<Integer> placeIDListTopRated = new ArrayList<Integer>();
	ArrayList<String> placeNameListTopRated = new ArrayList<String>();
	ArrayList<String> ownerListTopRated = new ArrayList<String>();

	ArrayList<Integer> placeIDsTopRated = new ArrayList<Integer>();


	ArrayList<Integer> storyIDsRecomForYou = new ArrayList<Integer>();
	ArrayList<Integer> placeIDListRecomForYou = new ArrayList<Integer>();
	ArrayList<String> placeNameListRecomForYou = new ArrayList<String>();
	ArrayList<String> ownerListRecomForYou = new ArrayList<String>();

	ArrayList<Integer> placeIDsRecomForYou = new ArrayList<Integer>();

	ArrayList<HashMap<String, String>> topRatedList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> recomForYouList = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommendation);




		lvTopRated = (ListView) findViewById(R.id.topRated);
		lvRecomForYou  =(ListView) findViewById(R.id.recomForYou);

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


		new MyTask().execute();

	}

	private void readTopRated() {


		TimelineAdapter adapter = new TimelineAdapter(this, topRatedList, "story");
		lvTopRated.setAdapter(adapter);
		lvTopRated.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				try {

					startShowStoryActivity(""+ storyIDsTopRated.get(arg2), ownerListTopRated.get(arg2),""+placeIDListTopRated.get(arg2),placeNameListTopRated.get(arg2));

				} catch (Exception e) {
					Log.e("SEARCH EXCEPTION", e.getMessage());
				}

			}
		});
		lvTopRated.setLongClickable(true);
		lvTopRated.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				return isInput;

			}
		});
		lvTopRated.setOnTouchListener(new ListView.OnTouchListener() {
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

	}

	private void readRecomForYou() {


		TimelineAdapter adapter = new TimelineAdapter(this, recomForYouList, "story");
		lvRecomForYou.setAdapter(adapter);
		lvRecomForYou.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				try {

					startShowStoryActivity(""+ storyIDsRecomForYou.get(arg2), ownerListRecomForYou.get(arg2),""+placeIDListRecomForYou.get(arg2),placeNameListRecomForYou.get(arg2));

				} catch (Exception e) {
					Log.e("SEARCH EXCEPTION", e.getMessage());
				}

			}
		});
		lvRecomForYou.setLongClickable(true);
		lvRecomForYou.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				return isInput;

			}
		});
		lvRecomForYou.setOnTouchListener(new ListView.OnTouchListener() {
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

	}

	private class MyTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			readTopRated();
			readRecomForYou();
		}

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient httpclient = new DefaultHttpClient();

			topRatedList.clear();
			recomForYouList.clear();

			storyIDsTopRated.clear();
			ownerListTopRated.clear();
			placeIDListTopRated.clear();
			placeNameListTopRated.clear();

			storyIDsRecomForYou.clear();
			ownerListRecomForYou.clear();
			placeIDListRecomForYou.clear();
			placeNameListRecomForYou.clear();



			String urlTopRated = Utility.SERVER_NAME + "GetRecommendedStories" ;		
			HttpPost httppostTopRated = new HttpPost(urlTopRated);

			try {

				HttpResponse getResponseTopRated = httpclient.execute(httppostTopRated);
				BufferedReader rdTopRated = new BufferedReader(new InputStreamReader(
						getResponseTopRated.getEntity().getContent()));
				StringBuffer resultTopRated = new StringBuffer();
				String line = "";
				while ((line = rdTopRated.readLine()) != null) {
					resultTopRated.append(line);
				}

				Log.e("TOP RATED STORIES", resultTopRated.toString());
				JSONArray jsonarrayTopRated = new JSONArray(resultTopRated.toString());


				for(int i=0; i<jsonarrayTopRated.length(); i++){
					JSONObject objTopRated = jsonarrayTopRated.getJSONObject(i);


					String content = objTopRated.getString("content");
					String story = "";
					if(content.length()<50)
						story = content;
					else 
						story = content.substring(0,50)+ "...";
					String user_id = objTopRated.getString("userId");
					Utility.nameFromID(user_id);
					String owner = Utility.userNameFromID;
					int story_id = objTopRated.getInt("storyId");
					String placeName = objTopRated.getString("placeName");
					int place_id = objTopRated.getInt("placeId");
					HashMap<String, String> map = new HashMap<String, String>();
					storyIDsTopRated.add(story_id);
					ownerListTopRated.add(owner);
					placeNameListTopRated.add(placeName);
					placeIDListTopRated.add(place_id);
					map.put(KEY_ID,""+ story_id);
					map.put(KEY_TITLE, story);
					map.put(KEY_INFO, owner);
					topRatedList.add(map);
				}   



			} catch (ClientProtocolException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			} catch (JSONException e) {

				e.printStackTrace();
			}


			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			int id = randInt(1,7) ;

			pairs.add(new BasicNameValuePair("themeId", id + "" ));		
			//http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/GetRecommendedStories?themeId=1(GET)
			String urlRecomForYou = Utility.SERVER_NAME + "GetRecommendedStories?" + URLEncodedUtils.format(pairs, "utf-8");	
			HttpGet httpgetRecomForYou = new HttpGet(urlRecomForYou);
			while(recomForYouList.size() == 0) {
				try {

					HttpResponse getResponseRecomForYou = httpclient.execute(httpgetRecomForYou);
					BufferedReader rdRecomForYou = new BufferedReader(new InputStreamReader(
							getResponseRecomForYou.getEntity().getContent()));
					StringBuffer resultRecomForYou = new StringBuffer();
					String lineRecomForYou = "";
					while ((lineRecomForYou = rdRecomForYou.readLine()) != null) {
						resultRecomForYou.append(lineRecomForYou);
					}

					Log.e("RECOMFORYOU STORIES", resultRecomForYou.toString());
					JSONArray jsonarrayRecomForYou = new JSONArray(resultRecomForYou.toString());


					for(int i=0; i<jsonarrayRecomForYou.length(); i++){





						JSONObject objRecomForYou = jsonarrayRecomForYou.getJSONObject(i);
						String content = objRecomForYou.getString("content");
						String story = "";
						if(content.length()<50)
							story = content;
						else 
							story = content.substring(0,50)+ "...";
						String user_id = objRecomForYou.getString("userId");
						Utility.nameFromID(user_id);
						String owner = Utility.userNameFromID;
						int story_id = objRecomForYou.getInt("storyId");
						String placeName = objRecomForYou.getString("placeName");
						int place_id = objRecomForYou.getInt("placeId");
						HashMap<String, String> map = new HashMap<String, String>();
						storyIDsRecomForYou.add(story_id);
						ownerListRecomForYou.add(owner);
						placeNameListRecomForYou.add(placeName);
						placeIDListRecomForYou.add(place_id);
						map.put(KEY_ID,""+ story_id);
						map.put(KEY_TITLE, story);
						map.put(KEY_INFO, owner);
						recomForYouList.add(map);
					}   



				} catch (ClientProtocolException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				} catch (JSONException e) {

					e.printStackTrace();
				}

			}

			return null;

		}

	}
	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
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
		inflater.inflate(R.menu.profile_menu, menu);

		return super.onCreateOptionsMenu(menu);

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
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


	public void navigateToProfileActivity(String user_name) {
		Intent profileIntent = new Intent(getApplicationContext(),ProfileActivity.class);
		Bundle b = new Bundle();
		b.putString("user_name",user_name);
		profileIntent.putExtras(b);
		profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(profileIntent);

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


	public void navigateToAdvancedSearchActivity() {
		Intent searchIntent = new Intent(getApplicationContext(),AdvancedSearchActivity.class);
		searchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(searchIntent);

	}


}
