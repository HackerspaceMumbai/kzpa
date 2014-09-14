package com.example.hack4change;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class CreateEventActivity extends Activity{
Button b1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_event);
		
		b1= (Button) findViewById(R.id.btnCreateEvent);
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				//Intent intent= new Intent(packageContext, cls)
			}
		});
		
	}
	
	
}
