package com.example.mtcbuzz;

import java.util.ArrayList;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class NewMaps extends Activity  {
	
	final Context context=this;
    // Google Map
    private GoogleMap googleMap;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	
        String stopping=null;
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.newmaps_layout);
        TextView t1=(TextView)findViewById(R.id.editText1);
        TextView t2=(TextView)findViewById(R.id.editText2);
     
        
        final Intent intent = getIntent();
        String routing1 = intent.getStringExtra(Intermediate.routing1);
        String routing2 = intent.getStringExtra(Intermediate.routing2);
        String startings = intent.getStringExtra(Intermediate.startings);
        String stoppings = intent.getStringExtra(Intermediate.stoppings);
        String intermediate = intent.getStringExtra(Intermediate.intermediate);
        String stopnumber=intent.getStringExtra(Intermediate.stopnumber);
        String startnumberings = intent.getStringExtra(Intermediate.startnumberings);
        String stopnumberings=intent.getStringExtra(Intermediate.stopnumberings);
          //Toast.makeText(this," ORANGE :"+routing1+"\n RED :"+routing2, Toast.LENGTH_LONG).show();
          t1.setText(routing1);
          t2.setText(routing2);
          
        
        DBAdapter db = new DBAdapter(this);
        Integer[] index=new Integer[600]; 
        Double[] lati=new Double[600];
        Double[] longi=new Double[600];
        String[] stagename=new String[600];
        
        db.open();
        String s = db.getAllStages(routing1);
        String s1= db.getAllStages(routing2);
        String[] ss=new String[600];
        String[] ss1=new String[600];
       ss=s.split(",");
       ss1=s1.split(",");
       String[] finals=new String[600];
       int i,j=0,pos=0,pos1=0;
       
   for(i=0;i<ss.length;i++)
   {
	   if(ss[i].equals(startnumberings))
	   pos=i;
	   if(ss[i].equals(stopnumberings))
       pos1=i;
   }
   
   if(pos<pos1)
   {
	   for(i=pos;i<=pos1;i++)
	   {
		   
		   finals[j]=ss[i];
	       index[j++]=0;
	       
	   }
   }
	   
   else
   {
	   
	   for(i=pos;i>=pos1;i--)
	   {
		   finals[j]=ss[i];
	       index[j++]=0;
	   }
	   
   }
	
   for(i=0;i<ss1.length;i++)
   {
	   if(ss1[i].equals(stopnumberings))
	   pos=i;
	   else if(ss1[i].equals(stopnumber))
		   pos1=i;
   }
   
   if(pos<pos1)
   {
	   for(i=pos+1;i<=pos1;i++)
	   {
		   
		   finals[j]=ss1[i];
	       index[j++]=1;
	       
	   }
   }
	   
   else
   {
	   
	   for(i=pos-1;i>=pos1;i--)
	   {
		   finals[j]=ss1[i];
	       index[j++]=1;
	   }
	   
   }
 
   
   Cursor c = null;
       
       for(i=0;i<j;i++)
       {
    	   try
    	   {
       c=db.getStageLocation(finals[i]);
       c.moveToFirst();
       lati[i]=Double.parseDouble(c.getString(c.getColumnIndex("lat")));
       longi[i]=Double.parseDouble(c.getString(c.getColumnIndex("lng")));
       stagename[i]=c.getString(c.getColumnIndex("name"));
    	   }
    	   catch(Exception e)
    	   {
    		   Log.w("hello",finals[i]);
    		   
    	   }
       }
       
        try {
            // Loading map
        	 initilizeMap();
        	 
        // create marker
         MarkerOptions[] marker = new MarkerOptions[100] ;
         for(i=0;i<j;i++)
         {	 
        if(stagename[i].equals(startings)|| stagename[i].equals(stoppings) || stagename[i].equals(intermediate))
        	 marker[i]=new MarkerOptions().position(new LatLng(lati[i], longi[i])).title(stagename[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        else
        {
        	if(index[i]==0) 
        	marker[i]=new MarkerOptions().position(new LatLng(lati[i], longi[i])).title(stagename[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        	else
        	marker[i]=new MarkerOptions().position(new LatLng(lati[i], longi[i])).title(stagename[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        	
        }
        googleMap.addMarker(marker[i]);
        
        
         }
         
         CameraPosition cameraPosition = new CameraPosition.Builder().target(
                 new LatLng(lati[0],longi[0])).zoom(14).build();
  googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        
        
        } catch (Exception e) {
            e.printStackTrace();
        }
 db.close();
c.close();
    }
 
    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById( R.id.map)).getMap();
            // check if map is created successfully or not
           
             googleMap.setOnInfoWindowClickListener(
            		  new GoogleMap.OnInfoWindowClickListener(){
            		    public void onInfoWindowClick(Marker marker){
            		     
            		    	m e=new m(marker.getTitle(),marker.getPosition(),getBaseContext());
                    		e.show(getFragmentManager(), ALARM_SERVICE);	
            		    }
            		    });
            
            
            
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maps, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent=new Intent(this,ShowAlarms.class);
		startActivity(intent);
		return true;
	
	}
	

	
}
