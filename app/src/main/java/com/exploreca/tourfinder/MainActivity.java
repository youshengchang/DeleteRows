package com.exploreca.tourfinder;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.exploreca.tourfinder.db.ToursDataSource;
import com.exploreca.tourfinder.model.Tour;
import com.exploreca.tourfinder.xml.ToursPullParser;

public class MainActivity extends ListActivity {

	public static final String LOGTAG="EXPLORECA";
	public static final String USERNAME="pref_username";
	public static final String VIEWIMAGE="pref_viewimages";
	private static final int TOUR_DETAIL_ACTIVITY = 1001;

	private SharedPreferences settings;
	private OnSharedPreferenceChangeListener listener;
	private List<Tour> tours;
	boolean isMyTours;

	ToursDataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		settings = PreferenceManager.getDefaultSharedPreferences(this);

		listener = new OnSharedPreferenceChangeListener() {

			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {
				MainActivity.this.refreshDisplay();
			}
		};
		settings.registerOnSharedPreferenceChangeListener(listener);

		datasource = new ToursDataSource(this);
		datasource.open();

		tours = datasource.findAll();
		if (tours.size() == 0) {
			createData();
			tours = datasource.findAll();
		}
		isMyTours = false;

		refreshDisplay();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;

		case R.id.menu_all:
			tours = datasource.findAll();
			refreshDisplay();
			isMyTours = false;
			break;

		case R.id.menu_cheap:
			tours = datasource.findFiltered("price <= 300", "price ASC");
			refreshDisplay();
			isMyTours = false;
			break;

		case R.id.menu_fancy:
			tours = datasource.findFiltered("price >= 1000", "price DESC");
			refreshDisplay();
			isMyTours = false;
			break;

		case R.id.menu_mytours:
            datasource.open();
			tours = datasource.findMyTours();
			refreshDisplay();
			isMyTours = true;
			break;
			
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void refreshDisplay() {
		
		boolean viewImages = settings.getBoolean(VIEWIMAGE, false);
		
		if (viewImages) {
			ArrayAdapter<Tour> adapter = new TourListAdapter(this, tours);
			setListAdapter(adapter);
		} else {
			ArrayAdapter<Tour> adapter = new ArrayAdapter<Tour>(this, 
					android.R.layout.simple_list_item_1, tours);
			setListAdapter(adapter);
		}
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		datasource.open();
	}

	@Override
	protected void onPause() {
		super.onPause();
		datasource.close();
	}

	private void createData() {
		ToursPullParser parser = new ToursPullParser();
		List<Tour> tours = parser.parseXML(this);

		for (Tour tour : tours) {
			datasource.create(tour);
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Tour tour = tours.get(position);
		
		Intent intent = new Intent(this, TourDetailActivity.class);
		
		intent.putExtra(".model.Tour", tour);
		intent.putExtra("isMyTours", isMyTours);
		
		startActivityForResult(intent, TOUR_DETAIL_ACTIVITY);
		
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if((requestCode == TOUR_DETAIL_ACTIVITY)&&(resultCode == -1)){
            datasource.open();
            tours = datasource.findMyTours();
            refreshDisplay();
            isMyTours = true;
        }
    }
}
