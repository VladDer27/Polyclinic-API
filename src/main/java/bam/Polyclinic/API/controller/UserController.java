package bam.Polyclinic.API.controller;

import bam.Polyclinic.API.model.entity.User;
import bam.Polyclinic.API.model.request.LoginRequest;
import bam.Polyclinic.API.model.response.UserResponse;
import bam.Polyclinic.API.service.UserService;
import bam.Polyclinic.API.utils.UserValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserValidator userValidator;
    private final ModelMapper modelMapper;

    private final UserService userService;

//    @PostMapping("/create")
//    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest request, BindingResult bindingResult){
//        User user = modelMapper.map(request, User.class);
//        userValidator.validate(user, bindingResult);
//        if (bindingResult.hasErrors())
//            return ResponseEntity.badRequest().build();
//        userService.register(user);
//        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(user, UserResponse.class));
//    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> userResponses = users.stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable long id){
        return ResponseEntity.ok(modelMapper.map(userService.getUserById(id), UserResponse.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable long id, @RequestBody @Valid LoginRequest request){
        User existedUser = userService.updateExistingUserObject(userService.getUserById(id), modelMapper.map(request, User.class));
        return  ResponseEntity.ok(modelMapper.map(existedUser, UserResponse.class));
    }

}
