package com.protocolos.toposervice.controllers;

import com.protocolos.toposervice.dtos.TopographyDTO;
import com.protocolos.toposervice.services.TopographyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/topographicdetails")
public class TopographyController {

    private final TopographyService topographyService;

    public TopographyController(TopographyService topographyService) {
        this.topographyService = topographyService;
    }

    @GetMapping("/{id}")
    public Mono<TopographyDTO> getUser(@PathVariable String id){
        return topographyService.findById(id);
    }
}
