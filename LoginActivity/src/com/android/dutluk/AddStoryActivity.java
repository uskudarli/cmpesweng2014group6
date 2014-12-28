package com.android.dutluk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
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

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;




public class AddStoryActivity extends Activity implements OnItemSelectedListener {

	ProgressDialog prgDialog;
	TextView errorMsg;

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
	String image = "";
	String themeID = "";
	private final int SELECT_FILE = 1;
	private final int REQUEST_CAMERA = 0;

	private Spinner spinner;
	ArrayList<String> themesList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_story);

		errorMsg = (TextView)findViewById(R.id.errorAddStory);
		// Instantiate Progress Dialog object
		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
		prgDialog.setMessage("Please wait...");
		// Set Cancelable as False
		prgDialog.setCancelable(false);


		imageV = (ImageView)findViewById(R.id.imageViewAddStory);
		storyET =  (EditText)findViewById(R.id.storyAddStory);
		tagsET = (EditText) findViewById(R.id.storyTagsAddStory);
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
				Log.e("submit", response);
				prgDialog.hide();
				
				try {
					JSONArray jsonarray = new JSONArray(response);
					themesList.clear();
					int size = jsonarray.length();
					for(int i=0; i<size; i++){
						themesList.add("");
					}
					
					for(int i=0; i<size; i++){
						JSONObject obj = jsonarray.getJSONObject(i);

						String theme = obj.getString("Name");
						int themeID = Integer.parseInt(obj.getString("themeID"));
						themesList.set(themeID-1, theme);

					}
				
					
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

				String tempPath = getPath(selectedImageUri, AddStoryActivity.this);
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
	public void navigatetoMapActivity(View view){
		story = storyET.getText().toString();
		tags = tagsET.getText().toString();
		time = timeET.getText().toString();
		// düzelt:)
		image = "";
		Intent MapIntent = new Intent(getApplicationContext(),MapActivity.class);
		Bundle b = new Bundle();
		b.putString("story",story);
		b.putString("tagsForStory",tags);
		b.putString("themeId",themeID+ "");
		b.putString("time",time);
		b.putString("image",image);
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

}




