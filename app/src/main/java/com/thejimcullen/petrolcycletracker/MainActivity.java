package com.thejimcullen.petrolcycletracker;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thejimcullen.petrolcycletracker.models.City;
import com.thejimcullen.petrolcycletracker.models.RecommendationState;
import com.thejimcullen.petrolcycletracker.databinding.ActivityMainBinding;
import com.thejimcullen.petrolcycletracker.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	SwipeRefreshLayout refreshLayout;
	RecommendationState recommendationState;
	RelativeLayout mainContent;
	ProgressBar progressBar;
	TextView welcomeText;
	TextView failureText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		ActivityMainBinding cycleInformationBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		recommendationState = new RecommendationState();
		cycleInformationBinding.setRecommendation(recommendationState);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// Drawer
		DrawerLayout drawer =  findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		refreshLayout = findViewById(R.id.main_refresh_layout);
		refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshLayout.setRefreshing(true);
				refreshLayout();
			}
		});

		mainContent = findViewById(R.id.main_content);
		progressBar = findViewById(R.id.progress_meter);
		welcomeText = findViewById(R.id.welcome_text);
		failureText = findViewById(R.id.failed_text);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		// Top nav buttons
		if (id == R.id.welcome_nav_item) {
			displayWelcome();
			Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
		} else {
			displayContent();

			City selectedCity = null;

			// City nav buttons
			if (id == R.id.brisbane_nav_item) {
				selectedCity = City.BRISBANE;
			} else if (id == R.id.sydney_nav_item) {
				selectedCity = City.SYDNEY;
			} else if (id == R.id.melbourne_nav_item) {
				selectedCity = City.MELBOURNE;
			} else if (id == R.id.adelaide_nav_item) {
				selectedCity = City.ADELAIDE;
			}

			fetchCity(selectedCity);
		}

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	public void startProgress() {
		failureText.setVisibility(View.GONE);
		mainContent.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
	}

	public void failedProgress() {
		failureText.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
	}

	public void endProgress() {
		mainContent.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
	}

	public void setTextResults(String intro, String recommendation, String graphInfo) {
		recommendationState.introText.set(intro);
		//recommendationState.buyingRecommendation.set(recommendation);
		//recommendationState.graphInfo.set(graphInfo);
	}

	private void displayWelcome() {
		this.recommendationState.setSelectedCity(null);
		welcomeText.setVisibility(View.VISIBLE);
		mainContent.setVisibility(View.GONE);
	}

	private void displayContent() {
		welcomeText.setVisibility(View.GONE);
		mainContent.setVisibility(View.VISIBLE);
	}

	private void refreshLayout() {
		City selectedCity = recommendationState.getSelectedCity();
		if (selectedCity != null) {
			fetchCity(selectedCity);
		}
	}

	private void fetchCity(City city) {
		if (city == null) {
			return;
		}

		new PetrolDataRetriever(this).execute(city);
	}
}
