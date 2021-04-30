package com.aigoshin.weather.repository;

import com.aigoshin.weather.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Integer> {

    City findOneByTitle(String title);

}
