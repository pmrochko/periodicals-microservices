package com.periodicals.userservice.model.exception;

/**
 * @author Pavlo Mrochko
 */
public class EntityNotFoundException extends ServiceException {

    private static final String DEFAULT_MESSAGE = "Entity is not found";

    public EntityNotFoundException(){
        super(DEFAULT_MESSAGE);
    }

    public EntityNotFoundException(String message){
        super(message);
    }

    @Override
    public ErrorType getErrorType(){
        return ErrorType.VALIDATION_ERROR_TYPE;
    }

}
