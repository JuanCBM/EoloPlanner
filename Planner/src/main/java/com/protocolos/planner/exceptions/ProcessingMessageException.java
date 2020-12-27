package com.protocolos.planner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Queue creation message invalid format")
public class ProcessingMessageException extends RuntimeException {
    private static final long serialVersionUID = 4795879547644246095L;
}
