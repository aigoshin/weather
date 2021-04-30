package com.aigoshin.weather.repository;

import com.aigoshin.weather.entity.City;
import com.aigoshin.weather.entity.WeatherMetaInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface WeatherMetaInfoRepository extends JpaRepository<WeatherMetaInfo, Integer> {


    @Query(value = "SELECT *\n" +
            "FROM weather_meta_info wmi\n" +
            "         JOIN(\n" +
            "    SELECT city_id, MAX(date_time) AS date_time\n" +
            "    FROM weather_meta_info\n" +
            "    GROUP BY city_id\n" +
            ") wmi2 ON wmi.city_id = wmi2.city_id AND wmi.date_time = wmi2.date_time\n" +
            "         JOIN city c ON wmi.city_id = c.id ORDER BY c.title ",
            countQuery = "SELECT COUNT(-1)\n" +
                    "FROM weather_meta_info wmi\n" +
                    "         JOIN(\n" +
                    "    SELECT city_id, MAX(date_time) AS date_time\n" +
                    "    FROM weather_meta_info\n" +
                    "    GROUP BY city_id\n" +
                    ") wmi2 ON wmi.city_id = wmi2.city_id AND wmi.date_time = wmi2.date_time\n" +
                    "         JOIN city c ON wmi.city_id = c.id ", nativeQuery = true)
    Page<WeatherMetaInfo> findAllWithCity(Pageable page);

    WeatherMetaInfo findTopByCityOrderByDateTimeDesc(City city);
}
