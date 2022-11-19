package com.Ecomm.Ecommerce.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorFormat {
    private LocalDateTime timeStamp;
    private String message;
    private String description;
    private HttpStatus status;
    public ErrorFormat(LocalDateTime timeStamp, String message, String description, HttpStatus status) {
        this.timeStamp = timeStamp;
        this.message = message;
        this.description = description;
        this.status = status;
    }
}
