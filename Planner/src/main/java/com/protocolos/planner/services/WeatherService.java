package com.protocolos.planner.services;

import com.protocolos.eoloplanner.Weather;

public interface WeatherService {
    Weather getWeatherDetails(String city) throws Exception;
}
