package com.example.listview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class Activity_ListView extends AppCompatActivity {

	private SharedPreferences prefs = null;
	private boolean first_run = true;
	private SharedPreferences.OnSharedPreferenceChangeListener listener;
	public String j_son_string;
	Spinner spinner;
	private ArrayAdapter adapt;


	private CustomAdapter adapter;
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
		spinner = (Spinner) findViewById(R.id.spinner);

		//toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();

		setupSimpleSpinner();

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				if (data != null) {
					sortData();
					bindAdapter();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});



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


		first_run = false;
		checkNetworkAndDownloadJson();
		//set the listview onclick listener
		//setupListViewOnClickListener();


	}

	private void checkNetworkAndDownloadJson() {
		boolean network_reachable = ConnectivityCheck.isNetworkReachable(this);

		if (network_reachable) {
			boolean has_wifi = ConnectivityCheck.isWifiReachable(this);
			if (has_wifi) {
				DownloadTask task = new DownloadTask(this);
				String url = prefs.getString("listPref","");
				Log.e("URL_String",prefs.getString("listPref",""));
				task.execute(url);
			} else {
				Toast.makeText(this,"Connected to the network, but have no wifi access",Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this,"Network is unreachable",Toast.LENGTH_SHORT).show();
		}
	}

	public void setupListViewOnClickListener() {
		//BikeData b = (BikeData)my_listview.getAdapter().getItem(0);
		//b.toString();
		//TODO you want to call my_listviews setOnItemClickListener with a new instance of android.widget.AdapterView.OnItemClickListener() {
		my_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Object o = my_listview.getItemAtPosition(position);
				BikeData b = (BikeData) o;

				AlertDialog a = new AlertDialog.Builder(view.getContext()).create();
				a.setMessage(b.toString());
				a.setButton("Ok",new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// just needs to close out
					}
				});
				a.show();
			}
		});

	}


	/**
	 * Sorts the BikeData based on the current selected sort option
	 */
	private void sortData() {
		String choice = spinner.getSelectedItem().toString();

		if (choice.equals("Company")) {
			Collections.sort(data,new ComparatorComp());
			return;
		}

		if (choice.equals("Location")) {
			Collections.sort(data,new ComparatorLocation());
			return;
		}

		if (choice.equals("Price")) {
			Collections.sort(data, new ComparatorPrice());
			return;
		}
	}


	private void bindAdapter() {
		adapter = new CustomAdapter(this,R.layout.listview_row_layout,data);
		my_listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
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
		sortData();
		bindAdapter();
	}



	/**
	 * create a data adapter to fill above spinner with choices(Company,Location and Price),
	 * bind it to the spinner
	 * Also create a OnItemSelectedListener for this spinner so
	 * when a user clicks the spinner the list of bikes is resorted according to selection
	 * dontforget to bind the listener to the spinner with setOnItemSelectedListener!
	 */
	private void setupSimpleSpinner() {
		spinner.setPrompt("Bikes");
		String[] s = {"Company", "Location", "Price"};
		adapt = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,s);
		adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapt);
		adapt.notifyDataSetChanged();

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

			case R.id.action_refresh:
				checkNetworkAndDownloadJson();

		default:
			break;
		}
		return true;
	}
}
