package com.sushiapi.SushiApi.service;

import com.sushiapi.SushiApi.model.dtos.*;
import com.sushiapi.SushiApi.model.entities.User;
import com.sushiapi.SushiApi.model.userjwt.UserPrincipal;
import com.sushiapi.SushiApi.repository.UserRepository;
import com.sushiapi.SushiApi.utils.JwtUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {


    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    @Autowired
    private ModelMapper modelMapper;

    public AuthService(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        try {


            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            System.out.println("Autenticación exitosa!");
            System.out.println("Usuario autenticado: " + authentication.getName());

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            userService.updateLastAccess(userPrincipal.getId());

            UserResponseDTO user = userService.findById(userPrincipal.getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            String accessToken = jwtUtils.generateAccessToken(userPrincipal.getUsername());
            String refreshToken = jwtUtils.generateRefreshToken(userPrincipal.getUsername());

            UserResponseDTO userResponse = modelMapper.map(user, UserResponseDTO.class);

            return AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(86400L)
                    .user(userResponse)
                    .build();
        }catch (Exception e){

            System.out.println("Error en autenticación: " + e.getClass().getSimpleName());
            System.out.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public AuthResponseDTO register(UserDTO userDTO) {
        UserResponseDTO user = userService.createUser(userDTO);

        String accessToken = jwtUtils.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());


        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .user(user)
                .build();
    }

    public AuthResponseDTO refreshToken(RefreshTokenRequestDTO request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtUtils.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token inválido");
        }

        String username = jwtUtils.getUsernameFromToken(refreshToken);

        UserResponseDTO user = userService.findByUsernameOrEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String newAccessToken = jwtUtils.generateAccessToken(username);



        return AuthResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .user(user)
                .build();
    }

}
