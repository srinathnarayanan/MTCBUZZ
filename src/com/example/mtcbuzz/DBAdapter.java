package com.example.mtcbuzz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
public class DBAdapter {

static final String KEY_ID1 = "_id1";
static final String KEY_ID2 = "_id2";
static final String KEY_ID3 = "_id3";

static final String KEY_ALARM = "name";


static final String KEY_ROUTENAME = "name";
static final String KEY_START = "start";
static final String KEY_STOP = "stop";
static final String KEY_STAGES = "stages";

static final String KEY_ID = "id";
static final String KEY_STOPNAME = "name";
static final String KEY_LAT = "lat";
static final String KEY_LNG = "lng";

static final String TAG = "DBAdapter";
static final String DATABASE_NAME = "App";
static final String DATABASE_TABLE1 = "routes";
static final String DATABASE_TABLE2 = "stages";
static final String DATABASE_TABLE3 = "alarm";

static final int DATABASE_VERSION = 1;
final Context context;
DatabaseHelper DBHelper;
SQLiteDatabase db;

public DBAdapter(Context ctx)
{
this.context = ctx;
DBHelper = new DatabaseHelper(context);
}
private static class DatabaseHelper extends SQLiteOpenHelper
{
DatabaseHelper(Context context)
{
	super(context, DATABASE_NAME, null, DATABASE_VERSION);

}
@Override
public void onCreate(SQLiteDatabase db)
{
}
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
{
Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
+ newVersion + ", which will destroy all old data");
db.execSQL("DROP TABLE IF EXISTS contacts");
onCreate(db);
}
}
//---opens the database---
public DBAdapter open() throws SQLException
{
db = DBHelper.getWritableDatabase();
return this;
}


//---closes the database---
public void close()
{
DBHelper.close();
}


//---insert a contact into the database---
public String checkRoute(String start, String stop)
{
	int flag=0;
	Cursor c;
	c=db.query(DATABASE_TABLE2, new String[] {KEY_ID}, KEY_STOPNAME+"=\""+start+"\"" , null, null, null, null);
	c.moveToFirst();
	String starting=c.getString(c.getColumnIndex("id"));
	c.close();
	c=db.query(DATABASE_TABLE2, new String[] {KEY_ID}, KEY_STOPNAME+"=\""+stop+"\"" , null, null, null, null);
	c.moveToFirst();
	String stopping=c.getString(c.getColumnIndex("id"));
	c.close();
	
	String result="";
	String s=null;
	String[] ss=new String[20];
	int i=1,j=0;
	for(i=1;i<=736;i++)
	{
	flag=0;
	c=db.query(DATABASE_TABLE1, new String[] {KEY_STAGES,KEY_ROUTENAME}, KEY_ID1+"="+i , null, null, null, null);
	c.moveToFirst();
	s=c.getString(c.getColumnIndex("stages"));
	ss=s.split(",");
	for(j=0;j<ss.length;j++)
	{
		if(ss[j].equals(starting))
			flag++;
		if(ss[j].equals(stopping))
			flag++;
		
	}
	if(flag==2)
	{
		result+=c.getString(c.getColumnIndex("name"));
		result+=",";
	}
	c.close();
	}
	c.close();
return result;	
}

public Cursor getStageLocation(String id)
{
return db.query(DATABASE_TABLE2, new String[] {KEY_LAT,KEY_LNG,KEY_STOPNAME}, KEY_ID+"=\""+id+"\"", null, null, null, null);
}

public String getAllStages(String routename)
{
Cursor c=db.query(DATABASE_TABLE1, new String[] {KEY_STAGES}, KEY_ROUTENAME+"=\""+routename+"\"", null, null, null, null);
c.moveToFirst();
String s=c.getString(c.getColumnIndex("stages"));
c.close();
return s;
}
public long addAlarm(String alarm)
{
	ContentValues initialValues = new ContentValues();
	initialValues.put(KEY_ALARM, alarm);
	return db.insert(DATABASE_TABLE3, null, initialValues);
}


public String returnStart(String routename)
{
	String res,res1;
	Cursor c=db.query(DATABASE_TABLE1, new String[] {KEY_START}, KEY_ROUTENAME+"=\""+routename+"\"", null, null, null, null);
	c.moveToFirst();
	res=c.getString(c.getColumnIndex("start"));
	c=db.query(DATABASE_TABLE2, new String[] {KEY_STOPNAME}, KEY_ID+"=\""+res+"\"", null, null, null, null);
	c.moveToFirst();
	res1=c.getString(c.getColumnIndex("name"));
	c.close();
	return res1;
}

public String returnStop(String routename)
{
	String res;
	Cursor c=db.query(DATABASE_TABLE1, new String[] {KEY_STOP}, KEY_ROUTENAME+"=\""+routename+"\"", null, null, null, null);
	c.moveToFirst();
	res=c.getString(c.getColumnIndex("stop"));
	c=db.query(DATABASE_TABLE2, new String[] {KEY_STOPNAME}, KEY_ID+"=\""+res+"\"", null, null, null, null);
	c.moveToFirst();
	res=c.getString(c.getColumnIndex("name"));
	c.close();
	return res;
	
}

public String getStopId(String id)
{
	String s;
	Cursor c=db.query(DATABASE_TABLE2, new String[] {KEY_ID}, KEY_STOPNAME+"=\""+id+"\"", null, null, null, null);
	c.moveToFirst();
	s=c.getString(c.getColumnIndex("id"));
	c.close();
	return s;
	
}

public String getIntermediate(String start,String stop)
{
	String s=null;
	String[] ss=new String[50];
	String[] starti=new String[1800];
	String[] startroutei=new String[1800];
	int startsize=0;
	String[] stopi=new String[800];
	String[] stoproutei=new String[1800];
	int stopsize=0;
	
	String route;
	int i,flag1=0,k,flag2=0,j;
	Cursor c;
	c=db.query(DATABASE_TABLE2, new String[] {KEY_ID}, KEY_STOPNAME+"=\""+start+"\"" , null, null, null, null);
	c.moveToFirst();
	start=c.getString(c.getColumnIndex("id"));
c.close();
	c=db.query(DATABASE_TABLE2, new String[] {KEY_ID}, KEY_STOPNAME+"=\""+stop+"\"" , null, null, null, null);
	c.moveToFirst();
	stop=c.getString(c.getColumnIndex("id"));
c.close();
	for(i=1;i<736;i++)
	{
		flag1=0;
		flag2=0;
		
		c=db.query(DATABASE_TABLE1, new String[] {KEY_STAGES,KEY_ROUTENAME}, KEY_ID1+"="+i , null, null, null, null);
		c.moveToFirst();
		s=c.getString(c.getColumnIndex("stages"));//getting all sets of stages
		route=c.getString(c.getColumnIndex("name"));
		ss=s.split(",");
			for(j=0;j<ss.length;j++)
			{
				if(ss[j].equals(start))//checking if start belongs to one of the stages
					{
					flag1=1;
					break;
					}
				if(ss[j].equals(stop))//checking if stop belongs to one of the stages
				{
					flag2=1;
					break;
				}
			}
	//adding all stops to respective lists	
			try
			{
		for(j=0;j<ss.length;j++)
		{
			
			if(flag1==1)
			{
				starti[startsize]=ss[j];
				startroutei[startsize]=route;
				startsize++;
			}
			if(flag2==1)
			{
				stopi[stopsize]=ss[j];
				stoproutei[stopsize]=route;
				stopsize++;
			}
		}
		
			}
			catch(Exception e)
			{
				Log.w(TAG, Integer.toString(j));
		
			}
		c.close();	
	}//end of i loop
	s="";
	int count=0,h=0;
	flag1=flag2=0;
	String z=null;
	String[] zz=new String[50];
	String sss=null;
	
	for(i=0;i<startsize;i++)
	{sss=null;
		
		for(j=0;j<stopsize;j++)
		{
			
			if(stopi[j].equals(starti[i]))
			{
				try
				{
					count=0;
				c=db.query(DATABASE_TABLE2, new String[] {KEY_STOPNAME},KEY_ID+" = "+starti[i], null, null, null, null);
				c.moveToFirst();
				sss=c.getString(c.getColumnIndex("name"));
				c.close();
			//first route,intermediate stop id,stop name, second route
					
				c=db.query(DATABASE_TABLE1, new String[] {KEY_STAGES},KEY_ROUTENAME+"=\""+startroutei[i]+"\"" , null, null, null, null);
					c.moveToFirst();
					z=c.getString(c.getColumnIndex("stages"));//getting all sets of stages
					zz=z.split(",");
					c.close();
						for( h=0;h<zz.length;h++)
						{
							if(zz[h].equals(start))//checking if start belongs to one of the stages
								{
								flag1=h;
								}
							else if(zz[h].equals(starti[i]))//checking if stop belongs to one of the stages
							{
								flag2=h;
							}
						}
						if(flag2>flag1)
						{
							count+=(flag2-flag1+1);
						}
						else
						{
							count+=(flag1-flag2+1);
							
						}
						c=db.query(DATABASE_TABLE1, new String[] {KEY_STAGES},KEY_ROUTENAME+"=\""+stoproutei[j]+"\"" , null, null, null, null);
						c.moveToFirst();
						z=c.getString(c.getColumnIndex("stages"));//getting all sets of stages
						zz=z.split(",");
						c.close();
						
						for( h=0;h<zz.length;h++)
						{
							if(zz[h].equals(starti[i]))//checking if start belongs to one of the stages
								{
								flag1=h;
								}
							else if(zz[h].equals(stop))//checking if stop belongs to one of the stages
							{
								flag2=h;
							}
						}
						if(flag2>flag1)
						{
							count+=(flag2-flag1);
						}
						else
						{
							count+=(flag1-flag2);
							
						}
					
				
				
				s+=startroutei[i]+","+starti[i]+","+sss+","+stoproutei[j]+","+Integer.toString(count)+";";
				c.close();
				}
				catch (Exception e)
				{
					Log.w(TAG, starti[i]);

					
				}
			}
		}
		
	}
	
	return s;
}



public String getAllAlarms()
{
String s="";
Cursor c=db.query(DATABASE_TABLE3, new String[] {KEY_ALARM}, null, null, null, null, null);
if(c.getCount()==0)
	return s;
else
{
c.moveToFirst();
while(!c.isLast())
{
s+=c.getString(c.getColumnIndex("name"));
s+=",";
c.moveToNext();
}
s+=c.getString(c.getColumnIndex("name"));
s+=",";
c.close();
return s;
}
}

public int deleteAlarm(String val)
{
	Cursor c=db.query(DATABASE_TABLE3, new String[] {KEY_ID3}, KEY_ALARM+"=\""+val+"\"", null, null, null, null);
	c.moveToFirst();
	int i=c.getInt(c.getColumnIndex("_id3"));
	db.delete(DATABASE_TABLE3, KEY_ALARM + "=\"" +val+"\"", null);	
	c.close();
	return i;
}

public boolean notPresent(String alarm)
{
	Cursor c=db.query(DATABASE_TABLE3, new String[] {KEY_ID3}, KEY_ALARM+"=\""+alarm+"\"", null, null, null, null);
	if(c.getCount()==0)
	{
		c.close();
		return true;
		
	}
	else
	{
		c.close();
		return false;
	}
}

public Cursor getLocations()
{
	return db.query(DATABASE_TABLE2, new String[] {KEY_LAT,KEY_LNG,KEY_STOPNAME}, null, null, null, null, null);
}

}