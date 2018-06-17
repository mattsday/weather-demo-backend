package com.vodafone.group.demo.mday.weatherbackend.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.vodafone.group.demo.mday.weatherbackend.model.api.WeatherReport;
import com.vodafone.group.demo.mday.weatherbackend.services.WeatherApiService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApiController {

	@Value("${weather.default.city}")
	private String defaultCity;

	@NonNull
	private WeatherApiService weatherService;

	@GetMapping("/")
	public WeatherReport getWeather() {
		return weatherService.getWeather(defaultCity);
	}

	@GetMapping("/{city}")
	public WeatherReport getWeather(@PathVariable("city") String city) {
		return weatherService.getWeather(city);
	}

}