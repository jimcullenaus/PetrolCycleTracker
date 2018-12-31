package com.thejimcullen.petrolcycletracker.models;

public enum City {
	BRISBANE ("Brisbane"),
	SYDNEY ("Sydney"),
	MELBOURNE ("Melbourne"),
	ADELAIDE ("Adeilaide");

	private String cityName;

	City(String name) {
		this.cityName = name;
	}

	public String getCityName() {
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
