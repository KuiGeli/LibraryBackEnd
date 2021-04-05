package com.library.library.Controller;

import com.library.library.Model.ERole;
import com.library.library.Model.Role;
import com.library.library.Model.User;
import com.library.library.Rpository.RoleRep;
import com.library.library.Rpository.UserRep;
import com.library.library.Security.jwt.JwtUtils;
import com.library.library.Security.payload.request.LoginRequest;
import com.library.library.Security.payload.request.SignupRequest;
import com.library.library.Security.payload.response.JwtResponse;
import com.library.library.Security.payload.response.MessageResponse;
import com.library.library.Security.secService.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("*")
public class AuthController {

    AuthenticationManager authenticationManager;
    UserRep userRep;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;
    RoleRep roleRep;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRep userRep,
                          PasswordEncoder passwordEncoder, JwtUtils jwtUtils, RoleRep roleRep) {
        this.authenticationManager = authenticationManager;
        this.userRep = userRep;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.roleRep = roleRep;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRep.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Usernameis already taken!"));
        }

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role editorRole = roleRep.findByName(ERole.ROLE_USER).orElseThrow(() ->
                    new RuntimeException("Error: Role is not found"));
            roles.add(editorRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "user":
                        Role useRole = roleRep.findByName(ERole.ROLE_ADMIN).orElseThrow(
                                () -> new RuntimeException("Error: Role is not Found"));
                        roles.add(useRole);
                        break;
                    case "admin":
                        Role adminRole = roleRep.findByName(ERole.ROLE_USER).orElseThrow(
                                () -> new RuntimeException("Error: Role is not Found"));
                        roles.add(adminRole);
                }
            });

            User user = new User(signupRequest.getUsername(),
                    passwordEncoder.encode(signupRequest.getPassword()),
                    roles
            );

            userRep.save(user);

        }
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

}
