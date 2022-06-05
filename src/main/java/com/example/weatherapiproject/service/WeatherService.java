package com.example.weatherapiproject.service;

import com.example.weatherapiproject.model.Weather;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class WeatherService {

    public JsonNode findCurrentWeather() throws JsonProcessingException {
        RestTemplate currentWeather = new RestTemplate();
        String fooPlaceInTheMap
                = "https://api.openweathermap.org/data/3.0/" +
                "onecall?lat=45.4781435610314&lon=-75.70370247743809&units=metric&exclude=hourly,daily&lang=ru" +
                "&appid=03eac6778286898f5f733e498b63801d";
        ResponseEntity<String> responseForWeather
                = currentWeather.getForEntity(fooPlaceInTheMap, String.class);

        ObjectMapper mapperWeather = new ObjectMapper();
        JsonNode rootWeather = mapperWeather.readTree(responseForWeather.getBody());

        return rootWeather;
    }
    public JsonNode findCurrentPlaceInTheMap() throws JsonProcessingException {
        RestTemplate currentPlaceInTheMap = new RestTemplate();
        String fooWeather
                = "https://maps.googleapis.com/maps/api/" +
                "geocode/json?latlng=45.4781435610314,-75.70370247743809" +
                "&key=AIzaSyBSBnmfLtZcqKQPybREkz_-p8F4R_HzIiE";
        ResponseEntity<String> responseForPlaceInTheMap
                = currentPlaceInTheMap.getForEntity(fooWeather, String.class);

        ObjectMapper mapperPlaceInTheMap = new ObjectMapper();
        JsonNode rootPlaceInTheMap = mapperPlaceInTheMap.readTree(responseForPlaceInTheMap.getBody());

        return rootPlaceInTheMap;
    }

    public static class CurrentWeather {
        private static JsonNode temperature;
        private static JsonNode feelsLike;
        private static String description;
        private static JsonNode windSpeed;

        public static JsonNode getTemperature() {
            return temperature;
        }

        public void setTemperature(JsonNode temperature) {
            this.temperature = temperature;
        }

        public static JsonNode getFeelsLike() {
            return feelsLike;
        }

        public void setFeelsLike(JsonNode feelsLike) {
            this.feelsLike = feelsLike;
        }

        public static String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public static JsonNode getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(JsonNode windSpeed) {
            this.windSpeed = windSpeed;
        }
    }

    public static class CurrentPlaceInTheMap {
        private static String cityName;
        private static String countryName;

        public static String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public static String getCountryName() { return countryName; }

        public void setCountryName(String countryName) { this.countryName = countryName; }
    }

    public Weather findCurrentResult() throws JsonProcessingException {
        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setTemperature(findCurrentWeather().findPath("temp"));
        currentWeather.setFeelsLike(findCurrentWeather().findPath("feels_like"));
        currentWeather.setDescription(findCurrentWeather().findPath("description").textValue());
        currentWeather.setWindSpeed(findCurrentWeather().findPath("wind_speed"));

        ObjectMapper mapper = new ObjectMapper();

        CurrentPlaceInTheMap currentPlaceInTheMap = new CurrentPlaceInTheMap();
        JsonNode node = findCurrentPlaceInTheMap().findPath("address_components");
        Iterator<JsonNode> iteratorOfNode = node.iterator();
        while (iteratorOfNode.hasNext()) {
            JsonNode element = iteratorOfNode.next();
            String type = element.findPath("types").elements().next().textValue();
            if (type.equals("locality")) {
                currentPlaceInTheMap.setCityName(element.findPath("long_name").textValue());
            }
            if (type.equals("country")) {
                currentPlaceInTheMap.setCountryName(element.findPath("long_name").textValue());
            }
        }

        Weather currentResult = new Weather();
        currentResult.setTemperature(String.valueOf(CurrentWeather.getTemperature()));
        currentResult.setFeelsLike(String.valueOf(CurrentWeather.getFeelsLike()));
        currentResult.setDescription(CurrentWeather.getDescription());
        currentResult.setWindSpeed(String.valueOf(CurrentWeather.getWindSpeed()));
        currentResult.setCityName(CurrentPlaceInTheMap.getCityName());
        currentResult.setCountryName(CurrentPlaceInTheMap.getCountryName());

        return currentResult;
    }
}
