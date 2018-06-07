package com.thejimcullen.petrolcycletracker;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	SwipeRefreshLayout swipeRefreshLayout;
	ImageView mSettingsButton;
	TextView mMainText;
	TextView mWelcome;
	ImageView mPriceGraph;
	TextView mCopyright;
	ProgressBar mProgressBar;
	PetrolDataRetriever sydney = null;
	PetrolDataRetriever melbourne = null;
	PetrolDataRetriever brisbane = null;
	PetrolDataRetriever adelaide = null;
	PetrolDataRetriever perth = null;
	SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		mMainText = findViewById(R.id.main_text);
		mWelcome = findViewById(R.id.welcome_text);
		mPriceGraph = findViewById(R.id.price_chart);
		mCopyright = findViewById(R.id.copyright);

		mProgressBar = findViewById(R.id.pBar);
		mProgressBar.setVisibility(View.GONE);

		swipeRefreshLayout = findViewById(R.id.main_swipe_layout);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				swipeRefreshLayout.setRefreshing(true);
				refreshLayout();

			}
		});

		preferences = getSharedPreferences("CITY_PREF", MODE_PRIVATE);
		// if not restoring from a recent state, use preference
		if (savedInstanceState == null) {
			int cityPref = preferences.getInt("currentCity", -1);
			if (cityPref >= 0) {
				//new PetrolDataRetriever(this).execute(City.getCity(cityPref));
			}
		}
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.action_settings) {
			Toast.makeText(this, "Going to settings", Toast.LENGTH_SHORT).show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		City city;

		if (id == R.id.nav_sydney) {
			city = City.SYDNEY;
		} else if (id == R.id.nav_melbourne) {
			city = City.MELBOURNE;
		} else if (id == R.id.nav_brisbane) {
			city = City.BRISBANE;
		} else if (id == R.id.nav_adelaide) {
			city = City.ADELAIDE;
		} else if (id == R.id.nav_default) {
			displayWelcome();
			finishNavigationSelection();
			SharedPreferences.Editor preferencesEditor = preferences.edit();
			preferencesEditor.putInt("currentCity", -1);
			preferencesEditor.apply();
			return true;
		} else {
			return false;
		}
		displayGraph();
		new PetrolDataRetriever(this).execute(city);
		SharedPreferences.Editor preferenceEditor = preferences.edit();
		preferenceEditor.putInt("currentCity", city.ordinal());
		preferenceEditor.apply();

		finishNavigationSelection();

		return true;
	}

	private void finishNavigationSelection() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// main text
		outState.putCharSequence("MainText", mMainText.getText());
		// image
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		((BitmapDrawable) mPriceGraph.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 50, bs);
		outState.putInt("GraphSize", bs.size());
		outState.putByteArray("Graph", bs.toByteArray());

		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(savedInstanceState.getByteArray("Graph"), 0, savedInstanceState.getInt("GraphSize"));
		mPriceGraph.setImageBitmap(bitmap);
		mMainText.setText(savedInstanceState.getCharSequence("MainText"));
		displayGraph();

		super.onRestoreInstanceState(savedInstanceState);
	}

	public void setmMainText(String text) {
		mMainText.setText(text);
	}

	public void setmMainText(Spanned text) {
		mMainText.setText(text);
	}

	public void displayWelcome() {
		mMainText.setVisibility(View.GONE);
		mWelcome.setVisibility(View.VISIBLE);
		mPriceGraph.setVisibility(View.GONE);
		mCopyright.setVisibility(View.INVISIBLE);
	}

	public void displayGraph() {
		mMainText.setVisibility(View.VISIBLE);
		mWelcome.setVisibility(View.GONE);
		mPriceGraph.setVisibility(View.VISIBLE);
		mCopyright.setVisibility(View.VISIBLE);
	}

	public void startProgress() {
		mProgressBar.setVisibility(View.VISIBLE);
		mMainText.setVisibility(View.GONE);
		mPriceGraph.setVisibility(View.GONE);
	}

	public void endProgress() {
		mMainText.setVisibility(View.VISIBLE);
		mPriceGraph.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}

	public void failedProgress() {
		mMainText.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}

	public void refreshLayout() {
		mCopyright.setText("refreshed");
		swipeRefreshLayout.setRefreshing(false);
	}
}
