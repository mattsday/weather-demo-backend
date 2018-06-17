package com.vodafone.group.demo.mday.weatherbackend.model.weathermap;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@RedisHash("WeatherForecast")
public class WeatherForecast {
	@Id
	@JsonProperty("city_name")
	private String cityName;
	private String cod;
	private Double message;
	private Long cnt;
	private Date updated;
	private List<ForecastEntity> list;
	private City city;
}
