package com.periodicals.paymentservice.model.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Pavlo Mrochko
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceException extends RuntimeException {

    private ErrorType errorType;

    public ServiceException(String message) {
        super(message);
    }

    public ErrorType getErrorType() {
        return ErrorType.PROCESSING_ERROR_TYPE;
    }

}
