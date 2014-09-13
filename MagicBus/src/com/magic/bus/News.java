package com.magic.bus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class News extends Activity {
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);

		findViewById(R.id.card4).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(News.this, Graphs.class);
				startActivity(intent);
			}
		});
		
		findViewById(R.id.card5).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{   
				Intent intent = new Intent(News.this, Resume.class);
				startActivity(intent);
			}
		});
		
		findViewById(R.id.card6).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{    
				Intent intent = new Intent(News.this, Resume.class);
				startActivity(intent);
			}
		});
}
}
