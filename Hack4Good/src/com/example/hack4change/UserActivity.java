package com.example.hack4change;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;

public class UserActivity extends Activity {

	Button getData;
	EditText userName;

	String[] details = { "Dummy user", "Dummy Status",
			"Dummy Events Participated in", "Dummy Change Meter" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);

		setContentView(R.layout.user_profile);

		// static final String EXTRA_USERNAME = "com.example.EXTRA_USERNAME";

		userName = (EditText) findViewById(R.id.editText1);
		final String un = userName.toString();

		getData = (Button) findViewById(R.id.btnGetData);

		getData.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new GetUserInfoTask().execute(un);

			}
		});

		/*
		 * Intent getUserInfo = new
		 * Intent(UserActivity.this,HttpActivity.class);
		 * getUserInfo.setAction(Intent.ACTION_SEND);
		 * getUserInfo.putExtra(Intent.EXTRA_TEXT, un);
		 * startActivity(getUserInfo);
		 */

	}

	@Override
	public View onCreateView(View parent, String name, Context context,
			AttributeSet attrs) {
		// TODO Auto-generated method stub
		// return super.onCreateView(parent, name, context, attrs);
		//
		// Inflater code here
		return super.onCreateView(parent, name, context, attrs);

	}

	public class GetUserInfoTask extends AsyncTask<String, Void, Void>

	{

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {
				userName = (EditText) findViewById(R.id.editText1);
				URL url = new URL("http://www.google.com/getUserInfo"
						+ userName);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				readStream(con.getInputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		private void readStream(InputStream in) {
			BufferedReader reader = null;

			try {
				reader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
				// showData(reader);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

	}

}
