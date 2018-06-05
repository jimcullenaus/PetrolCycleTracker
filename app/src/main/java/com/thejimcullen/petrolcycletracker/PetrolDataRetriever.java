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
		String cityName;
		try {
			cityName = params[0].cityName();
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
			String elementId = String.format("petrol-prices-in-%s", cityName.toLowerCase());
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
		activity.setmMainText("Unable to obtain data");
		activity.failedProgress();
	}

	@Override
	public void onPostExecute(CityPetrolState state) {
		activity.endProgress();

		String result = state.getLeadInText() + //System.getProperty("line.separator") +
				state.getRecommendation() + //System.getProperty("line.separator") +
				state.getInfo(); //+ System.getProperty("line.separator") +
				//state.getImageUrl();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			activity.setmMainText(Html.fromHtml(result, Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST));
		} else {
			activity.setmMainText(Html.fromHtml(result));
		}
		new ImageDownloader(activity.mPriceGraph, state).execute(state.getImageUrl());
	}


}
