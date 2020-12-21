package com.protocolos.toposervice.repositories;

import com.protocolos.toposervice.models.Topography;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopographyRepository extends ReactiveMongoRepository<Topography, String> {

}
