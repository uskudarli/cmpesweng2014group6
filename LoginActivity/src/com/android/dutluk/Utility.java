package com.android.dutluk;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * Class which has Utility methods
 * 
 */
public class Utility {

	public static String myUserName = "" ;
	public static String myUserID = "";
	public static String [] userList;
	public static String [] storyTagsList;
	public static String [] placeTagsList;
	public static String userNameFromID= "";
	public static String userIDFromName= "";
	public static boolean isReseted = false;

	public static final String SERVER_NAME = "http://titan.cmpe.boun.edu.tr:8085/dutluk_android_api/";


	private static Pattern pattern;
	private static Matcher matcher;
	//public static Map<Integer,ArrayList<Double>> multiMap = new HashMap<Integer,ArrayList<Double>>();

	//Email Pattern
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * Validate Email with regular expression
	 * 
	 * @param email
	 * @return true for Valid Email and false for Invalid Email
	 */
	public static boolean validateEmail(String email) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();

	}
	/**
	 * Checks for Null String object
	 * 
	 * @param txt
	 * @return true for not null and false for null String object
	 */
	public static boolean isNotNull(String txt){
		return txt!=null && txt.trim().length()>0 ? true: false;
	}

	public static void nameFromID(String id) {
		RequestParams params = new RequestParams();
		params.put("userId", id);
		AsyncHttpClient client = new AsyncHttpClient();		 

		client.get(Utility.SERVER_NAME + "GetUserMail?", params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			

			@Override
			public void onSuccess(String response) {

				try {
					// JSON Object
					JSONObject obj = new JSONObject(response);
					Log.e("UTILITY.nameFromID", obj.toString());
					// When the JSON response has status boolean value assigned with true
					String jsonStr =  obj.getString("Email");
					userNameFromID = jsonStr;

				} catch (JSONException e) {

					e.printStackTrace();
					Log.e("UTILITY.nameFromID", e.toString());
				}
			}
			// When the response returned by REST has Http response code other than '200'
			@Override
			public void onFailure(int statusCode, Throwable error,
					String content) {
				// When Http response code is '404'
				if(statusCode == 404){
					Log.e("UTILITY.nameFromID", "Requested resource not found");
				}                 
				// When Http response code is '500'
				else if(statusCode == 500){
					Log.e("UTILITY.nameFromID", "Something went wrong at server end");
				} 
				// When Http response code other than 404, 500
				else{
					Log.e("UTILITY.nameFromID", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}
	public static void IDFromName(final String mail) {
		RequestParams params = new RequestParams();
		params.put("email", mail);
		AsyncHttpClient client = new AsyncHttpClient();		 

		client.get(Utility.SERVER_NAME + "GetUserId?", params ,new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			

			@Override
			public void onSuccess(String response) {

				try {
					// JSON Object
					JSONObject obj = new JSONObject(response);
					Log.e("UTILITY.IDFromName", obj.toString());
					// When the JSON response has status boolean value assigned with true
					String jsonStr =  obj.getString("UserId");
					userIDFromName = jsonStr;
					Log.e("Utility-userIDFromName-mail", userIDFromName + "aa" + mail);
					if(mail.equals(myUserName)){
						myUserID = jsonStr;
						Log.e("Utility-myUserID-myUserName", myUserID + "aa" + myUserName);
					}
					

				} catch (JSONException e) {

					e.printStackTrace();
					Log.e("UTILITY.IDFromName", e.toString());
				}
			}
			// When the response returned by REST has Http response code other than '200'
			@Override
			public void onFailure(int statusCode, Throwable error,
					String content) {
				// When Http response code is '404'
				if(statusCode == 404){
					Log.e("UTILITY.IDFromName", "Requested resource not found");
				}                 
				// When Http response code is '500'
				else if(statusCode == 500){
					Log.e("UTILITY.IDFromName", "Something went wrong at server end");
				} 
				// When Http response code other than 404, 500
				else{
					Log.e("UTILITY.IDFromName", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
				}
			}
		});
	}
}
