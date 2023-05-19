package com.periodicals.catalogservice.model.exception;

/**
 * @author Pavlo Mrochko
 */
public class NotUniqueParameterException extends ServiceException {

    private static final String DEFAULT_MESSAGE = "Incorrect entered data";

    public NotUniqueParameterException(){
        super(DEFAULT_MESSAGE);
    }

    public NotUniqueParameterException(String message){
        super(message);
    }

    @Override
    public ErrorType getErrorType(){
        return ErrorType.VALIDATION_ERROR_TYPE;
    }

}
