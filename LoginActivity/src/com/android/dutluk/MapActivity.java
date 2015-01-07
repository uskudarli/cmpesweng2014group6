package com.android.dutluk;


import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnActionExpandListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.location.LocationListener;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import android.app.ProgressDialog;


import com.koushikdutta.ion.Ion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class MapActivity extends FragmentActivity implements LocationListener {
	ActionBar actionBar;
	ProgressDialog prgDialog;

	GoogleMap map;
	Context context = this; 


	String story ="";
	String tagsForStory = "";
	String themeId ="";
	String time="";
	//String uploadedPath = "";
	String latitude = "";
	String longitude = "";
	String tagsForPlace = "";

	String story_id = "";

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
	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_INFO = "info";
boolean imageCheck = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
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
		Intent addStoryIntent = getIntent();
		story = addStoryIntent.getStringExtra("story");
		tagsForStory = addStoryIntent.getStringExtra("tagsForStory");
		themeId = addStoryIntent.getStringExtra("themeId");
		time = addStoryIntent.getStringExtra("time");
		imageCheck = addStoryIntent.getBooleanExtra("imageCheck",false);

	
		
		map = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.gmap)).getMap();
		map.setMyLocationEnabled(true);
		RequestParams params = new RequestParams();
		invokeWSforGET(params);

		map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {


			public void onMapLongClick(final LatLng latlng) {
				LayoutInflater li = LayoutInflater.from(context);
				final View v = li.inflate(R.layout.map2, null);
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setView(v);
				builder.setCancelable(false);

				builder.setPositiveButton("Create Story", new DialogInterface.OnClickListener() {


					public void onClick(DialogInterface dialog, int which) {
						EditText title = (EditText) v.findViewById(R.id.ettitle);


						map.addMarker(new MarkerOptions()
						.title(title.getText().toString())

						.position(latlng)        
								);

						latitude = latlng.latitude + "";
						longitude = latlng.longitude+ "" ;
						tagsForPlace = title.getText().toString();
						saveStory();
					
					}
				});

				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {


					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();

					}
				});

				AlertDialog alert = builder.create();
				alert.show();
			}
		});

	}

	public void invokeWSforGET(RequestParams params){
		prgDialog.show();
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Utility.SERVER_NAME + "GetPlace" , params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String response) {
				Log.e("PLACE LIST", response);
				prgDialog.hide();
				try {
					JSONArray jsonarray = new JSONArray(response);


					for(int i=0; i<jsonarray.length(); i++){
						JSONObject obj = jsonarray.getJSONObject(i);

						String name = obj.getString("Name");
						String longi = obj.getString("Longtitude");
						String lat = obj.getString("Latitude");

						LatLng temp = new LatLng(Double.parseDouble(lat), Double.parseDouble(longi));
						map.addMarker(new MarkerOptions()
						.title(name)
						.position(temp));
						map.setOnInfoWindowClickListener(
								new OnInfoWindowClickListener(){
									public void onInfoWindowClick(final Marker marker){
										LayoutInflater li = LayoutInflater.from(context);
										final View v = li.inflate(R.layout.map2, null);
										AlertDialog.Builder builder = new AlertDialog.Builder(context);
										builder.setView(v);
										builder.setCancelable(false);

										builder.setPositiveButton("Create Story", new DialogInterface.OnClickListener() {


											public void onClick(DialogInterface dialog, int which) {
												EditText title = (EditText) v.findViewById(R.id.ettitle);

												latitude = marker.getPosition().latitude + "";
												longitude = marker.getPosition().longitude+ "" ;
												tagsForPlace = title.getText().toString();
												saveStory();
											
											}
										});

										builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {


											public void onClick(DialogInterface dialog, int which) {
												dialog.cancel();

											}
										});

										AlertDialog alert = builder.create();
										alert.show();
									}
								}
								);
					}

					Log.e("GetPlace.success",response);
				} catch (JSONException e) {
					Log.e("GetPlace.json",e.toString());
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int statusCode, Throwable error,
					String content) {
				// Hide Progress Dialog
				Log.e("failure", content);
				prgDialog.hide();
				// When HTTP response code is '404'
				if(statusCode == 404){
					Log.e("GetPlace.404","Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){
					Log.e("GetPlace.500", "Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("GetPlace.other", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}





	public void saveStory(){
		RequestParams params = new RequestParams();
		params.put("mail",Utility.myUserName);
		// düzelt:)
		//params.put("image", image);
		params.put("story", story);
		params.put("storyTag", tagsForStory);
		params.put("themeId", themeId);
		params.put("time", time);
		params.put("lat", latitude);
		params.put("lng", longitude);				
		params.put("placeTag", tagsForPlace);

		invokeWSforSAVE(params);

	}    

	public void invokeWSforSAVE(RequestParams params){
		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();

		client.post(Utility.SERVER_NAME + "AddStory",params ,new AsyncHttpResponseHandler() {
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
						// Set Default Values for Edit View controls

						showExpPoint();

					
						Log.e("AddStory.success", imageCheck + "a");

						story_id = obj.getString("message");
						//Log.e("AddStory.story_id", imageCheck + "a");
						
						if(imageCheck)
							uploadImage();
					} 
					
					else{
						Log.e("AddStory.error",  obj.getString("message"));
					
					}
				} catch (JSONException e) {

					Log.e("AddStory.json","Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("AddStory.404", "Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){
					Log.e("AddStory.500", "Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("AddStory.other","Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}
	
	
	public void uploadImage(){
		Log.e("uploadImageStory",Utility.uploadedImageAddStory.getAbsolutePath()+ "aa");
		Ion.with(getBaseContext()).load("http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/UploadPicture?func=story&storyId=" + story_id).progressDialog(prgDialog).setMultipartFile("file", Utility.uploadedImageAddStory).asJsonObject();
	//	deleteImage();
	}

	public void deleteImage(){
		Utility.uploadedImageAddStory.delete();
	}	
	
	public void showExpPoint() {

		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final TextView input = new TextView(this);
		alert.setMessage("You earn 10 points!");
		alert.setTitle("CONRATULATIONS"); 
		alert.setView(input);
		alert.setPositiveButton("Profile for experience point", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				navigateToProfileActivity(Utility.myUserName);
				dialog.dismiss();
			}
		});

		alert.setNegativeButton("Timeline for new story", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				navigateToTimelineActivityForMe();
				dialog.dismiss();
			}
		});
		alert.show();   

	}

	@Override
	public void onLocationChanged(Location location) {


	}

	public void navigateToTimelineActivityForMe(){
		Intent timelineIntent = new Intent(getApplicationContext(),TimelineActivity.class);
		Bundle b = new Bundle();
		b.putString("type","myStories");
		b.putString("user_name", Utility.myUserName);
		timelineIntent.putExtras(b);
		timelineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(timelineIntent);
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		inflater = getMenuInflater();
		inflater.inflate(R.menu.addstory_menu, menu);
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



				m.findItem(R.id.ownProfile);
				m.findItem(R.id.ownProfile).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				m.findItem(R.id.recommendation);
				m.findItem(R.id.recommendation).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				m.findItem(R.id.map);
				m.findItem(R.id.map).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				m.findItem(R.id.advancedSearch);
				m.findItem(R.id.advancedSearch).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);



				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {




				m.findItem(R.id.ownProfile);
				m.findItem(R.id.ownProfile).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


				m.findItem(R.id.recommendation);
				m.findItem(R.id.recommendation).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				m.findItem(R.id.map);
				m.findItem(R.id.map).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

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
	public void navigateToTimelineActivityForPlaces(String placeID){
		Intent timelineIntent = new Intent(getApplicationContext(),TimelineActivity.class);
		Bundle b = new Bundle();
		b.putString("type","placesStories");
		b.putString("placeID",placeID);
		timelineIntent.putExtras(b);
		timelineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(timelineIntent);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.search:			
			break;
		case R.id.ownProfile:
			navigateToProfileActivity(Utility.myUserName);
			break;
		case R.id.recommendation:
			navigateToRecommendationActivity();
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
