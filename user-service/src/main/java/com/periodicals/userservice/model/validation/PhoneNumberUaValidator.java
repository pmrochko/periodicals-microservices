package com.periodicals.userservice.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Pavlo Mrochko
 */
public class PhoneNumberUaValidator implements ConstraintValidator<ValidPhoneNumberUa, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        String regex = "^(?:\\+38)?(?:\\([0-9]{3}\\)[ .-]?[0-9]{3}[ .-]?[0-9]{2}[ .-]?[0-9]{2}|" +
                "[0-9]{3}[ .-]?[0-9]{3}[ .-]?[0-9]{2}[ .-]?[0-9]{2}|" +
                "[0-9]{3}[0-9]{7})$\n";
        return (value != null) && (value.toString().matches(regex));
    }

}
