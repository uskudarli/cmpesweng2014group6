package com.android.dutluk;


import java.util.ArrayList;
import java.util.HashMap;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.Toast;



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
	MenuInflater inflater;
	Menu m;
	ArrayList<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>();
	


	ArrayList<ArrayList<String>> listForMarkers = new ArrayList<ArrayList<String>>();  

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
		
		
		RequestParams params = new RequestParams();
		invokeWS(params);

	}
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public void invokeWS(RequestParams params){
		prgDialog.show();
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Utility.SERVER_NAME + "GetPlace" , params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String response) {
				Log.e("PLACE LIST", response);
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
	
		return super.onCreateOptionsMenu(menu);

	}


	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		switch (item.getItemId()) {
		case R.id.addStory:
			navigateToAddStoryActivity();
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

	public void navigateToAddStoryActivity() {
		Intent addStoryIntent = new Intent(getApplicationContext(),AddStoryActivity.class);
		addStoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(addStoryIntent);
		
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