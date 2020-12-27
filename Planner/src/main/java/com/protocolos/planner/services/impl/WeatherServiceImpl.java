package com.protocolos.planner.services.impl;

import com.protocolos.eoloplanner.GetWeatherRequest;
import com.protocolos.eoloplanner.Weather;
import com.protocolos.eoloplanner.WeatherServiceGrpc.WeatherServiceBlockingStub;
import com.protocolos.planner.services.WeatherService;
import java.util.concurrent.CompletableFuture;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WeatherServiceImpl implements WeatherService {

  @GrpcClient("weatherServer")
  private WeatherServiceBlockingStub client;

  @Async
  @Override
  public CompletableFuture<Weather> getWeatherDetails(String city) throws Exception {

    GetWeatherRequest request = GetWeatherRequest.newBuilder()
        .setCity(city)
        .build();

    Weather responseWeather = this.client.getWeather(request);

    return CompletableFuture.completedFuture(responseWeather);
  }
}
