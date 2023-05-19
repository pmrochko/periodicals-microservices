package com.periodicals.paymentservice.model.exception;

/**
 * @author Pavlo Mrochko
 */
public class DoesNotHaveAccessException extends ServiceException {

    private static final String DEFAULT_MESSAGE = "User is not authenticated";

    public DoesNotHaveAccessException(){
        super(DEFAULT_MESSAGE);
    }

    public DoesNotHaveAccessException(String message){
        super(message);
    }

    @Override
    public ErrorType getErrorType(){
        return ErrorType.SECURITY_ERROR_TYPE;
    }

}
