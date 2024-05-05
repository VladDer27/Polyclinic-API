package bam.Polyclinic.API.service;

import bam.Polyclinic.API.model.entity.User;
import bam.Polyclinic.API.repository.UserRepository;
import bam.Polyclinic.API.utils.enums.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findAllByRole(role);
    }

    public User getUserById(long id) {
        return userRepository.getById(id);
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserById(long userId) {
        userRepository.deleteById(userId);
    }


    public User updateExistingUserObject(User existingUser, User newUser) {
        existingUser.setFirstName(newUser.getFirstName());
        existingUser.setLastName(newUser.getLastName());
        existingUser.setMiddleName(newUser.getMiddleName());
        existingUser.setLogin(newUser.getLogin());
//        existingUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return existingUser;
    }
}
