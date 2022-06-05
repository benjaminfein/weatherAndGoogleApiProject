package com.example.weatherapiproject.controller;

import com.example.weatherapiproject.model.Weather;
import com.example.weatherapiproject.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
    final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("api/currentWeatherAndPlace")
    public Weather findPlaceInTheMap() throws JsonProcessingException {
        return weatherService.findCurrentResult();
    }
}
