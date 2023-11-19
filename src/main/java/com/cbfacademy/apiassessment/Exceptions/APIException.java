package com.cbfacademy.apiassessment.Exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class APIException {
    private final String message;
    private final HttpStatus httpStatus;
    private final Date timeStamp;

    public APIException(String message,
                        HttpStatus httpStatus,
                        Date timeStamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timeStamp = timeStamp;
    }
}
