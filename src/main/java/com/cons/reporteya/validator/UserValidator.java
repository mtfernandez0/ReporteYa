package com.cons.reporteya.validator;

import com.cons.reporteya.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Calendar;
import java.util.Date;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        checkUserPasswordMatch(user, errors);
        checkUserOverEighteen(user.getDate_of_birth(), errors);
    }

    private void checkUserPasswordMatch(User user, Errors errors){
        if (!user.getPassword().equals(user.getPasswordConfirmation()))
            errors.rejectValue(
                    "passwordConfirmation",
                    "Match"
            );
    }

    private void checkUserOverEighteen(Date date, Errors errors){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -18);
        Date eighteenYearsAgo = calendar.getTime();

        if (date.after(eighteenYearsAgo)){
            errors.rejectValue(
                    "date_of_birth",
                    "Time"
            );
        }
    }
}
