package com.rx.validators;


import com.rx.dao.UserRole;
import com.rx.dto.AddingUserFormDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class AddingUserFormDtoValidator implements Validator {

    private final Pattern emailPattern;

    @Autowired
    public AddingUserFormDtoValidator(@Value("${app.email.pattern}") String emailPattern) {
        this.emailPattern = Pattern.compile(emailPattern);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AddingUserFormDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AddingUserFormDto dto = (AddingUserFormDto) target;

        validateLogin(dto.getLogin(), errors);
        validateEmail(dto.getEmail(), errors);
        validatePassword(dto.getPassword(), errors);
        validateFirstName(dto.getFirstName(), errors);
        validateLastName(dto.getLastName(), errors);
        validateMiddleName(dto.getMiddleName(), errors);
        validateUserRole(dto.getUserRole(), errors);
    }

    private void validateLogin(String login, Errors errors) {
        if (login == null || login.isEmpty()) {
            errors.rejectValue("login", "add.user.field.not.specified");
        } else if (login.length() > 128 || login.length() < 6) {
            errors.rejectValue("login", "add.user.invalid.size.range");
        }
    }

    private void validateEmail(String email, Errors errors) {
        if (email == null || email.isEmpty()) {
            errors.rejectValue("email", "add.user.field.not.specified");
        } else if (!emailPattern.matcher(email).matches()) {
            errors.rejectValue("email", "add.user.invalid.email");
        }
    }

    private void validatePassword(String password, Errors errors) {
        if (password == null || password.isEmpty()) {
            errors.rejectValue("password", "add.user.field.not.specified");
        } else if (password.length() > 128 || password.length() < 6) {
            errors.rejectValue("password", "add.user.invalid.size.range");
        }
    }

    private void validateLastName(String lastName, Errors errors) {
        if (lastName == null || lastName.isEmpty()) {
            errors.rejectValue("lastName", "add.user.field.not.specified");
        } else if (lastName.length() > 128) {
            errors.rejectValue("lastName", "add.user.invalid.size");
        }
    }

    private void validateFirstName(String firstName, Errors errors) {
        if (firstName == null || firstName.isEmpty()) {
            errors.rejectValue("firstName", "add.user.field.not.specified");
        } else if (firstName.length() > 128) {
            errors.rejectValue("firstName", "add.user.invalid.size");
        }
    }

    private void validateMiddleName(String middleName, Errors errors) {
        if (middleName == null || middleName.isEmpty()) {
            errors.rejectValue("middleName", "add.user.field.not.specified");
        } else if (middleName.length() > 128) {
            errors.rejectValue("middleName", "add.user.invalid.size");
        }
    }

    private void validateUserRole(UserRole userRole, Errors errors) {
        if (userRole == null) {
            errors.rejectValue("userRole", "add.user.field.not.specified");
        }
    }
}
