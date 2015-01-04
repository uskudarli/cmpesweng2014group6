package com.android.dutluk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class ProfileActivity extends Activity  implements OnItemSelectedListener{
	ActionBar actionBar;
	ProgressDialog prgDialog;

	ImageView imageV;
	EditText nameET;
	TextView birthDateET;

	TextView mailTV;
	EditText phoneET;
	TextView xpTV;
	TextView levelTV;
	EditText bioET;
	
	ImageButton pickTime;

	int year;
	int month;
	int day;
	static final int DATE_DIALOG_ID = 100;
	
	private final int SELECT_FILE = 1;
	private final int REQUEST_CAMERA = 0;
	private Spinner genderSpinner;
	ArrayList<String> genderList = new ArrayList<String>();
	String gender = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
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
         
		imageV = (ImageView)findViewById(R.id.imageViewProfile);
     	nameET = (EditText)findViewById(R.id.nameProfile);
     	birthDateET = (TextView)findViewById(R.id.birthDateProfile);
     	addTimeButtonListener();
     	mailTV =  (TextView)findViewById(R.id.emailProfile);
     	phoneET = (EditText)findViewById(R.id.phoneProfile);
     	xpTV = (TextView)findViewById(R.id.xpProfile);
     	levelTV = (TextView)findViewById(R.id.levelProfile);
     	bioET = (EditText)findViewById(R.id.biographyProfile);
     	
     	RequestParams params = new RequestParams();
		params.put("email", Utility.myUserName);
		invokeWSforGET(params);
		
		genderSpinner = (Spinner)findViewById(R.id.spinnerGender);
		ArrayAdapter<String>adapter = new ArrayAdapter<String>(ProfileActivity.this,
				android.R.layout.simple_spinner_item,genderList);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		
		genderList.add("Female");
		genderList.add("Male");
		genderList.add("Unspecified");
		    
		genderSpinner.setAdapter(adapter);
		genderSpinner.setSelection(0);
		genderSpinner.setOnItemSelectedListener(this);

    }
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
    	

		gender = genderList.get(position);
		Log.e("GENDER", "a " + gender );
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	public void invokeWSforGET(RequestParams params){
		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
        client.post(Utility.SERVER_NAME + "GetProfile",params ,new AsyncHttpResponseHandler() {
        	// When the response returned by REST has Http response code '200'
             @Override
             public void onSuccess(String response) {
            	// Hide Progress Dialog
            	 prgDialog.hide();
            	 
                 try {
                	 	 // JSON Object
                     
                         JSONObject obj = new JSONObject(response);
                        
                         // When the JSON response has status boolean value assigned with true
                         if (!obj.getString("Name").equals("")){
                         //if(obj.getBoolean("result")){
                        	 // Set Default Values for Edit View controls
                        	
                        	 setDefaultValues(obj);
                        	 // Display successfully registered message using Toast
                        	 Toast.makeText(getApplicationContext(), "You can update your profile!", Toast.LENGTH_LONG).show();
                         } 
                         // Else display error message
                         else{
//                        	 errorMsg.setText(obj.getString("message"));
//                        	 Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                         }
                 } catch (JSONException e) {
                     // TODO Auto-generated catch block
                     Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
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
                     Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                 } 
                 // When Http response code is '500'
                 else if(statusCode == 500){
                     Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                 } 
                 // When Http response code other than 404, 500
                 else{
                     Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                 }
             }
         });
	}
	/**
	 * Set default values for Edit View controls
	 * @throws JSONException 
	 */
	public void setDefaultValues(JSONObject obj) throws JSONException{
		
		nameET.setText(obj.getString("Name"));
		mailTV.setText(obj.getString("Email"));
		xpTV.setText(obj.getString("ExperiencePoint"));
		levelTV.setText(obj.getString("Level"));
		phoneET.setText(obj.getString("Phone"));
     	birthDateET.setText(obj.getString("Birthdate"));
     	bioET.setText(obj.getString("Bio"));
     	String temp_gender = obj.getString("gender");
     	int gender_index  = genderList.indexOf(temp_gender);
     	genderSpinner.setSelection(gender_index);
     
		// BURDA IMAGE KALDI
	}
	

	public void saveUser(View view){

		String name = nameET.getText().toString();
		String birthDate = birthDateET.getText().toString();
		
		String phone = phoneET.getText().toString();
		String bio = bioET.getText().toString();

		RequestParams params = new RequestParams();
		
		params.put("name", name);
		params.put("email", Utility.myUserName);
		params.put("phone", phone);
		params.put("birthDate", birthDate);
		params.put("bio", bio);	
		params.put("gender", gender);			
		invokeWSforSAVE(params);

		
	}    



	public void invokeWSforSAVE(RequestParams params){
		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
        client.post(Utility.SERVER_NAME + "updateProfile",params ,new AsyncHttpResponseHandler() {
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
                        	 //setDefaultValues(obj);
                        	 // Display successfully registered message using Toast
                        	 Toast.makeText(getApplicationContext(), "You are successfully updated your profile!", Toast.LENGTH_LONG).show();
                         } 
                         // Else display error message
                         else{
                        	
                        	 Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                         }
                 } catch (JSONException e) {
                     // TODO Auto-generated catch block
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
	public void navigateToTimelineActivityForMe(View view){
		Intent timelineIntent = new Intent(getApplicationContext(),TimelineActivity.class);
		Bundle b = new Bundle();
		b.putString("type","myStories");
		timelineIntent.putExtras(b);
		timelineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(timelineIntent);
	}
	
	public void addTimeButtonListener() {
		final Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		pickTime = (ImageButton)findViewById(R.id.birthDateButtonProfile);

		pickTime.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID);

			}

		});

	}
	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// set selected date into Text View
			birthDateET.setText(new StringBuilder().append(month + 1)
					.append("/").append(day).append("/").append(year).append(" "));


		}
	};
	public void goImageSelector(View view) {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
		"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment
							.getExternalStorageDirectory(), "temp.jpg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, REQUEST_CAMERA);
				} else if (items[item].equals("Choose from Library")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							SELECT_FILE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				File f = new File(Environment.getExternalStorageDirectory()
						.toString());
				for (File temp : f.listFiles()) {
					if (temp.getName().equals("temp.jpg")) {
						f = temp;
						break;
					}
				}
				try {
					Bitmap bm;
					BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

					bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
							btmapOptions);

					// bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
					imageV.setImageBitmap(bm);

					String path = android.os.Environment
							.getExternalStorageDirectory()
							+ File.separator
							+ "Phoenix" + File.separator + "default";
					f.delete();
					OutputStream fOut = null;
					File file = new File(path, String.valueOf(System
							.currentTimeMillis()) + ".jpg");
					
				
					try {
						fOut = new FileOutputStream(file);
						bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
						fOut.flush();
						fOut.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == SELECT_FILE) {
				Uri selectedImageUri = data.getData();
				String tempPath = getPath(selectedImageUri, ProfileActivity.this);
				Bitmap bm;
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
				bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
				imageV.setImageBitmap(bm);
			}
		}
	}
	public String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = activity
		.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

}
