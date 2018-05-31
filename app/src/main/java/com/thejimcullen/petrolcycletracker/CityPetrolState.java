package com.thejimcullen.petrolcycletracker;

public class CityPetrolState {
	private String imageUrl;
	private String recommendation;

	public CityPetrolState(String imageUrl, String recommendation) {
		this.imageUrl = imageUrl;
		this.recommendation = recommendation;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getRecommendation() {
		return recommendation;
	}
}
