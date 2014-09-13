package com.magic.bus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Resume extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume);
		
		findViewById(R.id.back).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{    
				Intent intent = new Intent(Resume.this, HelloCardActivity.class);
				startActivity(intent);
			}
		});
	}

}
