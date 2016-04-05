package com.example.listview;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Activity_ListView extends AppCompatActivity {

	private SharedPreferences prefs = null;
	private boolean first_run = true;
	private SharedPreferences.OnSharedPreferenceChangeListener listener;
	public String j_son_string;

	List<BikeData> data;
	ListView my_listview;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Change title to indicate sort by
		setTitle("Sort by:");

		//listview that you will operate on
		my_listview = (ListView)findViewById(R.id.lv);

		//toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();

		setupSimpleSpinner();

		//set the listview onclick listener
		setupListViewOnClickListener();

		//TODO call a thread to get the JSON list of bikes
		//TODO when it returns it should process this data with bindData

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
				if (key.equals("listPref")) {
					Toast.makeText(getBaseContext(),"Preference changed", Toast.LENGTH_SHORT).show();
					if (!first_run) {
						checkNetworkAndDownloadJson();
					}

				}
			}
		};
		prefs.registerOnSharedPreferenceChangeListener(listener);

		checkNetworkAndDownloadJson();


	}

	private void checkNetworkAndDownloadJson() {
		boolean network_reachable = ConnectivityCheck.isNetworkReachable(this);

		if (network_reachable) {
			boolean has_wifi = ConnectivityCheck.isWifiReachable(this);
			if (has_wifi) {
				DownloadTask task = new DownloadTask(this);
				//try {
					task.execute(prefs.getString("listPref",""));
					//Object temp = task.execute(prefs.getString("listPref","")).get(); //never used temp just making program wait
		//		} catch (InterruptedException e) {
		//			e.printStackTrace();
		//		} catch (ExecutionException e) {
//					e.printStackTrace();
		//		}

				//bindData(this.j_son_string);
			} else {
				Toast.makeText(this,"Connected to the network, but have no wifi access",Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this,"Network is unreachable",Toast.LENGTH_SHORT).show();
		}
	}

	private void setupListViewOnClickListener() {
		//TODO you want to call my_listviews setOnItemClickListener with a new instance of android.widget.AdapterView.OnItemClickListener() {
	}

	/**
	 * Takes the string of bikes, parses it using JSONHelper
	 * Sets the adapter with this list using a custom row layout and an instance of the CustomAdapter
	 * binds the adapter to the Listview using setAdapter
	 *
	 * @param JSONString  complete string of all bikes
	 */
	public void bindData(String JSONString) {
		data = JSONHelper.parseAll(JSONString);


	}


	Spinner spinner;
	/**
	 * create a data adapter to fill above spinner with choices(Company,Location and Price),
	 * bind it to the spinner
	 * Also create a OnItemSelectedListener for this spinner so
	 * when a user clicks the spinner the list of bikes is resorted according to selection
	 * dontforget to bind the listener to the spinner with setOnItemSelectedListener!
	 */
	private void setupSimpleSpinner() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent myIntent = new Intent(this, activityPreference.class);
				startActivity(myIntent);
				break;

		default:
			break;
		}
		return true;
	}
}
