package com.vodafone.group.demo.mday.weatherbackend.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vodafone.group.demo.mday.weatherbackend.model.weathermap.WeatherForecast;

@Repository
public interface CacheRepo extends CrudRepository<WeatherForecast, String> {

}
