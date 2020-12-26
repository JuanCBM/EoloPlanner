package com.protocolos.toposervice.utils;

import com.protocolos.toposervice.models.Topography;
import com.protocolos.toposervice.repositories.TopographyRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    private final TopographyRepository topographyRepository;

    DatabaseInitializer(TopographyRepository topographyRepository) {
        this.topographyRepository = topographyRepository;
    }

    @PostConstruct
    public void initDatabase() {
        this.topographyRepository.deleteAll();
        Flux<Topography> topographyFlux = Flux.just(
                new Topography("Madrid", "flat"),
                new Topography("Barcelona", "flat"),
                new Topography("Soria", "montain"),
                new Topography("Santander", "montain"),
                new Topography("Albacete", "montain"),
                new Topography("Bilbao", "flat"));

        topographyFlux
                .flatMap(this.topographyRepository::save)
                .blockLast();
    }
}
