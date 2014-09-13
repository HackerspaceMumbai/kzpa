package com.magic.bus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HelloCardActivity extends Activity
{
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hellocard);

		findViewById(R.id.carddonation).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(HelloCardActivity.this, Donation.class);
				startActivity(intent);
			}
		});
		
		findViewById(R.id.cardnews).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(HelloCardActivity.this, News.class);
				startActivity(intent);
			}
		});
		
		findViewById(R.id.cardabout).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(HelloCardActivity.this, Aboutus.class);
				startActivity(intent);
			}
		});
	}
}
