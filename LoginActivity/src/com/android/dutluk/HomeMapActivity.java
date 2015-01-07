package com.android.dutluk;


import java.util.ArrayList;
import java.util.HashMap;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnActionExpandListener;

import android.widget.AdapterView;
import android.widget.ListView;




import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.*;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import android.app.ProgressDialog;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class HomeMapActivity extends FragmentActivity {


	ProgressDialog prgDialog;
	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_INFO = "info";

	ActionBar actionBar;
	
	ArrayList<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>();
	


	ArrayList<ArrayList<String>> listForMarkers = new ArrayList<ArrayList<String>>();  
	
	
	Boolean isInput = true;
	ListView lv;
	
	MenuInflater inflater;
	Menu m;

	ArrayList<Integer> storyIDs = new ArrayList<Integer>();
	ArrayList<Integer> placeIDList = new ArrayList<Integer>();
	ArrayList<String> placeNameList = new ArrayList<String>();
	ArrayList<String> ownerList = new ArrayList<String>();
	ArrayList<Integer> placeIDs = new ArrayList<Integer>();
	ArrayList<HashMap<String, String>> searchList = new ArrayList<HashMap<String, String>>();

	GoogleMap map;
	Context context = this; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
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
		map = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.gmap)).getMap();
		map.setMyLocationEnabled(true);
		
		lv = (ListView) findViewById(R.id.listView1);
		
		Utility.IDFromName(Utility.myUserName);
		
		RequestParams params = new RequestParams();
		invokeWS(params);

	}

	public void invokeWS(RequestParams params){
		prgDialog.show();
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Utility.SERVER_NAME + "GetPlace" , params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String response) {
				Log.e("invokeWSGetPlace PLACE LIST", response);
				prgDialog.hide();
				int size = 0;
				final int size_f ;
				try {
					JSONArray jsonarray = new JSONArray(response);

					size = jsonarray.length();
					size_f = size;
					for(int i=0; i<size; i++){
						JSONObject obj = jsonarray.getJSONObject(i);

						String name = obj.getString("Name");

						String placeID = obj.getString("PlaceID");
						
						String latitude = obj.getString("Latitude");
						String longitude = obj.getString("Longtitude");
						ArrayList<String> list = new ArrayList<String>();
						list.add(name);
						list.add(placeID);
						list.add(latitude);
						list.add(longitude);
						
						listForMarkers.add(list);
					}
					
					for(int i = 0 ; i < size; i++){
						ArrayList<String> temp = listForMarkers.get(i);
						String temp_name= temp.get(0);
						String temp_placeID= temp.get(1);
						String temp_latitude= temp.get(2);
						String temp_longitude= temp.get(3);
						LatLng temp_latlng = new LatLng(Double.parseDouble(temp_latitude), Double.parseDouble(temp_longitude));
						map.addMarker(new MarkerOptions()
						.title(temp_name)
						.position(temp_latlng));
					}
					map.setOnInfoWindowClickListener(
							  new OnInfoWindowClickListener(){
							    public void onInfoWindowClick(Marker marker){
							    	String placeID = "";
							    	double lat = marker.getPosition().latitude;
							    	double longi = marker.getPosition().longitude;
							    	LatLng searched = new LatLng(lat, longi);
							    	for(int i = 0 ; i < size_f; i++){
										ArrayList<String> temp = listForMarkers.get(i);
										String temp_name= temp.get(0);
										String temp_placeID= temp.get(1);
										String temp_latitude= temp.get(2);
										String temp_longitude= temp.get(3);
										LatLng temp_latlng = new LatLng(Double.parseDouble(temp_latitude), Double.parseDouble(temp_longitude));
										if(temp_latlng.equals(searched))
											placeID = temp_placeID;
									}
							    	
							    	navigateToTimelineActivityForPlaces(placeID);
							    }
							  }
							);
					
				} catch (JSONException e) {
					Log.e("invokeWSGetPlace.json", e.toString());
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int statusCode, Throwable error,
					String content) {
				// Hide Progress Dialog
				prgDialog.hide();
				Log.e("failure", content);
				// When HTTP response code is '404'
				if(statusCode == 404){
					Log.e("invokeWSGetPlace.404", "Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){
					Log.e("invokeWSGetPlace.500",  "Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("invokeWSGetPlace.other",  "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
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


	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		inflater = getMenuInflater();
		inflater.inflate(R.menu.homemap_menu, menu);
		final android.widget.SearchView searchView = (android.widget.SearchView) menu
				.findItem(R.id.search).getActionView();
		searchView.setSubmitButtonEnabled(true);
		
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
				m.findItem(R.id.ownProfile);
				m.findItem(R.id.ownProfile).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				m.findItem(R.id.recommendation);
				m.findItem(R.id.recommendation).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				m.findItem(R.id.advancedSearch);
				m.findItem(R.id.advancedSearch).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				
				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				
				m.findItem(R.id.addStory);
				m.findItem(R.id.addStory).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);			
				m.findItem(R.id.ownProfile);
				m.findItem(R.id.ownProfile).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				m.findItem(R.id.recommendation);
				m.findItem(R.id.recommendation).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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
		case R.id.recommendation:
			navigateToRecommendationActivity();
			break;
		case R.id.advancedSearch:
			navigateToAdvancedSearchActivity();
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	public void navigateToRecommendationActivity() {
		Intent recomIntent = new Intent(getApplicationContext(),RecommendationActivity.class);
		recomIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(recomIntent);

	}

	public void navigateToAddStoryActivity() {
		Intent addStoryIntent = new Intent(getApplicationContext(),AddStoryActivity.class);
		Bundle b = new Bundle();
		b.putString("tags","");
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
	public void navigateToAdvancedSearchActivity() {
		Intent searchIntent = new Intent(getApplicationContext(),AdvancedSearchActivity.class);
		searchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(searchIntent);
		
	}
	
}