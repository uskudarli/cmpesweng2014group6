package com.android.dutluk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


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

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;




public class AddStoryActivity extends Activity {

	ProgressDialog prgDialog;
	TextView errorMsg;
	
	ImageView imageV;
	EditText storyET;
	EditText tagsET;
	TextView placeET;
	EditText timeET;


	String story ="";
	String tags= "";
	String time="";
	String image = "";
	private final int SELECT_FILE = 1;
	private final int REQUEST_CAMERA = 0;

	
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
		b.putString("time",time);
		b.putString("image",image);
		MapIntent.putExtras(b);
		MapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(MapIntent);
	}
	
}


