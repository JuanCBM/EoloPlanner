package com.protocolos.planner.services;

import org.springframework.scheduling.annotation.Async;

public interface WriterService {

    @Async
    void write(String message);

}
