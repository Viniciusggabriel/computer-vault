package tech.vault.server.application.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.vault.server.application.dto.auth.AuthenticationRequestDTO;
import tech.vault.server.application.dto.auth.AuthenticationResponseDTO;
import tech.vault.server.application.dto.auth.RegisterRequestDTO;
import tech.vault.server.application.service.security.UserService;
import tech.vault.server.domain.entity.values.Role;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "*") //TODO: Trocar por ip do front-end
    public ResponseEntity<AuthenticationResponseDTO> registerUser(
            @RequestHeader(name = "username") String userName,
            @RequestHeader(name = "password") String password,
            @RequestHeader(name = "role") Role role
    ) {
        RegisterRequestDTO request = new RegisterRequestDTO(userName, password, role);

        return ResponseEntity.status(HttpStatus.CREATED).body(service.userRegister(request));
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "*") //TODO: Trocar por ip do front-end
    public ResponseEntity<AuthenticationResponseDTO> authenticateUser(@RequestHeader(name = "username") String userName,
                                                                      @RequestHeader(name = "password") String password) {
        AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO(userName, password);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.authenticate(authenticationRequestDTO));
    }
}
