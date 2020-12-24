package com.protocolos.planner.services.impl;

import com.protocolos.eoloplanner.Weather;
import com.protocolos.planner.models.Topography;
import com.protocolos.planner.services.TopoService;
import com.protocolos.planner.services.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ReceiverServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(ReceiverServiceImpl.class);
    public static final String RECEIVE_METHOD_NAME = "receiveMessage";

    private final RestTemplate restTemplate;
    private final TopoService topoService;
    private final WeatherService weatherService;

    public ReceiverServiceImpl(RestTemplateBuilder restTemplateBuilder, TopoService topoService, WeatherService weatherService) {
        this.restTemplate = restTemplateBuilder.build();
        this.topoService = topoService;
        this.weatherService = weatherService;
    }

    public void receiveMessage(String city) throws Exception {
        logger.info("[Receiver] Ha recibido el mensaje \"" + city + '"');
        // Start the clock
        long start = System.currentTimeMillis();

        // Kick of multiple, asynchronous lookups
        CompletableFuture<Topography> page1 = this.findTopography(city);
        CompletableFuture<Weather> page2 = this.findWeather(city);


        // Wait until they are all done (page1, page2)
        // CompletableFuture.allOf(page1, page2).join();

        // Print results, including elapsed time
        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
        logger.info("--> " + page1.get());
        logger.info("Elapsed time2: " + (System.currentTimeMillis() - start));
        logger.info("--> " + page2.get());
    }


    @Async
    public CompletableFuture<Topography> findTopography(String city) throws InterruptedException {
        logger.info("Looking up for {} topography", city);
        Topography result = this.topoService.getTopographicDetails(city);

        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<Weather> findWeather(String city) throws Exception {
        logger.info("Looking up for {} weather", city);
        Weather result = this.weatherService.getWeatherDetails(city);

        return CompletableFuture.completedFuture(result);
    }
}