package com.sushiapi.SushiApi.service;

import com.sushiapi.SushiApi.model.dtos.UserDTO;
import com.sushiapi.SushiApi.model.dtos.UserResponseDTO;
import com.sushiapi.SushiApi.model.dtos.UserUpdateDTO;
import com.sushiapi.SushiApi.model.entities.User;
import com.sushiapi.SushiApi.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
public UserResponseDTO createUser (UserDTO userDTO){

        try {
            validateUserDoesNotExist(userDTO);

            User user = modelMapper.map(userDTO, User.class);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setActive(true);
            User savedUser = userRepository.save(user);

            return modelMapper.map(savedUser, UserResponseDTO.class);
        } catch (Exception e) {
            e.printStackTrace(); 
            throw e;
        }

}

    @Transactional
public UserResponseDTO updateUser (Long userId, UserUpdateDTO updateDTO){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (updateDTO.getFirstName() != null) {
            user.setFirstName(updateDTO.getFirstName());
        }
        if (updateDTO.getLastName() != null) {
            user.setLastName(updateDTO.getLastName());
        }
        if (updateDTO.getEmail() != null) {
            validateEmailNotInUse(updateDTO.getEmail(), userId);
            user.setEmail(updateDTO.getEmail());
        }

         userRepository.save(user);

        return modelMapper.map(user, UserResponseDTO.class);
}

public Optional<UserResponseDTO> findByUsernameOrEmail (String usernameOrEmail) {
    Optional<User> user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

    return user.map(u -> modelMapper.map(u, UserResponseDTO.class));
}

public Optional <UserResponseDTO> findById (Long userId){
    Optional<User> user = userRepository.findById(userId);

    return user.map(u -> modelMapper.map(u, UserResponseDTO.class));
}

public void updateLastAccess(Long userId){
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    user.setLastAccess(LocalDateTime.now());
    userRepository.save(user);
}

public void changePassword (Long userId, String currentPassword, String newPassword){
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
        throw new RuntimeException("Contraseña actual incorrecta");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
}
// AUXILIARY METHODS

    private void validateUserDoesNotExist(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("El email ya está en uso");
        }
    }

    private void validateEmailNotInUse(String email, Long userId) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            throw new RuntimeException("El email ya está en uso");
        }
    }

}
