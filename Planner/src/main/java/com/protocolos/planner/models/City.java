package com.protocolos.planner.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class City {

  Long id;
  String city;
  Integer progress;
  Boolean completed;
  String planning;

}
