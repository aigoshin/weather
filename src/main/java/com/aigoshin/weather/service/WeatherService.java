package com.aigoshin.weather.service;

import com.aigoshin.weather.entity.City;
import com.aigoshin.weather.entity.WeatherMetaInfo;
import com.aigoshin.weather.model.WeatherResponseModel;
import com.aigoshin.weather.repository.CityRepository;
import com.aigoshin.weather.repository.WeatherMetaInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    @Value("${weather.url}")
    private String weatherUrl;

    @Value("${weather.api.key}")
    private String weatherApiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeatherMetaInfoRepository weatherMetaInfoRepository;

    @Autowired
    private CityRepository cityRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "${weather.loader.cron}")
    public void runJob() {
        HttpEntity<WeatherResponseModel> entity = new HttpEntity<>(httpHeaders());
        LocalDateTime now = LocalDateTime.now();

        Page<City> cities = cityRepository.findAll(PageRequest.of(0, 10));

        List<WeatherMetaInfo> weatherMetaInfoList = new ArrayList<>();
        for (City city : cities.getContent()) {
            WeatherResponseModel responseBody = getWeatherResponseModel(entity, city);
            if (responseBody != null) {
                weatherMetaInfoList.add(bindEntity(responseBody, city, now));
            }
        }

        if (!CollectionUtils.isEmpty(weatherMetaInfoList)) {
            weatherMetaInfoRepository.saveAll(weatherMetaInfoList);
        }
    }

    @Transactional(readOnly = true)
    public Page<WeatherMetaInfo> getWeatherMetaInfoPage(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return weatherMetaInfoRepository.findAllWithCity(pageable);
    }

    @Transactional
    public WeatherMetaInfo getWeatherMetaInfoByCity(String title, Double lat, Double lon) {
        City city = cityRepository.findOneByTitle(title);
        if (city == null) {
            if (lat == null || lon == null) {
                throw new IllegalArgumentException("If city is not found lat and lon should be specified");
            }
            city = new City();
            city.setLat(lat);
            city.setLon(lon);
            city.setTitle(title);
            cityRepository.save(city);

            HttpEntity<WeatherResponseModel> entity = new HttpEntity<>(httpHeaders());
            WeatherResponseModel weatherResponseModel = getWeatherResponseModel(entity, city);
            WeatherMetaInfo weatherMetaInfo = bindEntity(weatherResponseModel, city, LocalDateTime.now());
            return weatherMetaInfoRepository.saveAndFlush(weatherMetaInfo);
        } else {
            return weatherMetaInfoRepository.findTopByCityOrderByDateTimeDesc(city);
        }
    }

    private WeatherResponseModel getWeatherResponseModel(HttpEntity<WeatherResponseModel> entity, City city) {
        UriComponents urlBuilder = UriComponentsBuilder
                .fromHttpUrl(weatherUrl)
                .queryParam("lat", city.getLat())
                .queryParam("lat", city.getLon())
                .queryParam("hours", false)
                .build();

        ResponseEntity<WeatherResponseModel> response = restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, entity, WeatherResponseModel.class);
        return response.getBody();
    }

    private HttpHeaders httpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Yandex-API-Key", weatherApiKey);
        return httpHeaders;
    }

    private WeatherMetaInfo bindEntity(WeatherResponseModel model, City city, LocalDateTime now) {
        WeatherMetaInfo entity = new WeatherMetaInfo();
        entity.setTemperature(model.getFactObjectModel().getTemp());
        entity.setDateTime(now);
        entity.setCity(city);
        return entity;
    }
}
