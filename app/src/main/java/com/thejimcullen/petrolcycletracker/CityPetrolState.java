package com.thejimcullen.petrolcycletracker;

public class CityPetrolState {
	private String imageUrl;
	private String leadInText;
	private String recommendation;
	private String info;

	public CityPetrolState(String imageUrl, String leadInText, String recommendation, String info) {
		this.imageUrl = imageUrl;
		this.leadInText = leadInText;
		this.recommendation = recommendation;
		this.info = info;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getRecommendation() {
		return recommendation;
	}

	public String getLeadInText() {
		return leadInText;
	}

	public String getInfo() {
		return info;
	}
}
