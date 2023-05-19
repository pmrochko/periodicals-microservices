package com.periodicals.catalogservice.model.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Pavlo Mrochko
 */
@Data
@AllArgsConstructor
public class Error {

    private String message;

    private ErrorType errorType;

    private LocalDateTime timeStamp;

}
