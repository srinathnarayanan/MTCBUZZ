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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

class m extends DialogFragment {
	String title;
    Context c;
    LatLng ll;
    public static final String alarm="com.example.mtcbuzz.alarm";
    public static final String lati="com.example.mtcbuzz.lati";
    public static final String longi="com.example.mtcbuzz.longi";


    public m(String t, LatLng l,Context cc)
    {
    	ll=l;
    	c=cc;
    	title=t;
    }
    
    
    
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
       final String s=title;
       final String  latitude=Double.toString(ll.latitude);
       final String  longitude=Double.toString(ll.longitude);
   	
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("alert you at "+title+"?").setPositiveButton("OK",
            		   new DialogInterface.OnClickListener() {
            	   public void onClick(DialogInterface dialog, int which) {
            		   
            		   Intent intent=new Intent(c,AlarmActivity.class);
            		   intent.putExtra(alarm, s);
            		   intent.putExtra(lati, latitude);
            		   intent.putExtra(longi, longitude);
            		   
            		   startActivity(intent);
            		   Toast.makeText(c, s+" alarm added", Toast.LENGTH_LONG).show();
             		   
            	   }        
               }).setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
            	   public void onClick(DialogInterface dialog, int which) {
            		   Toast.makeText(getActivity(), "select correct bus stop", Toast.LENGTH_LONG).show();
            	   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
};


public class Maps extends Activity  {
	
	final Context context=this;
    // Google Map
    private GoogleMap googleMap;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
     
        
    	String stopping=null;
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
    	TextView t=(TextView)findViewById(R.id.editText1);
        final Intent intent = getIntent();
        String route = intent.getStringExtra(DisplayMessageActivity.choosestop);
        String startings = intent.getStringExtra(DisplayMessageActivity.startings);
        String stoppings = intent.getStringExtra(DisplayMessageActivity.stoppings);
      t.setText(route);
        DBAdapter db = new DBAdapter(this);
       
        Double[] lati=new Double[30];
        Double[] longi=new Double[30];
        String[] stagename=new String[30];
        
        db.open();
        String s = db.getAllStages(route);
       String[] ss=s.split(",");
       Cursor c;
       int i=0;
       for(i=0;i<ss.length;i++)
       {
       c=db.getStageLocation(ss[i]);
       c.moveToFirst();
       lati[i]=Double.parseDouble(c.getString(c.getColumnIndex("lat")));
       longi[i]=Double.parseDouble(c.getString(c.getColumnIndex("lng")));
       stagename[i]=c.getString(c.getColumnIndex("name"));
       
       }
       
        try {
            // Loading map
        	 initilizeMap();
        	 
        // create marker
         MarkerOptions[] marker = new MarkerOptions[60] ;
         for(i=0;i<ss.length;i++)
         {	 
        if(stagename[i].equals(startings)|| stagename[i].equals(stoppings))
        	 marker[i]=new MarkerOptions().position(new LatLng(lati[i], longi[i])).title(stagename[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        else
        	 marker[i]=new MarkerOptions().position(new LatLng(lati[i], longi[i])).title(stagename[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        
        googleMap.addMarker(marker[i]);
        
        
         }
         
         CameraPosition cameraPosition = new CameraPosition.Builder().target(
                 new LatLng(lati[0],longi[0])).zoom(14).build();
  googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        
        
        } catch (Exception e) {
            e.printStackTrace();
        }
 db.close();
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
