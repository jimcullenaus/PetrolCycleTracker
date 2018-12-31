package com.thejimcullen.petrolcycletracker.models;

import android.databinding.ObservableField;

public class RecommendationState {
	public final ObservableField<String> introText = new ObservableField<>();
	public final ObservableField<String> buyingRecommendation = new ObservableField<>();
	public final ObservableField<String> graphInfo = new ObservableField<>();
	public final ObservableField<String> imgAlt = new ObservableField<>();
	private City selectedCity;

	public City getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(City selectedCity) {
		this.selectedCity = selectedCity;
	}

	public RecommendationState() {
		this.introText.set("");
		this.buyingRecommendation.set("");
		this.graphInfo.set("");
		this.imgAlt.set("");
	}

	public RecommendationState(String introText,
	                           String recommendation,
	                           String graphInfo,
	                           String imgAlt) {
		this.introText.set(introText);
		this.buyingRecommendation.set(recommendation);
		this.graphInfo.set(graphInfo);
		this.imgAlt.set(imgAlt);
	}
}
