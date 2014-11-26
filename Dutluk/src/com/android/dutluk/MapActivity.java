package com.android.dutluk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements LocationListener {
	GoogleMap map;
	Context context = this; 
	
	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.map);
	        
	        map = ((MapFragment) getFragmentManager()
	                .findFragmentById(R.id.gmap)).getMap();
	        map.setMyLocationEnabled(true);
	        
	        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
				
				
				public void onMapLongClick(final LatLng latlng) {
					LayoutInflater li = LayoutInflater.from(context);
					final View v = li.inflate(R.layout.map2, null);
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setView(v);
					builder.setCancelable(false);
					
					builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
						
						
						public void onClick(DialogInterface dialog, int which) {
							EditText title = (EditText) v.findViewById(R.id.ettitle);
							EditText snippet = (EditText) v.findViewById(R.id.etsnippet);
							
					        map.addMarker(new MarkerOptions()
			                .title(title.getText().toString())
			                .snippet(snippet.getText().toString())
		                    .position(latlng)        
					        );
					        
					        
					        Toast.makeText(getApplicationContext(), "latitude is: " + latlng.latitude, Toast.LENGTH_LONG).show();
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

//	        LatLng sydney = new LatLng(-33.867, 151.206);
//
//	        map.setMyLocationEnabled(true);
//	        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
//
//	        map.addMarker(new MarkerOptions()
//	                .title("Sydney")
//	                .snippet("The most populous city in Australia.")
//	                .position(sydney));

	    

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}
	 
	
	 

}
