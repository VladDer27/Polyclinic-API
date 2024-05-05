package bam.Polyclinic.API.utils;

import bam.Polyclinic.API.model.entity.User;
import bam.Polyclinic.API.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (userService.getUserByLogin(user.getLogin()) != null) {
            errors.rejectValue("login", "",
                    "Пользователь с такой почтой уже зарегистрирован");
        }
    }
}
