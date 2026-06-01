package com.javaduolingo.service;

import com.javaduolingo.dto.RegisterForm;
import com.javaduolingo.model.User;
import com.javaduolingo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterForm form) {
        if (userRepository.existsByEmail(form.getEmail().toLowerCase().trim())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
        if (!form.passwordsMatch()) {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }
        User user = new User(
                form.getName(),
                form.getEmail().toLowerCase().trim(),
                passwordEncoder.encode(form.getPassword()),
                User.Role.USER
        );
        user.setPhone(form.getPhone());
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase().trim());
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateProfile(User user, String name, String phone, String cpf) {
        user.setName(name);
        user.setPhone(phone);
        user.setCpf(cpf);
        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void toggleActive(Long userId) {
        userRepository.findById(userId).ifPresent(u -> {
            u.setActive(!u.isActive());
            userRepository.save(u);
        });
    }

    public long countUsers() { return userRepository.countByActive(true); }
}
