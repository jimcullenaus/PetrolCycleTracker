package com.thejimcullen.petrolcycletracker.models;

import android.databinding.ObservableField;

public class RecommendationState {
	public final ObservableField<String> introText = new ObservableField<>();
	public final ObservableField<String> buyingRecommendation = new ObservableField<>();
	public final ObservableField<String> graphInfo = new ObservableField<>();

	public RecommendationState() {
		this.introText.set("intro text");
		this.buyingRecommendation.set("buying recommendation");
		this.graphInfo.set("graph info");
	}
}
