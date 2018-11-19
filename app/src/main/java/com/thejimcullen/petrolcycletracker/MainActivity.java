package com.thejimcullen.petrolcycletracker;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.thejimcullen.petrolcycletracker.models.RecommendationState;
import com.thejimcullen.petrolcycletracker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	RecommendationState recommendationState;

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

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
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
			Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
		} else if (id == R.id.about_nav_item) {
			Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
		}
		// City nav buttons
		else if (id == R.id.brisbane_nav_item) {
			Toast.makeText(this, "Brisbane", Toast.LENGTH_SHORT).show();
		} else if (id == R.id.sydney_nav_item) {
			Toast.makeText(this, "Sydney", Toast.LENGTH_SHORT).show();
		} else if (id == R.id.melbourne_nav_item) {
			Toast.makeText(this, "Melbourne", Toast.LENGTH_SHORT).show();
		} else if (id == R.id.adelaide_nav_item) {
			Toast.makeText(this, "Adelaide", Toast.LENGTH_SHORT).show();
		}

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}
