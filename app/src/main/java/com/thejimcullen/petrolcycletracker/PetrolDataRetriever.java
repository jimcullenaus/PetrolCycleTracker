package com.thejimcullen.petrolcycletracker;

import android.app.Activity;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class PetrolDataRetriever  extends AsyncTask<String, Void, CityPetrolState> {

	MainActivity activity;

	public PetrolDataRetriever(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	public CityPetrolState doInBackground(String... params) {
		String cityName;
		try {
			cityName = params[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			cancel(true);
			return null;
		}
		Document document = null;
		try {
			document = Jsoup.connect(activity.getString(R.string.url)).get();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		String imgUrl;
		Element recommendation;
		try {
			String elementId = String.format("petrol-prices-in-%s", cityName.toLowerCase());
			Element x = document.getElementById(elementId);
			Element title = x.parent();
			recommendation = title.nextElementSibling().nextElementSibling();
			Element image = recommendation.nextElementSibling().nextElementSibling().child(0);
			if (image.tagName().equals("img")) {
				imgUrl = recommendation.nextElementSibling().nextElementSibling().child(0).absUrl("src");
			} else {
				cancel(true);
				return null;
			}
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			cancel(true);
			return null;
		}

		return new CityPetrolState(imgUrl, recommendation.html());
	}

	@Override
	public void onCancelled() {
		activity.setmMainText("Unable to obtain data");
	}

	@Override
	public void onPostExecute(CityPetrolState state) {
		activity.setmMainText(state.getRecommendation() + System.getProperty("line.separator") + state.getImageUrl());
	}


}
