package com.magic.bus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Donation extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donation);
		findViewById(R.id.bdonate).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{    
				Intent intent = new Intent(Donation.this, HelloCardActivity.class);
				startActivity(intent);
			}
		});
	}

}
