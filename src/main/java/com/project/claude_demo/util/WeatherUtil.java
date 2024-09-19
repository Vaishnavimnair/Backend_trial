package com.project.claude_demo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@Component
public class WeatherUtil {
    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public WeatherUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    private static final Logger logger = LoggerFactory.getLogger(WeatherUtil.class);
    public Map<String, Object> getWeatherForLocation(String location) {
        String url = apiUrl + "?q=" + location + "&appid=" + apiKey;
        logger.debug("Requesting weather data from URL: {}", url);
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            logger.debug("Received response: {}", response.getBody());
            return response.getBody();
        } catch (RestClientException e) {
            logger.error("Error fetching weather data for location: " + location, e);
            throw new RuntimeException("Error fetching weather data: " + e.getMessage(), e);
        }
    }
}