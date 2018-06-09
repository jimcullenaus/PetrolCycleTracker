package com.thejimcullen.petrolcycletracker;

import android.content.res.Resources;

enum City {
//	SYDNEY (Resources.getSystem().getString(R.string.sydney)),
//	MELBOURNE (Resources.getSystem().getString(R.string.melbourne)),
//	BRISBANE (Resources.getSystem().getString(R.string.brisbane)),
//	ADELAIDE (Resources.getSystem().getString(R.string.adelaide)),
//	PERTH (Resources.getSystem().getString(R.string.perth));
	SYDNEY ("Sydney"),
	MELBOURNE ("Melbourne"),
	BRISBANE ("Brisbane"),
	ADELAIDE ("Adelaide"),
	PERTH ("Perth");

	private String cityName;

	City(String name) {
		this.cityName = name;
	}

	public String cityName() {
		return cityName;
	}

	public static City getCity(int ref) {
		for (City city : values()) {
			if (city.ordinal() == ref) {
				return city;
			}
		}
		return null;
	}
}
