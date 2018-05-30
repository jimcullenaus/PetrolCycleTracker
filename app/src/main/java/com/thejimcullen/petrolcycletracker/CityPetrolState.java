package com.thejimcullen.petrolcycletracker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class CityPetrolState {
	private String city;
	private Element recommendation;
	private String imgUrl;

	public CityPetrolState(String city) {
		this.city = city;
		checkAcccPage();
	}

	public void checkAcccPage() {
		Document document = null;
		try {
			document = Jsoup.connect("https://www.accc.gov.au/consumers/petrol-diesel-lpg/petrol-price-cycles").get();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		Element title = document.getElementById(String.format("petrol-prices-in-%s",city)).parent();
		recommendation = title.nextElementSibling().nextElementSibling();
		Element image = recommendation.nextElementSibling().nextElementSibling().child(0);
		if (image.tagName().equals("img")) {
			imgUrl = recommendation.nextElementSibling().nextElementSibling().child(0).absUrl("src");
		} else {
			imgUrl = "image not found";
		}
	}

	public String getCity() {
		return city;
	}

	public Element getRecommendation() {
		return recommendation;
	}

	public String getImgUrl() {
		return imgUrl;
	}
}
