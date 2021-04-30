package com.aigoshin.weather.controller;

import com.aigoshin.weather.entity.WeatherMetaInfo;
import com.aigoshin.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class BaseController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(50);
        Page<WeatherMetaInfo> weatherMetaInfoPage = weatherService.getWeatherMetaInfoPage(currentPage, pageSize);
        model.addAttribute("weatherMetaInfoPage", weatherMetaInfoPage);

        int totalPages = weatherMetaInfoPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "index";
    }
}
