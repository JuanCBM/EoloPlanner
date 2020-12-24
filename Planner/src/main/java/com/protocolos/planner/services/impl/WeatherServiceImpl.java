package com.protocolos.planner.services.impl;

import com.protocolos.eoloplanner.GetWeatherRequest;
import com.protocolos.eoloplanner.Weather;
import com.protocolos.eoloplanner.WeatherServiceGrpc.WeatherServiceBlockingStub;
import com.protocolos.planner.services.WeatherService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WeatherServiceImpl implements WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @GrpcClient("weatherServer")
    private WeatherServiceBlockingStub client;

    @Override
    public Weather getWeatherDetails(String city) throws Exception {
        logger.info("Get Weather Details {}", city);
        GetWeatherRequest request = GetWeatherRequest.newBuilder()
                .setCity(city)
                .build();

        Weather responseWeather = this.client.getWeather(request);

        System.out.println("Response received from server:\n" + responseWeather);

        return responseWeather;
    }
}
