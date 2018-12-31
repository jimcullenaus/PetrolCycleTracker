package com.thejimcullen.petrolcycletracker;

import android.os.AsyncTask;
import android.os.Build;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.thejimcullen.petrolcycletracker.models.City;
import com.thejimcullen.petrolcycletracker.models.RecommendationState;

import java.io.IOException;

public class PetrolDataRetriever extends AsyncTask<City, Void, RecommendationState> {
	private MainActivity activity;

	PetrolDataRetriever(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		activity.startProgress();
		super.onPreExecute();
	}

	@Override
	protected RecommendationState doInBackground(City... cities) {
		City city;
		try {
			city = cities[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			cancel(true);
			return null;
		}

		Document document = getDocument(city);
		if (document == null) {
			cancel(true);
			return null;
		}

		String imgUrl, imgAlt;
		Element intro, recommendation, graphInfo;
		try {
			String elementId = String.format("petrol-prices-in-%s",
					city.getCityName().toLowerCase());
			Element title = document.getElementById(elementId).parent();
			intro = title.nextElementSibling();
			recommendation = intro.nextElementSibling();

			// If current element is not bullet list of recommendation, assume no tip being given currently
			if (!recommendation.tagName().toLowerCase().equals("ul")) {
				// First child of parent was actually graphInfo
				// recommendation and intro do not exist
				graphInfo = intro;
				recommendation = null;
				intro = null;
			} else {
				graphInfo = recommendation.nextElementSibling();
			}

			Element image = graphInfo.nextElementSibling().child(0);
			if (image.tagName().equals("img")) {
				imgUrl = graphInfo
						.nextElementSibling().child(0).absUrl("src");
				imgAlt = graphInfo
						.nextElementSibling().child(0).absUrl("alt");
			} else {
				cancel(true);
				return null;
			}
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return null;
		}

		if (recommendation == null) {
			return new RecommendationState("No buying tips are given while the ACCC is closed down for holidays.",
					"Graphs are still updated daily.",
					graphInfo.html(),
					imgAlt);
		}

		return new RecommendationState(intro.html(),
				recommendation.html(),
				graphInfo.html(),
				imgAlt);
	}

	@Override
	public void onCancelled() {
		activity.failedProgress();
	}

	@Override
	protected void onPostExecute(RecommendationState state) {
		activity.endProgress();

		String intro = state.introText.get();
		String recommendation = state.buyingRecommendation.get();
		String info = state.graphInfo.get();

		activity.setTextResults(intro, recommendation, info);
	}

	private Document getDocument(City city) {
		try {
			return Jsoup.connect(activity.getString(R.string.url)).get();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
