package com.vodafone.group.demo.mday.weatherbackend.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vodafone.group.demo.mday.weatherbackend.model.weathermap.WeatherForecast;
import com.vodafone.group.demo.mday.weatherbackend.repo.CacheRepo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WeatherMapService {

	@NonNull
	private RestTemplate restTemplate;
	@Value("${weather.api.uri}")
	private String baseUri;
	@Value("${weather.api.key}")
	private String apiKey;

	// Time to expire cache in milliseconds (default = 1 hour)
	private Long expire = 3600000L;

	@NonNull
	private CacheRepo cacheRepo;

	public WeatherForecast getWeather(String city) {
		WeatherForecast weather = null;

		// Look up entry in cache
		if (cacheRepo.findById(city).isPresent()) {
			Date now = new Date();
			weather = cacheRepo.findById(city).get();

			// Check if we need to re-fetch
			Date updated = weather.getUpdated();
			long delta = now.getTime() - updated.getTime();
			if (delta < expire) {
				System.out.println("Weather is still cached - " + now + " is better than " + updated
						+ " - with a expiration of " + new Date(updated.getTime() + expire));
				return weather;
			}
		}
		weather = getWeatherFromService(city);
		weather.setUpdated(new Date());
		cacheRepo.save(weather);
		return weather;
	}

	private WeatherForecast getWeatherFromService(String city) {
		String uri = baseUri + city + "&APPID=" + apiKey;
		WeatherForecast weather = restTemplate.getForObject(uri, WeatherForecast.class);
		weather.setCityName(city);
		return weather;
	}

}
