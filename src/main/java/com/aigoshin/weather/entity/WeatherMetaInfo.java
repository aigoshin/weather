package com.aigoshin.weather.entity;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "weather_meta_info")
public class WeatherMetaInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "temperature")
    private float temperature;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "city_id")
    private City city;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
