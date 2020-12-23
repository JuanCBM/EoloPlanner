package com.protocolos.planner.services;

import com.protocolos.planner.Weather;

public interface WeatherService {
    Weather getWeatherDetails(String city) throws Exception;
}
