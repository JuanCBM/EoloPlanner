package com.protocolos.toposervice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Topography not found")
public class TopographyNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 4795879547644246095L;
}
