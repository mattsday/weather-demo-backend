package com.vodafone.group.demo.mday.weatherbackend.model.weathermap;

import java.util.List;

import lombok.Data;

@Data
public class ForecastEntity {
	private Long dt;
	private Main main;
	private List<WeatherEntity> weather;
	private Clouds clouds;
	private Wind wind;
	private ForecastSys sys;
	private String dt_txt;
}
