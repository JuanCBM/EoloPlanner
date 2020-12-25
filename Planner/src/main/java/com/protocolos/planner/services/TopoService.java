package com.protocolos.planner.services;

import com.protocolos.planner.models.Topography;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface TopoService {

    @Async
    CompletableFuture<Topography> getTopographicDetails(String city);

}