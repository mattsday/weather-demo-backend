package com.vodafone.group.demo.mday.weatherbackend.model.weathermap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Main {

	private Double temp;
	private Long pressure;
	private Long humidity;
	@JsonProperty("temp_min")
	private Double tempMin;
	@JsonProperty("temp_max")
	private Double tempMax;

}
