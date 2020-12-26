package com.protocolos.planner.services.impl;

import com.protocolos.eoloplanner.Weather;
import com.protocolos.planner.models.Topography;
import com.protocolos.planner.services.TopoService;
import com.protocolos.planner.services.WeatherService;
import com.protocolos.planner.services.WriterService;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ReceiverServiceImpl {
    public static final String RECEIVE_METHOD_NAME = "messageProcessor";

    private static final Logger logger = LoggerFactory.getLogger(ReceiverServiceImpl.class);

    private static final char LETTER_LIMIT = 'm';
    private static final int SEGMENT_PERCENTAGE = 25;
    private static final int MIN_DELAY = 1;
    private static final int MAX_DELAY = 3;

    private final TopoService topoService;
    private final WeatherService weatherService;
    private final WriterService writerService;

    public ReceiverServiceImpl(TopoService topoService, WeatherService weatherService, WriterService writerService) {
        this.topoService = topoService;
        this.weatherService = weatherService;
        this.writerService = writerService;
    }

    // TODO: Escribir en las colas el formato correcto.
    public void messageProcessor(String city) throws Exception {
        logger.info("[Receiver] Ha recibido el mensaje \"" + city + '"');

        final AtomicReference<String> cityConcatenation = new AtomicReference<>(city);
        final AtomicReference<Integer> percentage = new AtomicReference<>(0);

        // Asynchronous executions
        CompletableFuture<Topography> page1 = this.topoService.getTopographicDetails(city);
        CompletableFuture<Weather> page2 = this.weatherService.getWeatherDetails(city);
        logger.info("Looking up for {} weather and topography ", city);
        addSegmentPercentage(percentage);

        page1.thenAccept(topography -> {
            logger.info("[TOPOGRAPHY] Got topography detail from remote topo service {}", topography.getLandscape());
            addSegmentPercentage(percentage);
            // Escribir en la cola %
            cityConcatenation.set(cityConcatenation.get().concat(topography.getLandscape()));
            this.writerService.write(city+"_"+percentage);
        });

        page2.thenAccept(weather -> {
            logger.info("[WEATHER] Got weather detail from remote weather service {}", weather.getWeather());
            addSegmentPercentage(percentage);
            // Escribir en la cola %
            cityConcatenation.set(cityConcatenation.get().concat(weather.getWeather()));
            this.writerService.write(city+"_"+percentage);
        });

        // Wait until they are all done
        CompletableFuture.allOf(page1, page2).join();
        TimeUnit.SECONDS.sleep(RandomUtils.nextInt(MIN_DELAY, MAX_DELAY + 1));
        addSegmentPercentage(percentage);

        if(city.toLowerCase().charAt(0) <= LETTER_LIMIT){
            // Escribir en la cola %
            logger.info("--> {} {}%", cityConcatenation.get().toLowerCase(), percentage);
            this.writerService.write(city+"_"+percentage);
        }else{
            // Escribir en la cola %
            logger.info("--> {} {}%", cityConcatenation.get().toUpperCase(), percentage);
            this.writerService.write(city+"_"+percentage);
        }

    }

    private void addSegmentPercentage(AtomicReference<Integer> percentage) {
        percentage.set(percentage.get() + SEGMENT_PERCENTAGE);
        logger.info("--> {}%", percentage);
    }

}