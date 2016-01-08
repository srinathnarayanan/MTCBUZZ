package com.example.mtcbuzz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.android.maps.GeoPoint;
import com.myfirstapp.cardsui.Card;
import com.myfirstapp.cardsui.Card.OnCardSwiped;
import com.myfirstapp.cardsui.CardUI;
import com.myfirstapp.cardsui.SwipeDismissTouchListener;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;


public class AlarmActivity extends Activity {
	Double latitude;
	Double longitude;
	String alarm;
	LocationManager lm;
	LocationListener locationListener;
	String senderTel;
	        
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		
		
		DBAdapter db=new DBAdapter(this);
		
		db.open();
		final Intent intent=getIntent();
		alarm=intent.getStringExtra(m.alarm);
		 latitude=Double.parseDouble(intent.getStringExtra(m.lati));
		 longitude=Double.parseDouble(intent.getStringExtra(m.longi));
		
		 if(db.notPresent(alarm))
		 {
		long a=db.addAlarm(alarm);
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Intent intent1 = new Intent("com.example.mtcbuzz.AlarmReceiverActivity");
		
        intent1.putExtra("name", alarm);   
         
		PendingIntent proximityIntent = PendingIntent.getBroadcast(getApplicationContext(), (int)a, intent1,  0);
		        
		
        locationManager.addProximityAlert(
		            latitude, // the latitude of the central point of the alert region
		            longitude, // the longitude of the central point of the alert region
		            500, // the radius of the central point of the alert region, in meters
		            -1, // time for this proximity alert, in milliseconds, or -1 to indicate no expiration
		            proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
		       );
		 }   
	
		
		String s=db.getAllAlarms();
		String[] ss=s.split(",");
		int i=0;
		 CardUI mCardView = (CardUI) findViewById(R.id.cardsview);
	        mCardView.setSwipeable(true);
	       
		for(i=0;i<ss.length;i++)
		{
			MyPlayCard androidViewsCard = new MyPlayCard(ss[i],
					null, "#89A131",
					"#89A131", false, false);
			androidViewsCard.setOnCardSwipedListener(new t(getBaseContext(),ss[i]));
			mCardView.addCard(androidViewsCard);
			 
			// draw cards
			mCardView.refresh();
		}
	
		db.close();
		
	}

	
	public void gohome(View view)
	{
		Intent intent=new Intent(this,MainActivity.class);
		startActivity(intent);
		
	}


}



