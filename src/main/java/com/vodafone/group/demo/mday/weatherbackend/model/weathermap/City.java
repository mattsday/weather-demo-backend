package com.vodafone.group.demo.mday.weatherbackend.model.weathermap;

import lombok.Data;

@Data
public class City {
	private Long id;
	private String name;
	private Coord coord;
	private String country;
	private Long population;
}
