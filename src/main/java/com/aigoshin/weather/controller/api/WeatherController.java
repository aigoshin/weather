package com.aigoshin.weather.controller.api;

import com.aigoshin.weather.entity.WeatherMetaInfo;
import com.aigoshin.weather.service.WeatherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/weather")
@RestController
@Api(tags = "Weather Controller")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    @ApiOperation("Endpoint to get weather info by city")
    public ResponseEntity<WeatherMetaInfo> getWeatherMetaInfo(@RequestParam String title,
                                                              @RequestParam(required = false) Double lat,
                                                              @RequestParam(required = false) Double lon) {
        return new ResponseEntity<>(weatherService.getWeatherMetaInfoByCity(title, lat, lon), HttpStatus.OK);
    }
}
