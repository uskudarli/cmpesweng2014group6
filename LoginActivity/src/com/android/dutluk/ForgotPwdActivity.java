package com.android.dutluk;

import java.util.Properties;
import java.util.Random;



import android.app.ActionBar;
import android.app.Activity;

import android.app.ProgressDialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;


import android.os.Bundle;


import android.view.View;
import android.widget.EditText;

import android.widget.Toast;



import javax.mail.*;
import javax.mail.internet.*;


public class ForgotPwdActivity extends Activity {
	ActionBar actionBar;
	ProgressDialog prgDialog;


	EditText emailET;

	String mail = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgotpwd);
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

		emailET = (EditText)findViewById(R.id.emailForgotpwd);



	}

	public void resetPassword(View view){
		// Get Email Edit View Value
		mail = emailET.getText().toString();
		// Instantiate Http Request Param Object

		// When Email Edit View and Password Edit View have values other than Null
		if(Utility.isNotNull(mail)){
			// When Email entered is Valid
			if(Utility.validateEmail(mail)){
				
				Thread thread = new Thread(new Runnable(){
					@Override
					public void run() {
						
						try {
							String newPassword = generatePassword();
							//send a new random password with an e-mail to user
							Properties props = new Properties();
							props.put("mail.smtp.host", "smtp.gmail.com");
							props.put("mail.smtp.socketFactory.port", "465");
							props.put("mail.smtp.socketFactory.class",
									"javax.net.ssl.SSLSocketFactory");
							props.put("mail.smtp.auth", "true");
							props.put("mail.smtp.port", "465");

							Session session = Session.getDefaultInstance(props,
									new javax.mail.Authenticator() {
								protected PasswordAuthentication getPasswordAuthentication() {
									return new PasswordAuthentication("dutluk.group6@gmail.com","cmpesweng2014");
								}
							});

							try {

								Message message = new MimeMessage(session);
								message.setFrom(new InternetAddress("DUTLUK"));
								message.setRecipients(Message.RecipientType.TO,
										InternetAddress.parse(mail));
								message.setSubject("Your new password on Dutluk");
								message.setText("Dear Dutluk user, \n\nYour new password is: "+newPassword+". Don't forget to change your password after log in. \n\nDutluk Team");

								Transport.send(message);
								navigatetoLoginActivity();
								

							} catch (MessagingException e) {
								throw new RuntimeException(e);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				});
			

				thread.start(); 
			} 
			// When Email is invalid
			else{
				Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
			}
		} 
		// When any of the Edit View control left blank
		else{
			Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
		}

	}



	public static String generatePassword()
	{
		Random rn = new Random();
		char[] text = new char[8];
		for (int i = 0; i < 8; i++)
		{
			text[i] = "0123456789abcdefghjklmn".charAt(rn.nextInt("0123456789abcdefghjklmn".length()));
		}
		return new String(text);
	}
	public void navigatetoLoginActivity(){
		Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
		Utility.myUserName = mail;
		Utility.IDFromName(mail);
		Utility.myUserID = Utility.userIDFromName;
		loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(loginIntent);
		Utility.isReseted = true;
	}


}
