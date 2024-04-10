package com.javarush.TagMe.validator;

import com.javarush.TagMe.dto.UserDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())){
            errors.rejectValue("confirmPassword","Match","Пароли не совпадают");
        }
    }
}
