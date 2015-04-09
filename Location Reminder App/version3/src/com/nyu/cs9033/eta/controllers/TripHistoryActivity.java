package com.nyu.cs9033.eta.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;

public class TripHistoryActivity extends Activity {

	private TripDatabaseHelper dbHelper;
	private List<Trip> tripList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.triphistory);

		dbHelper = new TripDatabaseHelper(this);

		// get all the trips from database
		tripList = dbHelper.getHistoryTrips();

		// create a ListView
		ListView list = (ListView) findViewById(R.id.trip_history);

		// get each trip and put into an corresponding item
		List<Map<String, Object>> listItemContainer = new ArrayList<Map<String, Object>>();

		String str = null;
		for (int i = 0; i < tripList.size(); i++) {
			Map<String, Object> Itemlist = new HashMap<String, Object>();
			str = tripList.get(i).getTripId();
			Itemlist.put("Tripid", "TripId:  "+tripList.get(i).getTripId());
			Itemlist.put("Name", "TripName:  "+tripList.get(i).getTripName());
			Itemlist.put("Date", "TripTime:  "+tripList.get(i).getDate());
			Itemlist.put("Destination", "TripDest:  "+tripList.get(i).getDestination());
			listItemContainer.add(Itemlist);
		}

		// Create SimpleAdapter to connect the ListView and Trip ArrayList
		SimpleAdapter sa = new SimpleAdapter(this, listItemContainer,
				R.layout.listitem,
				new String[] { "Tripid","Name", "Date", "Destination" }, new int[] {
						R.id.tripid,R.id.tripname, R.id.tripdate, R.id.tripdest });

		list.setAdapter(sa);

		list.setOnItemClickListener(new OnItemClickListener() {
			// position means the chosen trip from the trip list
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println(position);
				Trip tripget = tripList.get(position);
				Intent intentView = new Intent(TripHistoryActivity.this,
						ViewTripActivity.class);
				intentView.putExtra("receive", tripget);
				startActivity(intentView);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}
