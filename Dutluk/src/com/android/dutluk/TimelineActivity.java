package com.android.dutluk;

import java.io.BufferedReader;
import java.io.File;
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

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class TimelineActivity extends Activity {

	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_DURATION = "duration";

	ActionBar actionBar;
	MenuInflater inflater;
	Menu m;
	String MEDIA_PATH;
	File[] songList = {};
	ListView lv;
	ArrayList<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>();
	String[] links = new String[1000];
	WebView webView;
	ProgressDialog dialog;
	Boolean isInput = true;
	MediaPlayer song;
	boolean isLong = false;
	int pageNumber = 1;
	LinearLayout ll;
	String searchedTerm;
	Parcelable state;
	TextView guide;
	LinearLayout guideLayout;
	int searchTry = 0;
	String mail = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		Intent registerIntent = getIntent();
        mail = registerIntent.getStringExtra("mail");
		LinearLayout layout = (LinearLayout) findViewById(R.id.reklam);
		guide = (TextView) findViewById(R.id.guide);
		guideLayout = (LinearLayout) findViewById(R.id.textLayout);
		lv = (ListView) findViewById(R.id.listView1);
		final View footerView = getLayoutInflater().inflate(R.layout.more, null);
		lv.addFooterView(footerView);
		ll = (LinearLayout) findViewById(R.id.more);
		ll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				state = lv.onSaveInstanceState();
			}
		});

		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(16, 188, 201)));
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		new MyTask().execute();
		//readData();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private void readData() {
		// TODO Auto-generated method stub

		MyAdapter adapter = new MyAdapter(this, alist);
		lv.setAdapter(adapter);
		if (state != null)
			lv.onRestoreInstanceState(state);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				try {
					String story_id = 0 + "";
					startShowStoryActivity(story_id);

				} catch (Exception e) {
					// TODO: handle exception
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

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		final android.widget.SearchView searchView = (android.widget.SearchView) menu
				.findItem(R.id.search).getActionView();
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint("Search Story");
		searchView
				.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {

					@Override
					public boolean onQueryTextSubmit(String query) {
						// TODO Auto-generated method stub

						return false;
					}

					@Override
					public boolean onQueryTextChange(String newText) {
						// TODO Auto-generated method stub

						return false;
					}
				});
		MenuItem menuItem = menu.findItem(R.id.search);
		m = menu;
		menuItem.setOnActionExpandListener(new OnActionExpandListener() {

			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {

				m.findItem(R.id.item3);
				m.findItem(R.id.map);
				m.findItem(R.id.item3).setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM);
				m.findItem(R.id.map).setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM);
				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				// TODO Auto-generated method stub

				m.findItem(R.id.item3);
				m.findItem(R.id.item3).setShowAsAction(
						MenuItem.SHOW_AS_ACTION_ALWAYS);
				m.findItem(R.id.map);
				m.findItem(R.id.map).setShowAsAction(
						MenuItem.SHOW_AS_ACTION_ALWAYS);
				return true;
			}
		});
		return super.onCreateOptionsMenu(menu);

	}


	public void  startShowStoryActivity(String story_id) {
	
		Intent showStoryIntent = new Intent(getApplicationContext(),ShowStoryActivity.class);
		Bundle b = new Bundle();
		b.putString("story_id",story_id);
		showStoryIntent.putExtras(b);
		// Clears History of Activity
		showStoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(showStoryIntent);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.search:
			guideLayout.setVisibility(LinearLayout.GONE);
			break;

		case R.id.item3:
			navigateToAddStoryActivity();
			break;
		case R.id.map:
			navigateToMapActivity();
			
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	public void navigateToAddStoryActivity() {
		Intent timelineIntent = new Intent(getApplicationContext(),AddStoryActivity.class);
		Bundle b = new Bundle();
		b.putString("mail",mail);
		timelineIntent.putExtras(b);
		timelineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(timelineIntent);
		
	}
	public void navigateToMapActivity() {
		Intent timelineIntent = new Intent(getApplicationContext(),MapActivity.class);
		Bundle b = new Bundle();
		b.putString("mail",mail);
		timelineIntent.putExtras(b);
		timelineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(timelineIntent);
		
	}
	private class MyTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			readData();
		}

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient httpclient = new DefaultHttpClient();
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("email", mail));
			String url = "http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/GetStory?" + URLEncodedUtils.format(pairs, "utf-8");
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
			        HashMap<String, String> map = new HashMap<>();
					map.put(KEY_ID, "a" + i);
					map.put(KEY_TITLE, name);
					map.put(KEY_DURATION, "info"+i);
					alist.add(map);
			    }   
			


				} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
				return null;

		}

	}

}
