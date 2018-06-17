package com.vodafone.group.demo.mday.weatherbackend.model.weathermap;

import lombok.Data;

@Data
public class WeatherEntity {
	private Long id;
	private String main;
	private String description;
	private String icon;

}
