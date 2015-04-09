package com.nyu.cs9033.eta.controllers;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nyu.cs9033.eta.models.Trip;
//import android.database.DatabaseErrorHandler;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class TripDatabaseHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "trips";

	private static final String TABLE_TRIP = "trip";
	private static final String COLUMN_TRIP_NAME = "tripname";
	private static final String COLUMN_TRIP_ID = "tripid";
	private static final String COLUMN_TRIP_DATE = "tripdate";
	private static final String COLUMN_TRIP_DESTINATION = "destination";
	private static final String COLUMN_TRIP_PARTICIPANTS = "friends";



	public TripDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// Create table trips and location
		db.execSQL("create table " + TABLE_TRIP + " (" + COLUMN_TRIP_ID
				+ " text primary key, " + COLUMN_TRIP_NAME
				+ " text, " + COLUMN_TRIP_DATE + " text, "
				+ COLUMN_TRIP_DESTINATION + " text," + COLUMN_TRIP_PARTICIPANTS
				+ " text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// DROP TABLE IF EXISTS
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);

		onCreate(db);

	}

	public long insertTrip(Trip trip) {
		// onUpgrade(d,1,2 );
		ContentValues contvalue = new ContentValues();
		String str = trip.getTripId();
		contvalue.put(COLUMN_TRIP_ID,trip.getTripId());
		contvalue.put(COLUMN_TRIP_NAME, trip.getTripName());
		contvalue.put(COLUMN_TRIP_DATE, trip.getDate());
		contvalue.put(COLUMN_TRIP_DESTINATION, trip.getDestination());
		contvalue.put(COLUMN_TRIP_PARTICIPANTS, trip.getFriends());
		return getWritableDatabase().insert(TABLE_TRIP, null, contvalue);
	}


	public List<Trip> getHistoryTrips() {
		List<Trip> tripHisoryList = new ArrayList<Trip>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + TABLE_TRIP, null);
		// loop through all query results
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Trip trip = new Trip(cursor.getString(0),cursor.getString(1), cursor.getString(2),
					cursor.getString(3), cursor.getString(4));
			tripHisoryList.add(trip);
		}
		return tripHisoryList;
	}

}
