package com.sparta.upgradeschedulemanagement.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.upgradeschedulemanagement.dto.WeatherDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String getWeatherData(String date) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://f-api.github.io/")
                .path("/f-api/weather.json")
                .encode()
                .build()
                .toUri();
        log.info("url = " + uri);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
        return getWeather(responseEntity.getBody(), date);

    }

    public String getWeather(String jsonValue, String date) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<WeatherDto> weatherList = objectMapper.readValue(jsonValue, new TypeReference<List<WeatherDto>>() {});
            for (WeatherDto weatherDto : weatherList) {
                if (weatherDto.getDate().equals(date)) {
                    return weatherDto.getWeather();
                }
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("날짜 형식이 맞지 않습니다.");
        }
        throw new RuntimeException();
    }
}
