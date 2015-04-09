package com.nyu.cs9033.eta.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.models.TripDatabaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class TripHistoryActivity extends Activity {
	
	private TripDatabaseHelper dbHelper;
	private List<Trip> tripList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trip_history);
		dbHelper = new TripDatabaseHelper(this);

		tripList = dbHelper.getAllTrips();
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		
		if (tripList.size() < 1) {

			TextView noTrip=(TextView)findViewById(R.id.notrip);
			noTrip.setVisibility(View.VISIBLE);

		}
		
		for (int i=0; i < tripList.size(); i++){
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("tripDate",tripList.get(i).getDate());
			listItem.put("tripDesc",tripList.get(i).getDestination());
			listItem.put("tripFriends", tripList.get(i).getFriends());
			listItems.add(listItem);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.simple_item,
				new String[]{"tripDate", "tripDesc", "tripFriends"},
				new int[] {R.id.sim_trip_date,R.id.sim_trip_desc, R.id.sim_trip_friends});
		ListView list = (ListView) findViewById(R.id.trip_history_show);
		list.setAdapter(simpleAdapter);
		list.setOnItemClickListener(new OnItemClickListener()
		{
			//The "location" is selected
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				System.out.println(position);
				Trip seTrip = tripList.get(position);
				startTripViewer(seTrip);
			}
		});
		
		
	}
	public void startTripViewer(Trip mTrip) {
		
		// TODO - fill in here
		Intent toViewTripActivity = new Intent(TripHistoryActivity.this, ViewTripActivity.class);
		toViewTripActivity.putExtra("tripCreated", mTrip);
		startActivity(toViewTripActivity);
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		if (dbHelper != null){
			dbHelper.close();
		}
	}
}
