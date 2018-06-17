package com.vodafone.group.demo.mday.weatherbackend.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.stereotype.Service;

import com.vodafone.group.demo.mday.weatherbackend.model.api.WeatherEntry;
import com.vodafone.group.demo.mday.weatherbackend.model.api.WeatherReport;
import com.vodafone.group.demo.mday.weatherbackend.model.weathermap.ForecastEntity;
import com.vodafone.group.demo.mday.weatherbackend.model.weathermap.WeatherForecast;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherApiService {

	@NonNull
	private WeatherMapService weatherMapService;

	public WeatherReport getWeather(String city) {
		WeatherReport report = new WeatherReport();
		report.setCityName(city);

		WeatherForecast forecast = weatherMapService.getWeather(city);

		// Boostrap weather list:

		List<WeatherEntry> weatherList = createList(
				LocalDateTime.ofInstant(Instant.ofEpochMilli(forecast.getList().get(0).getDt() * 1000L),
						TimeZone.getDefault().toZoneId()),
				forecast.getList());

		report.setWeather(weatherList);

		return report;
	}

	// Create a list of weather entries for the next 5 days
	private List<WeatherEntry> createList(LocalDateTime start, List<ForecastEntity> forecast) {
		int day = start.getDayOfWeek().getValue();
		List<WeatherEntry> list = new ArrayList<>(6);
		// Get Today's weather
		WeatherEntry today = new WeatherEntry();
		today.setTemp(convertFromKelvin(forecast.get(0).getMain().getTemp()));
		today.setHumidity(forecast.get(0).getMain().getHumidity());
		today.setWindDegrees(forecast.get(0).getWind().getDeg().longValue());
		today.setWindDirection(getDirection(today.getWindDegrees()));
		today.setWindSpeed(forecast.get(0).getWind().getSpeed());
		today.setIcon(forecast.get(0).getWeather().get(0).getIcon());
		today.setDay("Today");
		list.add(0, today);

		// Loop through for the rest of today and later
		for (int i = 1; i < 6; i++) {
			WeatherEntry entry = new WeatherEntry();
			list.add(i, entry);
			entry.setDay(DayOfWeek.of(day).getDisplayName(TextStyle.FULL, Locale.UK));
			// For today only set the temp:
			if (i == 1) {
				entry.setDay("Later");
			}

			Map<String, Integer> iconScore = new HashMap<>();

			int maxTemp = -100;
			int minTemp = 100;

			List<Long> humidity = new ArrayList<>();
			List<Long> windDegrees = new ArrayList<>();
			List<Double> windSpeed = new ArrayList<>();

			// Now extract all entries for this day:
			for (ForecastEntity f : forecast) {
				// Convert timestamp to date
				LocalDateTime entryDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(f.getDt() * 1000L),
						TimeZone.getDefault().toZoneId());
				if (entryDate.getDayOfWeek().getValue() != day) {
					continue;
				}
				int temp = convertFromKelvin(f.getMain().getTemp());
				if (temp > maxTemp) {
					maxTemp = temp;
				}
				if (temp < minTemp) {
					minTemp = temp;
				}
				if (f.getWeather().size() > 0) {
					Integer currentScore = iconScore.get(f.getWeather().get(0).getIcon());
					if (currentScore == null) {
						iconScore.put(f.getWeather().get(0).getIcon(), 1);
					} else {
						iconScore.put(f.getWeather().get(0).getIcon(), currentScore + 1);
					}
				}
				humidity.add(f.getMain().getHumidity());
				windSpeed.add(f.getWind().getSpeed());
				windDegrees.add(f.getWind().getDeg().longValue());
			}
			// Score the icon
			String icon = "03d";
			int winning = 0;
			for (String key : iconScore.keySet()) {
				if (iconScore.get(key) > winning) {
					winning = iconScore.get(key);
					icon = key;
				}
			}
			// Average out values
			entry.setHumidity(getAverage(humidity));
			entry.setWindDegrees(getAverage(windDegrees));
			entry.setWindSpeed(getAverageDouble(windSpeed));
			entry.setWindDirection(getDirection(entry.getWindDegrees()));

			entry.setIcon(icon);

			entry.setTempMax(maxTemp);
			entry.setTempMin(minTemp);

			day++;
			if (day > 7) {
				day = 1;
			}
		}
		return list;
	}

	private String getDirection(Long degrees) {
		if (degrees > 339)
			return "North";
		if (degrees > 294)
			return "North West";
		if (degrees > 249)
			return "West";
		if (degrees > 204)
			return "South West";
		if (degrees > 159)
			return "South";
		if (degrees > 114)
			return "South East";
		if (degrees > 66)
			return "East";
		if (degrees > 21)
			return "North East";
		return "North";
	}

	private Long getAverage(List<Long> list) {
		Long total = 0L;
		for (Long i : list) {
			total += i;
		}
		BigDecimal bd = new BigDecimal(total);
		bd = bd.divide(new BigDecimal(list.size()), 2, RoundingMode.HALF_UP);
		return bd.longValue();
	}

	private Double getAverageDouble(List<Double> list) {
		Double total = 0D;
		for (Double i : list) {
			total += i;
		}
		BigDecimal bd = new BigDecimal(total);
		bd = bd.divide(new BigDecimal(list.size()), 2, RoundingMode.HALF_UP);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	private Integer convertFromKelvin(Double kelvin) {
		BigDecimal bd = new BigDecimal(kelvin - 273.15d);
		bd = bd.setScale(0, RoundingMode.HALF_UP);
		return bd.intValue();
	}

}