package bam.Polyclinic.API.service;

import bam.Polyclinic.API.model.entity.User;
import bam.Polyclinic.API.repository.UserRepository;
import bam.Polyclinic.API.utils.enums.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public EncryptionService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.ROLE_GUEST);
        userRepository.save(user);
    }
}
