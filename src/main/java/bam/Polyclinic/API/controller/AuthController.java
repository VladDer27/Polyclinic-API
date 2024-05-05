package bam.Polyclinic.API.controller;

import bam.Polyclinic.API.model.entity.User;
import bam.Polyclinic.API.model.request.LoginRequest;
import bam.Polyclinic.API.model.response.LoginResponse;
import bam.Polyclinic.API.model.response.UserResponse;
import bam.Polyclinic.API.service.AuthService;
import bam.Polyclinic.API.service.EncryptionService;
import bam.Polyclinic.API.utils.UserValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final ModelMapper modelMapper;

    private final UserValidator userValidator;

    private final EncryptionService encryptionService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request){
        return ResponseEntity.ok(authService.attemptLogin(request.getLogin(), request.getPassword()));
    }

    @PostMapping("/registration")
    public ResponseEntity<UserResponse> registration(@RequestBody @Valid LoginRequest request,
                                                     BindingResult bindingResult){
        User newUser = modelMapper.map(request, User.class);
        userValidator.validate(newUser, bindingResult);
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().build();
        encryptionService.register(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(newUser, UserResponse.class));
    }
}
