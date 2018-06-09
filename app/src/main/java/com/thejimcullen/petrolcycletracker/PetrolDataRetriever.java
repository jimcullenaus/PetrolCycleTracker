package com.thejimcullen.petrolcycletracker;

import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class PetrolDataRetriever  extends AsyncTask<City, Void, CityPetrolState> {

	private MainActivity activity;
	private City city;

	PetrolDataRetriever(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		activity.startProgress();
		super.onPreExecute();
	}

	@Override
	public CityPetrolState doInBackground(City... params) {
		try {
			city = params[0];
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
		String imgAlt;
		Element recommendation;
		Element leadInText;
		Element info;
		try {
			String elementId = String.format("petrol-prices-in-%s", city.cityName().toLowerCase());
			Element title = document.getElementById(elementId).parent();
			leadInText = title.nextElementSibling();
			recommendation = leadInText.nextElementSibling();
			info = recommendation.nextElementSibling();
			Element image = info.nextElementSibling().child(0);
			if (image.tagName().equals("img")) {
				imgUrl = recommendation.nextElementSibling().nextElementSibling().child(0).absUrl("src");
				imgAlt = recommendation.nextElementSibling().nextElementSibling().child(0).absUrl("alt");
			} else {
				cancel(true);
				return null;
			}
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			cancel(true);
			return null;
		}

		return new CityPetrolState(imgUrl, imgAlt, leadInText.html(), recommendation.html(), info.html());
	}

	@Override
	public void onCancelled() {
		activity.setmBuyingRecommendation("Unable to obtain data");
		activity.failedProgress();
	}

	@Override
	public void onPostExecute(CityPetrolState state) {
		activity.endProgress();

		String introText = state.getLeadInText();
		String recommendation = state.getRecommendation();
		String graphInfo = state.getInfo().replaceAll("below", "above");

		String result = state.getLeadInText() + //System.getProperty("line.separator") +
				state.getRecommendation() + //System.getProperty("line.separator") +
				state.getInfo(); //+ System.getProperty("line.separator") +
				//state.getImageUrl();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			activity.setTextResults(Html.fromHtml(introText, Html.FROM_HTML_MODE_COMPACT),
					Html.fromHtml(recommendation, Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST),
					Html.fromHtml(graphInfo, Html.FROM_HTML_MODE_COMPACT));
		} else {
			activity.setTextResults(Html.fromHtml(introText),
					Html.fromHtml(recommendation),
					Html.fromHtml(graphInfo));
		}
		new ImageDownloader(activity.mPriceGraph, state).execute(state.getImageUrl());
	}


}
