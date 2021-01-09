package com.protocolos.planner.services;

import com.protocolos.planner.models.Topography;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;

public interface TopoService {

    @Async
    CompletableFuture<Topography> getTopographicDetails(String city);

}