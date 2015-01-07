package com.android.dutluk;

import java.io.File;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


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
import android.view.MenuItem.OnActionExpandListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;





public class AddStoryActivity extends Activity implements OnItemSelectedListener {
	ActionBar actionBar;
	ProgressDialog prgDialog;
	boolean imageCheck = false;

	ImageView imageV;
	EditText storyET;
	EditText tagsET;
	TextView placeET;
	EditText timeET;
	ImageButton pickTime;

	int year;
	int month;
	int day;
	static final int DATE_DIALOG_ID = 100;


	String story ="";
	String tags= "";
	String time="";
	//String uploadedPath = "";
	String themeID = "";
	private final int SELECT_FILE = 1;
	private final int REQUEST_CAMERA = 0;



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

	private Spinner spinner;
	ArrayList<String> themesList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_story);
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

	

		imageV = (ImageView)findViewById(R.id.imageViewAddStory);
		storyET =  (EditText)findViewById(R.id.storyAddStory);
		tagsET = (EditText) findViewById(R.id.storyTagsAddStory);

		Intent comingIntent = getIntent();
		tags = comingIntent.getStringExtra("tags");
		if (!tags.equals(""))
			tagsET.setText(tags);

		timeET = (EditText)findViewById(R.id.timeAddStory);
		addTimeButtonListener();

		RequestParams params = new RequestParams();
		invokeWS(params);


		spinner = (Spinner)findViewById(R.id.spinnerAddStory);
		ArrayAdapter<String>adapter = new ArrayAdapter<String>(AddStoryActivity.this,
				android.R.layout.simple_spinner_item,themesList);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		themesList.add("Select Theme");

		spinner.setAdapter(adapter);
		spinner.setSelection(0);
		spinner.setOnItemSelectedListener(this);


	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

		int temp = position + 1;
		themeID = temp + "";
		Log.e("THEME", themeID);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}


	public void invokeWS(RequestParams params){
		prgDialog.show();
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Utility.SERVER_NAME + "GetThemes" , params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String response) {
			
				prgDialog.hide();

				try {
					JSONArray jsonarray = new JSONArray(response);
					
					int size = jsonarray.length();
					for(int i=0; i<size; i++){
						themesList.add("");
					}

					for(int i=0; i<size; i++){
						JSONObject obj = jsonarray.getJSONObject(i);

						String theme = obj.getString("Name");
						int themeID = Integer.parseInt(obj.getString("themeID"));
						themesList.set(themeID, theme);

					}

					Log.e("invokeWSGetThemes.success", response);
				} catch (JSONException e) {
					Log.e("invokeWSGetThemes.json", e.toString());
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
					Log.e("invokeWSGetThemes.404", "Requested resource not found");
				} 
				// When HTTP response code is '500'
				else if(statusCode == 500){
					Log.e("invokeWSGetThemes.500", "Something went wrong at server end");
				} 
				// When HTTP response code other than 404, 500
				else{
					Log.e("invokeWSGetThemes.other", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}

	public void goImageSelector(View view) {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
		"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(AddStoryActivity.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


					
					Utility.uploadedImageAddStoryPath = Environment.getExternalStorageDirectory().toString() + "/" +  String.valueOf(System.currentTimeMillis()) + ".jpg";
					
					
					File f = new File(Utility.uploadedImageAddStoryPath);
					
					Uri imageUri = Uri.fromFile(f);
					
					intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
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

				Utility.uploadedImageAddStory = new File(Utility.uploadedImageAddStoryPath);

				Bitmap bm;
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
				bm = BitmapFactory.decodeFile(Utility.uploadedImageAddStoryPath,btmapOptions);
				imageV.setImageBitmap(bm);
				imageCheck = true;

			} else if (requestCode == SELECT_FILE) {
				Uri selectedImageUri = data.getData();
				String tempPath = getPath(selectedImageUri, AddStoryActivity.this);
				Utility.uploadedImageAddStory = new File(tempPath);

				Bitmap bm;
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
				bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
				imageV.setImageBitmap(bm);
				imageCheck = true;
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


	public void navigatetoMapActivity(View view){
		story = storyET.getText().toString();
		tags = tagsET.getText().toString();
		time = timeET.getText().toString();
		// düzelt:)
		Intent MapIntent = new Intent(getApplicationContext(),MapActivity.class);
		Bundle b = new Bundle();
		b.putString("story",story);
		b.putString("tagsForStory",tags);
		b.putString("themeId",themeID+ "");
		b.putString("time",time);
		b.putBoolean("imageCheck",imageCheck);

		MapIntent.putExtras(b);
		MapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(MapIntent);
	}

	public void addTimeButtonListener() {
		final Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		pickTime = (ImageButton)findViewById(R.id.timeButtonAddStory);

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
			timeET.setText(new StringBuilder().append(month + 1)
					.append("-").append(day).append("-").append(year).append(" "));



		}
	};


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
		inflater.inflate(R.menu.addstory_menu, menu);
		final android.widget.SearchView searchView = (android.widget.SearchView) menu
				.findItem(R.id.search).getActionView();
		searchView.setSubmitButtonEnabled(true);
		
			searchView.setQueryHint("Search Story");
	
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




