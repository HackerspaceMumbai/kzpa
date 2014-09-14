package com.example.hack4good;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ShareActivity extends SherlockFragmentActivity {

	private FacebookFragment mainFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
			mainFragment = new FacebookFragment();
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, mainFragment).commit();
		} else {
			// Or set the fragment from restored state info
			mainFragment = (FacebookFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}
	}
}
