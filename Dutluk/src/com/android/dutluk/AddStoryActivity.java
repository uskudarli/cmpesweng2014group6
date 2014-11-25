package com.android.dutluk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class AddStoryActivity extends Activity {

	ProgressDialog prgDialog;
	TextView errorMsg;
	
	EditText titleET;
	ImageView imageV;
	EditText storyET;
	EditText placeET;
	EditText timeET;


	String mail = "";
	
	private final int SELECT_FILE = 1;
	private final int REQUEST_CAMERA = 0;
//	private ImageButton mapButton;
//	private ImageButton imageButton;
	
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
         
     	titleET = (EditText)findViewById(R.id.titleAddStory);
     	imageV = (ImageView)findViewById(R.id.imageViewAddStory);
     	storyET =  (EditText)findViewById(R.id.storyAddStory);
     	placeET =  (EditText)findViewById(R.id.placeAddStory);
     	timeET = (EditText)findViewById(R.id.timeAddStory);
//     	mapButton = (ImageButton)findViewById(R.id.mapButtonAddStory);
//     	imageButton = (ImageButton)findViewById(R.id.imageButtonAddStory);
        
        Intent loginIntent = getIntent();
        mail = loginIntent.getStringExtra("mail");
    }
    // düzelt:)
	public void saveStory(View view){

		String title = titleET.getText().toString();
		//String image ; //BURASI??
		String story = storyET.getText().toString();
		String place = placeET.getText().toString();
		String time = timeET.getText().toString();

		RequestParams params = new RequestParams();
		params.put("mail",mail);
		params.put("title", title);
		//params.put("image", image);
		params.put("story", story);
		params.put("place", place);
		params.put("time", time);				
		
	
		invokeWSforSAVE(params);
		
		//GEREKSIZ
		String id = "";
		 startShowStoryActivity(mail,id);
	}    

	public void invokeWSforSAVE(RequestParams params){
		// Show Progress Dialog 
		prgDialog.show();
		// Make RESTful web service call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.43.17:9999/addStory",params ,new AsyncHttpResponseHandler() {
        	// When the response returned by REST has Http response code '200'
             @Override
             public void onSuccess(String response) {
            	// Hide Progress Dialog
            	 prgDialog.hide();
                 try {
                	 	 // JSON Object
                         JSONObject obj = new JSONObject(response);
                         // When the JSON response has status boolean value assigned with true
                         if(obj.getBoolean("status")){
                        	 // Set Default Values for Edit View controls
                        	 
                        	 //düzelt:)
                        	 String id = "";
                        	 startShowStoryActivity(mail,id);
                        	 
                        	 
                        	 
                        	 
                        	 // Display successfully registered message using Toast
                        	 Toast.makeText(getApplicationContext(), "You are successfully add new story!", Toast.LENGTH_LONG).show();
                         } 
                         // Else display error message
                         else{
                        	 errorMsg.setText(obj.getString("error_msg"));
                        	 Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
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
	
	// düzelt:) 
	public void goMapActivity(View view){
//		Intent mapIntent = new Intent(getApplicationContext(),MapActivity.class);
//		mapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(mapIntent);
	}
	public void  startShowStoryActivity(String mail, String story_id) {
		Log.e("geldin", "show");
		Intent showStoryIntent = new Intent(getApplicationContext(),ShowStoryActivity.class);
		Bundle b = new Bundle();
		b.putString("mail",mail);
		b.putString("mail",story_id);
		showStoryIntent.putExtras(b);
		// Clears History of Activity
		showStoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(showStoryIntent);
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
	
}


