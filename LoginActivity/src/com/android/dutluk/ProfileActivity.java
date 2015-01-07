package com.android.dutluk;


import java.io.File;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.annotation.TargetApi;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import android.widget.AdapterView.OnItemSelectedListener;

import com.koushikdutta.ion.Ion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class ProfileActivity extends Activity  implements OnItemSelectedListener{
	ActionBar actionBar;
	ProgressDialog prgDialog;
	MenuInflater inflater;
	Menu m;


	ImageView imageV;
	EditText nameET;
	TextView birthDateTV;
	TextView mailTV;
	EditText phoneET;
	TextView xpTV;
	TextView levelTV;
	TextView subsTV;
	EditText bioET;
	File uploadedImage ;
	String uploadedImagePath;
	ImageButton pickTime;
	ImageButton addPhoto;
	Button save;
	
	
	private final int SELECT_FILE = 1;
	private final int REQUEST_CAMERA = 0;
	boolean imageCheck = false;
	
	int year;
	int month;
	int day;
	static final int DATE_DIALOG_ID = 100;
	

	private Spinner genderSpinner;
	ArrayList<String> genderList = new ArrayList<String>();
	
	
	String gender = "";
	String user_name = "";
	String user_id = "";
	String who = "";
	
	Button myStories ;
	Button subsStories;
	
	
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

		Intent comingIntent = getIntent();
		user_name = comingIntent.getStringExtra("user_name");


		Log.e("user_name", user_name+ "aa");
		if(user_name.equals(Utility.myUserName)) {

			user_id = Utility.myUserID;
			who = "me";
		}
		else {
			Utility.IDFromName(user_name);
			user_id = Utility.userIDFromName;
			who = "other";
		}
		save = (Button) findViewById(R.id.btnSaveProfile);
		myStories = (Button) findViewById(R.id.btnShowMyStoriesProfile);
		subsStories = (Button) findViewById(R.id.btnShowSubscribersStoriesProfile);
		


		imageV = (ImageView)findViewById(R.id.imageViewProfile);
		addPhoto = (ImageButton)findViewById(R.id.imageButtonProfile);
		nameET = (EditText)findViewById(R.id.nameProfile);
		birthDateTV = (TextView)findViewById(R.id.birthDateProfile);
		addTimeButtonListener();
		mailTV =  (TextView)findViewById(R.id.emailProfile);

		phoneET = (EditText)findViewById(R.id.phoneProfile);
		xpTV = (TextView)findViewById(R.id.xpProfile);
		levelTV = (TextView)findViewById(R.id.levelProfile);
		subsTV = (TextView)findViewById(R.id.subscriberCountProfile);
		bioET = (EditText)findViewById(R.id.biographyProfile);
		GetProfilePicture();

		invokeWSforGET();

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
	public void invokeWSforGET(){

		RequestParams params = new RequestParams();

		params.put("email", user_name);
		Log.e("Utility.user_name",user_name+ "aa");





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
						// Display successfully registered message 
						Log.e("GetProfile.success", "You can update your profile!");
					} 
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e("GetProfile.json", "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("GetProfile.404", "Requested resource not found");
				} 
				// When Http response code is '500'
				else if(statusCode == 500){
					Log.e("GetProfile.500", "Something went wrong at server end");
				} 
				// When Http response code other than 404, 500
				else{
					Log.e("GetProfile.other", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}
	/**
	 * Set default values for Edit View controls
	 * @throws JSONException 
	 */
	public void setDefaultValues(JSONObject obj) throws JSONException{
		String name = obj.getString("Name");
		String temp_gender = obj.getString("gender");

		if(who.equals("me")) {


		}
		else {


			nameET.setKeyListener(null);
			phoneET.setKeyListener(null);
			bioET.setKeyListener(null);
			pickTime.setVisibility(View.INVISIBLE);
			addPhoto.setVisibility(View.INVISIBLE);
			save.setVisibility(View.INVISIBLE);
			myStories.setText("Show "+name+"'s Stories");
			subsStories.setText("Show "+name+"'s Subscribed Stories");
			genderSpinner.setEnabled(false);
		}
		int gender_index  = genderList.indexOf(temp_gender);
		genderSpinner.setSelection(gender_index);
	
		nameET.setText(name);
		mailTV.setText(obj.getString("Email"));
		xpTV.setText(obj.getString("ExperiencePoint"));
		levelTV.setText(obj.getString("Level"));
		subsTV.setText(obj.getString("followerNumber"));
		phoneET.setText(obj.getString("Phone"));
		birthDateTV.setText(obj.getString("Birthdate"));
		bioET.setText(obj.getString("Bio"));



		// BURDA IMAGE KALDI
	}


	public void saveUser(View view){

		String name = nameET.getText().toString();
		String birthDate = birthDateTV.getText().toString();

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
		if(imageCheck) 
			uploadImage();

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

						// Display successfully registered message 
						Log.e("updateProfile.success", "You are successfully updated your profile!");
					} 
					// Else display error message
					else{

						Log.e("updateProfile.error", obj.getString("message"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e("updateProfile.json", "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("updateProfile.404", "Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){
					Log.e("updateProfile.500", "Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("updateProfile.other", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}
	public void navigateToTimelineActivityForMe(View view){
		Intent timelineIntent = new Intent(getApplicationContext(),TimelineActivity.class);
		Bundle b = new Bundle();
		b.putString("type","myStories");
		b.putString("user_name", user_name);
		timelineIntent.putExtras(b);
		timelineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(timelineIntent);
	}
	public void navigateToTimelineActivityForSubs(View view){
		Intent timelineIntent = new Intent(getApplicationContext(),TimelineActivity.class);
		Bundle b = new Bundle();
		b.putString("type", "subsStories");
		b.putString("user_name", user_name);
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
			birthDateTV.setText(new StringBuilder().append(month + 1)
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
					
					
					
					uploadedImagePath = Environment.getExternalStorageDirectory().toString() + "/" +  String.valueOf(System.currentTimeMillis()) + ".jpg";
					
					
					File f = new File(uploadedImagePath);
					
					Uri imageUri = Uri.fromFile(f);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
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

				uploadedImage = new File(uploadedImagePath);
				
				Bitmap bm;
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
				bm = BitmapFactory.decodeFile(uploadedImagePath,btmapOptions);
				imageV.setImageBitmap(bm);
				imageCheck = true;

			} else if (requestCode == SELECT_FILE) {
				Uri selectedImageUri = data.getData();
				String tempPath = getPath(selectedImageUri, ProfileActivity.this);
				uploadedImage = new File(tempPath);

				Bitmap bm;
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
				bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
				imageV.setImageBitmap(bm);
				imageCheck = true;
			}
		}
	}
	public void uploadImage(){
		Log.e("uploadImageProfile",uploadedImage.getAbsolutePath()+ "aa");
		Ion.with(getBaseContext()).load("http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/UploadPicture?func=profile&userId=" + Utility.myUserID).progressDialog(prgDialog).setMultipartFile("file", uploadedImage).asJsonObject();
		//deleteImage();
	}
	public void deleteImage() {
		uploadedImage.delete();
	}
	public void GetProfilePicture(){
		//http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/GetStoryPicture?storyId=200(GET)
		RequestParams params = new RequestParams();
		params.put("userId", user_id);

		Log.e("user_id", user_id +  " aaaaaaaa " );



		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Utility.SERVER_NAME+ "GetProfilePicture?",params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					// JSON Object
					JSONObject obj = new JSONObject(response);
					boolean check = obj.getBoolean("result");
					if (check){
						String path =  obj.getString("message");
						Log.e("PATH", path);

						
						Ion.with(ProfileActivity.this)
						.load(path)
						.withBitmap()
						.placeholder(R.drawable.ic_launcher)
						.error(R.drawable.ic_launcher)						
						.intoImageView(imageV);

					}




				} catch (JSONException e) {

					Log.e("getThemeName.json", "Error Occured [Server's JSON response might be invalid]!");
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
					Log.e("getThemeName.404", "Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){
					Log.e("getThemeName.500", "Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("getThemeName.other", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
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
