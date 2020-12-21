package com.protocolos.toposervice.models;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@AllArgsConstructor
@Document(collection = "topographies")
public class Topography {
    @Id
    private String id;
    private String landscape;
}
