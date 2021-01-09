package com.protocolos.toposervice.services.impl;

import com.protocolos.toposervice.dtos.TopographyDTO;
import com.protocolos.toposervice.exceptions.TopographyNotFoundException;
import com.protocolos.toposervice.repositories.TopographyRepository;
import com.protocolos.toposervice.services.TopographyService;
import java.time.Duration;
import org.apache.commons.lang3.RandomUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TopographyServiceImpl implements TopographyService {
    private final TopographyRepository topographyRepository;
    private final ModelMapper modelMapper;

    private static final int MIN_DELAY = 1;
    private static final int MAX_DELAY = 3;

    public TopographyServiceImpl(TopographyRepository topographyRepository) {
        this.topographyRepository = topographyRepository;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public Mono<TopographyDTO> findById(String id) {
        return topographyRepository.findById(id)
                .map(topography -> this.modelMapper.map(topography, TopographyDTO.class))
                .switchIfEmpty(Mono.error(new TopographyNotFoundException()))
                .delayElement(Duration.ofSeconds(RandomUtils.nextInt(MIN_DELAY, MAX_DELAY + 1)));
    }

}
