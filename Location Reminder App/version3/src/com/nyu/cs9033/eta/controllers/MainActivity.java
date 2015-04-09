package com.nyu.cs9033.eta.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;

public class MainActivity extends Activity {


	public static final String TAG = "MainActivity";
	private Trip trip = null;
	private TextView tv;
	private Button btnShowLocation,btnArrival;
	private Messenger mService = null;
	/** Messenger for communicating with service. */
	boolean mIsBound;
	/** Flag indicating whether we have called bind on the service. */
	final Messenger mMessenger = new Messenger(new IncomingHandler());


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// TODO - fill in here
		
		btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
		btnArrival = (Button) findViewById(R.id.btnArrival);


		btnArrival.setOnClickListener(btnArrivalListener);
		startService(new Intent(MainActivity.this, TestLocationService.class));
		startTripCreator();
		startTripHistory();
		onrefresh();
		setcurrentTripinfo(getIntent());

		CheckIfServiceIsRunning();

	}

	/**
	 * This method should start the Activity responsible for creating a Trip.
	 */


	/**
	 * This method should start the Activity responsible for viewing a Trip.
	 */


	/**
	 * Receive result from CreateTripActivity here. Can be used to save instance
	 * of Trip object which can be viewed in the ViewTripActivity.
	 * 
	 * Note: This method will be called when a Trip object is returned to the
	 * main activity. Remember that the Trip will not be returned as a Trip
	 * object; it will be in the persisted Parcelable form. The actual Trip
	 * object should be created and saved in a variable for future use, i.e. to
	 * view the trip.
	 * 
	 */

	
	public void startTripHistory() {
		final Button toViewHistory = (Button) findViewById(R.id.ViewTripHistory);
		toViewHistory.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intenttoHistory = new Intent(MainActivity.this,
						TripHistoryActivity.class);
				startActivity(intenttoHistory);
			}
		});
	}

	private OnClickListener btnArrivalListener = new OnClickListener() {
		public void onClick(View v){
			tv = (TextView) findViewById(R.id.textArrival);
			tv.setText("I arrived.");
			
		}
		
	};
		



	/**
	 * This method should start the Activity responsible for creating a Trip.
	 */
	public void startTripCreator() {

		// TODO - fill in here
		final Button toCreateTP = (Button) findViewById(R.id.toCreateTP);
		toCreateTP.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intenttoCreate = new Intent(MainActivity.this,
						CreateTripActivity.class);
				startActivity(intenttoCreate);
			}
		});
	}

	/**
	 * This method should start the Activity responsible for viewing a Trip.
	 */


	/****** New Function here: ********/
	private void setcurrentTripinfo(Intent i) {

		trip = i.getParcelableExtra("curtrip");
		if (trip != null) {

			btnShowLocation.setVisibility(View.VISIBLE);
			btnArrival.setVisibility(View.VISIBLE);


			tv = (TextView) findViewById(R.id.Maintripid);
			tv.setText("Trip Id");
			tv = (TextView) findViewById(R.id.Maintripidval);
			tv.setText(trip.getTripId());

			tv = (TextView) findViewById(R.id.Maintripname);
			tv.setText("Trip Name");
			tv = (TextView) findViewById(R.id.Maintripnameval);
			tv.setText(trip.getTripName());

			tv = (TextView) findViewById(R.id.Maintriplocation);
			tv.setText("Trip Location");
			tv = (TextView) findViewById(R.id.Maintriplocationval);
			tv.setText(trip.getDestination());

			tv = (TextView) findViewById(R.id.Maintriptime);
			tv.setText("Trip Time");
			tv = (TextView) findViewById(R.id.Maintriptimeval);
			tv.setText(trip.getDate());

			tv = (TextView) findViewById(R.id.Maintripmember);
			tv.setText("Trip Memeber");
			tv = (TextView) findViewById(R.id.Maintripmemberval);
			tv.setText(trip.getFriends());

			tv = (TextView) findViewById(R.id.cur_trip);
			tv.setText("Trip Tracking");

		}
	}

	// This set the refresh button
	private void onrefresh() {
		// show location button click event
		btnShowLocation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// check if GPS enabled
				Message msg = Message.obtain(null,
						TestLocationService.MSG_FROM_ACTIVITY);
				msg.replyTo = mMessenger;
				Bundle b = new Bundle();
				b.putString("trip_id", trip.getTripId());
				msg.setData(b);
				try {
					mService.send(msg);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/******** Communicate with service ************/
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = new Messenger(service);
			try {
				Message msg = Message.obtain(null,
						TestLocationService.MSG_REGISTER_CLIENT);
				msg.replyTo = mMessenger;
				Bundle b = new Bundle();
				b.putString("trip_id", trip.getTripId());
				msg.setData(b);
				mService.send(msg);
			} catch (RemoteException e) {
				// In this case the service has crashed before we could even do
				// anything with it
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			// This is called when the connection with the service has been
			// unexpectedly disconnected - process crashed.
			mService = null;
		}
	};

	private void CheckIfServiceIsRunning() {
		// If the service is running when the activity starts, we want to
		// automatically bind to it.
		if (TestLocationService.isRunning()) {
			doBindService();
		}
	}

	private String parseTripStatus(String result) {
		JSONObject jsonobject;
		String convertres = "";
		try {
			jsonobject = new JSONObject(result);
			JSONArray distance_left = jsonobject.getJSONArray("distance_left");
			JSONArray time_left = jsonobject.getJSONArray("time_left");
			JSONArray people = jsonobject.getJSONArray("people");
			for (int i = 0; i < people.length(); i++) {
				convertres += people.getString(i) + " :\tdistance_left\t"
						+ distance_left.getDouble(i) + "\ttime_left\t"
						+ time_left.getInt(i) + "\n";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertres;
	}

	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TestLocationService.MSG_FROM_SERVICE_UPLOAD_LOCATION:
				// handle request from service
				tv = (TextView) findViewById(R.id.cur_trip);
				String str = msg.getData().getString("current_location");
				tv.setText(str);
				break;
			case TestLocationService.MSG_FROM_SERVICE_TRIP_STATUS:
				tv = (TextView) findViewById(R.id.cur_trip_Trackinginfo);
				String result = msg.getData().getString("trip_status");
				tv.setText(parseTripStatus(result));
				break;
			default:
				break;
			}
		}
	}

	void doBindService() {
		// Establish a connection with the service. We use an explicit
		// class name because there is no reason to be able to let other
		// applications interact with our component.
		bindService(new Intent(MainActivity.this, TestLocationService.class),
				mConnection, // ServiceConnection object
				Context.BIND_AUTO_CREATE); // Create service if not

		mIsBound = true;

	}

	void doUnbindService() {
		if (mIsBound) {
			// If we registered with the service, then now is the time to
			// unregister.
			if (mService != null) {
				try {
					Message msg = Message.obtain(null,
							TestLocationService.MSG_UNREGISTER_CLIENT);
					msg.replyTo = mMessenger;
					mService.send(msg);
				} catch (RemoteException e) {
					// There is nothing special we need to do if the service has
					// crashed.
				}
			}
			// Detach our existing connection.
			unbindService(mConnection);
			mIsBound = false;
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			doUnbindService();
			stopService(new Intent(MainActivity.this, TestLocationService.class));
		} catch (Throwable t) {
			Log.e("MainActivity", "Failed to unbind from the service", t);
		}
	}
	
}
