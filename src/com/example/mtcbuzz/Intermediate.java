package com.example.mtcbuzz;

import com.myfirstapp.cardsui.CardUI;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class Intermediate extends Activity{
	public final static String routing1="com.example.mtcbuzz.stop1";
	public final static String routing2="com.example.mtcbuzz.stop2";
	public final static String intermediate="com.example.mtcbuzz.intermediate";
	public final static String startings="com.example.mtcbuzz.startings";
	public final static String stoppings="com.example.mtcbuzz.stoppings";
    public final static String stopnumber="com.example.mtcbuzz.stopnumber";
    public final static String startnumberings="com.example.mtcbuzz.startnumberings";
    public final static String stopnumberings="com.example.mtcbuzz.stopnumberings";
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displaymessage);

        final Intent intent = getIntent();
        final String stop = intent.getStringExtra(DisplayMessageActivity.startings);
        
        EditText editText = (EditText) findViewById(R.id.from_location);
        editText.setText(stop);
        editText.setEnabled(false);
        
        final String stop2 = intent.getStringExtra(DisplayMessageActivity.stoppings);
        editText = (EditText) findViewById(R.id.to_location);
        editText.setText(stop2);
        editText.setEnabled(false);
        
        CardUI mCardView = (CardUI) findViewById(R.id.cardsview);
        mCardView.setSwipeable(false);
        
        DBAdapter db = new DBAdapter(this);
        db.open();
        String s=db.getIntermediate(stop, stop2);
        if(s.equals(""))
        {
        	MyPlayCard androidViewsCard = new MyPlayCard("NO ROUTES AVAILABLE","try changing your bus stops","#669900","#669900", false, false);
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
    		

        	
        }
        else
        {
        String[] ss=s.split(";");
        String[][] sss=new String[500][500];
        int i;
        for(i=0;i<ss.length;i++)
        {
        	sss[i]=ss[i].split(",");
        }
        String[] temp=new String[500];
        int j=0;
        for(i=0;i<ss.length;i++)
        {
        	for(j=0;j<ss.length;j++)
        	{
        		if(Integer.parseInt(sss[i][4])<Integer.parseInt(sss[j][4]))
        		{
        		temp=sss[i];
        		sss[i]=sss[j];
        		sss[j]=temp;
        		}
        		
        	}
        	
        }
        
        for(i=0;i<ss.length;i++)
        {
        	final String startnumbering=db.getStopId(stop);
        	final String stopnumb=db.getStopId(stop2);
        	final String stopping1=sss[i][0];
        	final String stopping2=sss[i][3];
        	final String inter=sss[i][2];
        	final String stopnumbering=sss[i][1];
        	//stopping1=first route,stopnumbering=intermediate stop id,inter=stop name,stopping2= second route	
			
        	MyPlayCard androidViewsCard = new MyPlayCard(sss[i][0]+"+"+sss[i][3],"take "+sss[i][0]+" from "+stop+" to "+ inter+",then take "+sss[i][3]+" from "+inter+" to "+stop2,"#669900","#669900", false, false);
    		androidViewsCard.setOnClickListener(new OnClickListener() {

    			@Override
    			public void onClick(View v) {
    				
    				Intent intent=new Intent(v.getContext(),NewMaps.class);
    				intent.putExtra(routing1,stopping1 );
    				intent.putExtra(routing2,stopping2 );
    				intent.putExtra(intermediate,inter );
    				intent.putExtra(startings,stop );
    				intent.putExtra(stoppings,stop2);
    				intent.putExtra(stopnumber,stopnumb);
    				intent.putExtra(stopnumberings,stopnumbering);
    				intent.putExtra(startnumberings,startnumbering);

    				startActivity(intent);	
    			}
    		});

    		mCardView.addCard(androidViewsCard);
            
    		// draw cards
    		mCardView.refresh();
    		

        }
        
        }
        db.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent=new Intent(this,ShowAlarms.class);
		startActivity(intent);
		return true;
	
	}	
}
