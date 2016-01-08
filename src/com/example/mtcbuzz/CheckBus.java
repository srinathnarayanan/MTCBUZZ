package com.example.mtcbuzz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
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
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.util.FloatMath;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

class mHandler extends Handler
{
LatLng arg0;
mHandler(LatLng b)
{
	arg0=b;
}
}

public class CheckBus extends Activity implements LocationListener 
{	
		
	

	final Context context=this;
    // Google Map
    private GoogleMap googleMap;
 Location mylocation,temp;
 MarkerOptions[] marker=new MarkerOptions[1000];
 MarkerOptions mainmarker;
 MarkerOptions[] marking=new MarkerOptions[1000];
 Marker m;
 Marker[] ms=new Marker[1000];
 Marker[] ms1=new Marker[1000];
 LocationManager locationManager ;
 DBAdapter db;
 String s;
 int k;
 Cursor c;
 LatLng myLatLng;
 
	public void CopyDB(InputStream inputStream,
	        OutputStream outputStream) throws IOException {
	        //---copy 1K bytes at a time---
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = inputStream.read(buffer)) > 0) {
	        outputStream.write(buffer, 0, length);
	        }
	        inputStream.close();
	        outputStream.close();
	        }
	
 
 public boolean isInside(Location temp,Location base)
 {

	 double d=base.distanceTo(temp);
	 
//Toast.makeText(this, Double.toString(d), Toast.LENGTH_SHORT).show();
	  if(d<1500)
	 return true;
	 else
		 return false;
	 
 }
 
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
  	 c=null;
   	db=new DBAdapter(this);
   	String stopping=null;
   	s=null;
   	locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

   	super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_maps2);
      
        try {
	        String destPath = "/data/data/" + getPackageName() +
	        "/databases";
	        File f = new File(destPath);
	        if(!f.exists())
	        {
	        f.mkdirs();
	        f.createNewFile();
	        CopyDB(getBaseContext().getAssets().open("App"),
	        new FileOutputStream(destPath + "/App"));
	        }
	        } catch (FileNotFoundException e) {
	        e.printStackTrace();
	        } catch (IOException e) {
	        e.printStackTrace();
	        }
	       
          try {
	            // Loading map
	        	 initilizeMap();
	        
	        	 TextView t=(TextView)findViewById(R.id.editText1);
	        	 t.setTextColor(Color.BLACK);
	        	 t.setTextSize(20);
	        	 t.setText("Long press to see a new location ");
	             
	             
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
          
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
            		     
            		    }
            		    });
             
             googleMap.setOnMapLongClickListener(new OnMapLongClickListener()
             {
            	 

				@Override
				public void onMapLongClick(LatLng arg0) {
					Toast.makeText(getBaseContext(),"loading ...", Toast.LENGTH_LONG).show();
				final mHandler handler=new mHandler(arg0);
					handler.postDelayed(new Runnable() {
				        public void run() {

							m.remove();
							mainmarker=new MarkerOptions().position(handler.arg0).title("NEW LOCATION").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
					        m=googleMap.addMarker(mainmarker);
					        for(int p=0;p<k;p++)
				     			ms[p].remove();
				     		
					        mylocation.setLatitude(handler.arg0.latitude);
				     		mylocation.setLongitude(handler.arg0.longitude);
				     		k=0;
				            db.open();
				            c=db.getLocations();
				          
				            	 c.moveToFirst();
				                 while (c.isAfterLast() == false) {
				                	 
				                	 
				                	 temp.setLatitude(c.getDouble(c.getColumnIndex("lat")));
				             		temp.setLongitude(c.getDouble(c.getColumnIndex("lng")));
				             		
				             		marker[k]= new MarkerOptions().position(new LatLng(c.getDouble(c.getColumnIndex("lat")),c.getDouble(c.getColumnIndex("lng")))).title(c.getString(c.getColumnIndex("name"))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
				             		ms[k]=googleMap.addMarker(marker[k]);
				             		ms[k].setVisible(false);
				             		
				             		if(isInside(temp,mylocation))
				             		{	
				             		ms[k].setVisible(true);	
				                 }
				             		 k++;
				                	    c.moveToNext();
				                 }
				                 c.close();
				            		
				            db.close();
				            
				             
				             CameraPosition cameraPosition = new CameraPosition.Builder().target(handler.arg0).zoom(14).build();
				      googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				         				           
				        }
				    }, 200);
					   
				}
            	 
            	 
             });      
            
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        googleMap.setMyLocationEnabled(true);
        mylocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER); 
       // Toast.makeText(this, mylocation.getProvider(),Toast.LENGTH_LONG).show();
        temp=new Location("temp");
      
        myLatLng = new LatLng(mylocation.getLatitude(),
                mylocation.getLongitude());
       
        mainmarker=new MarkerOptions().position(myLatLng).title("YOU ARE HERE").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        m=googleMap.addMarker(mainmarker);
       
        k=0;
        
        db.open();
        	
        	 c=db.getLocations();
      
        	 c.moveToFirst();
             while (c.isAfterLast() == false) {
            	 
            	 
            	 temp.setLatitude(c.getDouble(c.getColumnIndex("lat")));
         		temp.setLongitude(c.getDouble(c.getColumnIndex("lng")));
         		
         		marker[k]= new MarkerOptions().position(new LatLng(c.getDouble(c.getColumnIndex("lat")),c.getDouble(c.getColumnIndex("lng")))).title(c.getString(c.getColumnIndex("name"))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
         		ms[k]=googleMap.addMarker(marker[k]);
         		ms[k].setVisible(false);
         		
         		if(isInside(temp,mylocation))
         		{	
         		ms[k].setVisible(true);	
             }
         		 k++;
            	    c.moveToNext();
             }
             c.close();
        		
        db.close();
        
         
         CameraPosition cameraPosition = new CameraPosition.Builder().target(myLatLng).zoom(14).build();
  googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        
        
    }
 
    @Override
    protected void onResume() {
    	super.onResume();
        
    	    initilizeMap();
    	
    }
    		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}



}

