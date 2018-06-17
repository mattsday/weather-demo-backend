package com.vodafone.group.demo.mday.weatherbackend.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WeatherEntry {
	private String day;
	@JsonProperty("temp")
	private Integer temp;
	@JsonProperty("temp_max")
	private Integer tempMax;
	@JsonProperty("temp_min")
	private Integer tempMin;
	private String icon;
	@JsonProperty("wind_speed")
	private Double windSpeed;
	@JsonProperty("wind_degrees")
	private Long windDegrees;
	@JsonProperty("wind_direction")
	private String windDirection;
	private Long humidity;

}
