package com.example.mtcbuzz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;



public class ProximityIntentReceiver extends BroadcastReceiver {
	 
	
	 @Override
	 public void onReceive(Context context, Intent intent) {
		 
		 PendingIntent p=PendingIntent.getBroadcast(context,0,intent,-1);
		 
		 String name= intent.getExtras().getString("name");
		 
		 String k=LocationManager.KEY_PROXIMITY_ENTERING;
	 // Key for determining whether user is leaving or entering 
	  
	  boolean state=intent.getBooleanExtra(k, false);
	  //Gives whether the user is entering or leaving in boolean form
final long[] pattern={0,500,1000,1000,1000,500,1000,1000,1000,
		0,500,1000,1000,1000,500,1000,1000,1000,
		0,500,1000,1000,1000,500,1000,1000,1000,
		0,500,1000,1000,1000,500,1000,1000,1000,
		0,500,1000,1000,1000,500,1000,1000,1000,
		0,500,1000,1000,1000,500,1000,1000,1000,
		0,500,1000,1000,1000,500,1000,1000,1000,
		0,500,1000,1000,1000,500,1000,1000,1000};
if(state){
	   	 
	
		 NotificationManager mNotificationManager =
			        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			// Sets an ID for the notification, so it can be updated
			int notifyID = 1;
			
			NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context)
			    .setContentTitle("ALMOST THERE...")
			    .setContentText(name+" is here")
			    .setSmallIcon(R.drawable.ic_launcher)
			    .setWhen(System.currentTimeMillis())
			    .setTicker(name+" is here")
			    .setLights(Color.GREEN,500, 500)
			    .setVibrate(pattern)
			    .setOnlyAlertOnce(true)
			    .setPriority(2)
			    .setAutoCancel(true);
				Uri alarmSound = RingtoneManager.getActualDefaultRingtoneUri(context, 1);
					
		    mNotifyBuilder.setSound(alarmSound);
		    
			mNotificationManager.notify(
			            notifyID,
			            mNotifyBuilder.build());
		
	
	

	   
	  }//end of if
	  }//end of onReceive
	 	}