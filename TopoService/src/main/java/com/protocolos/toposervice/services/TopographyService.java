package com.protocolos.toposervice.services;

import com.protocolos.toposervice.dtos.TopographyDTO;
import reactor.core.publisher.Mono;

public interface TopographyService {
    Mono<TopographyDTO> findById(String id);
}
