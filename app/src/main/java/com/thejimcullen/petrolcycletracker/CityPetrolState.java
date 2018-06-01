package com.thejimcullen.petrolcycletracker;

public class CityPetrolState {
	private String imgAlt;
	private String imageUrl;
	private String leadInText;
	private String recommendation;
	private String info;

	CityPetrolState(String imageUrl, String imgAlt, String leadInText, String recommendation, String info) {
		this.imageUrl = imageUrl;
		this.leadInText = leadInText;
		this.recommendation = recommendation;
		this.info = info;
		this.imgAlt = imgAlt;
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

	public String getImgAlt() {
		return imgAlt;
	}
}
