package com.example.mtcbuzz;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.myfirstapp.cardsui.CardStack;
import com.myfirstapp.cardsui.CardUI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class DisplayMessageActivity extends Activity {
	public final static String choosestop="com.example.mtcbuzz.choosestop";
	public final static String startings="com.example.mtcbuzz.startings";
	public final static String stoppings="com.example.mtcbuzz.stoppings";
    
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
	        
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displaymessage);
        
        final Intent intent = getIntent();
        final String stop = intent.getStringExtra(MainActivity.SRC_STOP);
        
        EditText editText = (EditText) findViewById(R.id.from_location);
        editText.setText(stop);
        editText.setEnabled(false);
        
        final String stop2 = intent.getStringExtra(MainActivity.DEST_STOP);
        editText = (EditText) findViewById(R.id.to_location);
        editText.setText(stop2);
        editText.setEnabled(false);
       String stopping=null;
        DBAdapter db = new DBAdapter(this);
        
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
	       
        
        
        //---get all contacts---
        db.open();
        String[] sss=new String[20]; 
        String ss = db.checkRoute(stop, stop2);
        CardUI mCardView = (CardUI) findViewById(R.id.cardsview);
        mCardView.setSwipeable(false);

        String rstart=null;
        String rstop=null;
        
        
        if(ss.equals(""))
        	{
        	ss="NO ROUTES AVAILABLE";

    		MyPlayCard androidViewsCard = new MyPlayCard(ss,
    				"no direct routes were available from "+stop+" to "+stop2, "#669900",
    				"#669900", false, false);
    		androidViewsCard.setOnClickListener(new OnClickListener() {

    			@Override
    			public void onClick(View v) {
    				
    				Intent intent=new Intent(v.getContext(),MainActivity.class);

    				startActivity(intent);	
    			}
    		});

    		mCardView.addCard(androidViewsCard);
            
    		// draw cards
    		mCardView.refresh();
    		MyPlayCard androidViewsCard1 = new MyPlayCard("CHOOSE ALTERNATE ROUTES",
    				"check for routes with an intermediate stop", "#669900",
    				"#669900", false, false);
    		androidViewsCard1.setOnClickListener(new OnClickListener() {

    			@Override
    			public void onClick(View v) {
    			
    				Intent intent1=new Intent(v.getContext(),Intermediate.class);
    				intent1.putExtra(startings,stop );
    				intent1.putExtra(stoppings,stop2);
    				startActivity(intent1);	
    			}
    		});

    		mCardView.addCard(androidViewsCard1);
            
    		// draw cards
    		mCardView.refresh();



        	}
        else
        {
        	
     sss=ss.split(",");
        int i=0;
        for(i=0;i<sss.length;i++)
        {
        rstart=db.returnStart(sss[i]);
        rstop=db.returnStop(sss[i]);
        
		MyPlayCard androidViewsCard = new MyPlayCard(sss[i],
				"from "+rstart+" to "+rstop, "#669900",
				"#669900", false, false);
		final String s=androidViewsCard.title;
		androidViewsCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(v.getContext(),Maps.class);

				intent.putExtra(choosestop, s);
				intent.putExtra(startings,stop );
				intent.putExtra(stoppings,stop2);
				startActivity(intent);	
			}
		});
		mCardView.addCard(androidViewsCard);
        
		// draw cards
		mCardView.refresh();
        }
        
        }// end of else
        db.close();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_message, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	Intent intent = new Intent(getApplicationContext(), ShowAlarms.class);
			startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                  Bundle savedInstanceState) {
              View rootView = inflater.inflate(R.layout.fragment_display_message,
                      container, false);
              return rootView;
        }
    }
}