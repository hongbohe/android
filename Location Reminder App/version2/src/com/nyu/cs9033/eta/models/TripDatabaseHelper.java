package com.nyu.cs9033.eta.models;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
//import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

public class TripDatabaseHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "trips";
	
	private static final String TABLE_TRIP = "trip";
	private static final String COLUMN_TRIP_ID = "_id";
	private static final String COLUMN_TRIP_DATE = "date";
	private static final String COLUMN_TRIP_DESTINATION = "destination";
	private static final String COLUMN_TRIP_FRIENDS = "tripfriends";
	
	private static final String TABLE_LOCATION = "location";
	private static final String COLUMN_LOC_TRIPID = "trip_id";
	private static final String COLUMN_LOC_TIMESTAMP = "time_stamp";
	private static final String COLUMN_LOC_LAT = "latitude";
	private static final String COLUMN_LOC_LONG = "longitude";
	private static final String COLUMN_LOC_ALT = "altitude";
	private static final String COLUMN_LOC_PROVIDER = "provider";
	
	public TripDatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//Create table trips and location
		db.execSQL("create table " + TABLE_TRIP + " ("
				+ COLUMN_TRIP_ID + " integer primary key autoincrement, "
				+ COLUMN_TRIP_DATE + " text, "
				+ COLUMN_TRIP_DESTINATION + " text, "
				+ COLUMN_TRIP_FRIENDS + " text)");
	    
		
		/*db.execSQL("create table " + TABLE_LOCATION + " ("
				+ COLUMN_LOC_TRIPID + " integer reference trip(_id), "
				+ COLUMN_LOC_TIMESTAMP + " integer, "
				+ COLUMN_LOC_LAT + " real, "
				+ COLUMN_LOC_LONG + " real, "
				+ COLUMN_LOC_ALT + " real, "
				+ COLUMN_LOC_PROVIDER + "varchar(100))");
		*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//DROP TABLE IF EXISTS
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION );
		//CREATE TABLE AGAIN
		onCreate(db);

	}
	
	public long insertTrip(Trip trip){
		ContentValues  cv = new ContentValues();
		cv.put(COLUMN_TRIP_DATE,trip.getDate());
		cv.put(COLUMN_TRIP_DESTINATION, trip.getDestination());
		cv.put(COLUMN_TRIP_FRIENDS, trip.getFriends());
		return getWritableDatabase().insert(TABLE_TRIP, null, cv);	
	}
	
	/*public long insertLocation(int tripId, Location location) { 
		ContentValues cv = new ContentValues(); cv.put(COLUMN_LOC_TRIPID, tripId); 
		cv.put(COLUMN_LOC_TIMESTAMP, location.getTime()); 
		cv.put(COLUMN_LOC_LAT, location.getLatitude());
		cv.put(COLUMN_LOC_LONG, location.getLongitude()); 
		cv.put(COLUMN_LOC_ALT, location.getAltitude()); 
		cv.put(COLUMN_LOC_PROVIDER, location.getProvider()); 
		// return id of new location 
		return getWritableDatabase().insert(TABLE_LOCATION, null, cv); 
	}
	*/
	public List<Trip> getAllTrips() { 
		List<Trip> tripList = new ArrayList<Trip>(); 
		SQLiteDatabase db = this.getReadableDatabase(); 
		Cursor cursor = db.rawQuery("select * from " + TABLE_TRIP, null);
		// loop through all query results 
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) { 
			Trip trip = new Trip(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)); 
			tripList.add(trip); 
		}
		return tripList;
	}
	
}
	


