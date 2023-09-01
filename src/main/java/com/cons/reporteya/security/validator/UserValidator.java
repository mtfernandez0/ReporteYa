package com.cons.reporteya.security.validator;

import com.cons.reporteya.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (!user.getPassword().equals(user.getPasswordConfirmation()))
            errors.rejectValue(
                    "passwordConfirmation",
                    "Match"
            );
    }
}
