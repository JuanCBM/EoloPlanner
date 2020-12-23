package com.protocolos.planner.services;

import com.protocolos.planner.models.Topography;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "topoService", url = "http://localhost:8080/api")
public interface TopoService {

    @GetMapping("/topographicdetails/{city}")
    Topography getTopographicDetails(@PathVariable("city") String city);

}