package com.android.dutluk;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class MapActivity extends FragmentActivity implements LocationListener {
	
	ProgressDialog prgDialog;

	GoogleMap map;
	Context context = this; 


	String story ="";
	String tagsForStory = "";
	String time="";
	String image = "";
	 String latitude = "";
	 String longitude = "";
	String tagsForPlace = "";
	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.map);
	        // Instantiate Progress Dialog object
	 		prgDialog = new ProgressDialog(this);
	 		// Set Progress Dialog Text
		    prgDialog.setMessage("Please wait...");
		    // Set Cancelable as False
		    prgDialog.setCancelable(false);
	        Intent addStoryIntent = getIntent();
	        story = addStoryIntent.getStringExtra("story");
	        tagsForStory = addStoryIntent.getStringExtra("tagsForStory");
	        time = addStoryIntent.getStringExtra("time");
	        image = addStoryIntent.getStringExtra("image");
	        

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
					        //Toast.makeText(getApplicationContext(), "latitude is: " + latlng.latitude, Toast.LENGTH_LONG).show();
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
				Log.e("submit", response);
				prgDialog.hide();
				try {
					JSONArray jsonarray = new JSONArray(response);


					for(int i=0; i<jsonarray.length(); i++){
						JSONObject obj = jsonarray.getJSONObject(i);

						String name = obj.getString("Name");
						String placeID = obj.getString("PlaceID");
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
										        //Toast.makeText(getApplicationContext(), "latitude is: " + latlng.latitude, Toast.LENGTH_LONG).show();
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
					
					
				} catch (JSONException e) {
				
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



	    
  
	public void saveStory(){
		RequestParams params = new RequestParams();
		params.put("mail",Utility.userName);
		// düzelt:)
		//params.put("image", image);
		params.put("story", story);
		params.put("storyTag", tagsForStory);
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
                    	 navigateToTimelineActivityForMe();
                      	 // Display successfully registered message using Toast
                      	 Toast.makeText(getApplicationContext(), "You are successfully add new story!", Toast.LENGTH_LONG).show();
                      	// refresh map burayý düzelt!
                       } 
                       // Else display error message
                       else{
//                      	 errorMsg.setText(obj.getString("error_msg"));
//                      	 Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
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
	
	@Override
	public void onLocationChanged(Location location) {
		
		
	}
	public void navigateToTimelineActivityForMe(){
		Intent timelineIntent = new Intent(getApplicationContext(),TimelineActivity.class);
		Bundle b = new Bundle();
		b.putString("type","myStories");
		timelineIntent.putExtras(b);
		timelineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(timelineIntent);
	}
	
	
}
