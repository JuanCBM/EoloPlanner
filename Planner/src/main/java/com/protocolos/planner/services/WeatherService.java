package com.protocolos.planner.services;

import com.protocolos.eoloplanner.Weather;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface WeatherService {

    @Async
    CompletableFuture<Weather> getWeatherDetails(String city) throws Exception;

}
