package com.protocolos.planner.services;

import com.protocolos.eoloplanner.Weather;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;

public interface WeatherService {

    @Async
    CompletableFuture<Weather> getWeatherDetails(String city) throws Exception;

}
