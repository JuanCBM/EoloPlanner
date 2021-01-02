package com.protocolos.planner.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.protocolos.eoloplanner.Weather;
import com.protocolos.planner.configurations.CreationQueueConfig;
import com.protocolos.planner.exceptions.ProcessingMessageException;
import com.protocolos.planner.models.City;
import com.protocolos.planner.models.Topography;
import com.protocolos.planner.services.TopoService;
import com.protocolos.planner.services.WeatherService;
import com.protocolos.planner.services.WriterService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ReceiverServiceImpl {

  private static final Logger logger = LoggerFactory.getLogger(ReceiverServiceImpl.class);

  private static final char LETTER_LIMIT = 'm';
  private static final int SEGMENT_PERCENTAGE = 25;
  private static final int MIN_DELAY = 1;
  private static final int MAX_DELAY = 3;
  private static final Integer INITIAL_PERCENTAGE = 0;
  private static final Integer MAX_PERCENTAGE = 100;

  private static final ObjectMapper mapper = new ObjectMapper();
  private final TopoService topoService;
  private final WeatherService weatherService;
  private final WriterService writerService;

  public ReceiverServiceImpl(TopoService topoService, WeatherService weatherService,
      WriterService writerService) {
    this.topoService = topoService;
    this.weatherService = weatherService;
    this.writerService = writerService;
  }

  @RabbitListener(queues = CreationQueueConfig.QUEUE_CREATION_NAME, ackMode = "AUTO")
  public void messageProcessor(String message) throws Exception {
    logger.info("[Receiver] Ha recibido el mensaje \"" + message + '"');

    City city = configureInitialCityData(message);
    final AtomicReference<String> cityConcatenation = new AtomicReference<>(city.getCity());
    final AtomicReference<Integer> percentage = new AtomicReference<>(INITIAL_PERCENTAGE);

    logger.info("[Receiver] Ha recibido la ciudad: {}", city);

    // Asynchronous executions
    CompletableFuture<Topography> page1 = this.topoService.getTopographicDetails(city.getCity());
    CompletableFuture<Weather> page2 = this.weatherService.getWeatherDetails(city.getCity());
    logger.info("Looking up for {} weather and topography ", city.getCity());
    percentage.set(percentage.get() + SEGMENT_PERCENTAGE);

    page1.thenAccept(topography -> {
      logger.info("[TOPOGRAPHY] Got topography detail from remote topo service {}",
          topography.getLandscape());
      writeMessageToNotificationQueue(percentage, cityConcatenation, city,
          topography.getLandscape());
    });

    page2.thenAccept(weather -> {
      logger.info("[WEATHER] Got weather detail from remote weather service {}",
          weather.getWeather());
      writeMessageToNotificationQueue(percentage, cityConcatenation, city, weather.getWeather());
    });

    // Wait until they are all done
    CompletableFuture.allOf(page1, page2).join();
    TimeUnit.SECONDS.sleep(RandomUtils.nextInt(MIN_DELAY, MAX_DELAY + 1));

    writeMessageToNotificationQueue(percentage, cityConcatenation, city, StringUtils.EMPTY);

  }

  private void writeMessageToNotificationQueue(AtomicReference<Integer> percentage,
      AtomicReference<String> cityConcatenation,
      City city,
      String message) {
    percentage.set(percentage.get() + SEGMENT_PERCENTAGE);
    city.setProgress(percentage.get());

    cityConcatenation.set(cityConcatenation.get().concat(message));

    if (MAX_PERCENTAGE.equals(city.getProgress())) {
      city.setCompleted(Boolean.TRUE);
      if (city.getCity().toLowerCase().charAt(0) <= LETTER_LIMIT) {
        city.setPlanning(cityConcatenation.get().toLowerCase());
      } else {
        city.setPlanning(cityConcatenation.get().toUpperCase());
      }
    }

    try {
      this.writerService.write(mapper.writeValueAsString(city));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      logger.error("[ERROR] error procesando los datos {}", e);
      throw new ProcessingMessageException();
    }

  }

  private City configureInitialCityData(String message) throws JsonProcessingException {
    City city = mapper.readValue(message, City.class);
    city.setCompleted(Boolean.FALSE);
    city.setProgress(INITIAL_PERCENTAGE);

    return city;
  }

}