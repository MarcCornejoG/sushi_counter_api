package com.sushiapi.SushiApi.controller;

import com.sushiapi.SushiApi.model.dtos.ChangePasswordDTO;
import com.sushiapi.SushiApi.model.dtos.UserResponseDTO;
import com.sushiapi.SushiApi.model.dtos.UserUpdateDTO;
import com.sushiapi.SushiApi.model.entities.User;
import com.sushiapi.SushiApi.model.userjwt.UserPrincipal;
import com.sushiapi.SushiApi.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')")
public class UserController {
    private final UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getProfile(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        UserResponseDTO user = userService.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateProfile(
            @Valid @RequestBody UserUpdateDTO updateDTO,
            Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserResponseDTO updatedUser = userService.updateUser(userPrincipal.getId(), updateDTO);


        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO,
            Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        userService.changePassword(
                userPrincipal.getId(),
                changePasswordDTO.getCurrentPassword(),
                changePasswordDTO.getNewPassword()
        );

        return ResponseEntity.ok("Contraseña cambiada exitosamente");
    }

}
