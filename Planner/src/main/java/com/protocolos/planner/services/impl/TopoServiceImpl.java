package com.protocolos.planner.services.impl;

import com.protocolos.planner.models.Topography;
import com.protocolos.planner.services.TopoService;
import java.util.concurrent.CompletableFuture;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TopoServiceImpl implements TopoService {

  private final RestTemplate restTemplate;

  public TopoServiceImpl(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  @Async
  @Override
  public CompletableFuture<Topography> getTopographicDetails(String city) {
    String url = String.format("http://localhost:8080/api/topographicdetails/%s", city);
    Topography results = restTemplate.getForObject(url, Topography.class);
    return CompletableFuture.completedFuture(results);
  }
}
