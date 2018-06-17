package com.vodafone.group.demo.mday.weatherbackend.model.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WeatherReport {
	@JsonProperty("city_name")
	private String cityName;
	private List<WeatherEntry> weather;

}
