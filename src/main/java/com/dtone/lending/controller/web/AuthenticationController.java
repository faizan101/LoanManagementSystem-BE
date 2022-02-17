package com.dtone.lending.controller.web;

import com.dtone.lending.domain.UserProfile;
import com.dtone.lending.dto.web.AuthenticationRequest;
import com.dtone.lending.repository.UserProfileRepository;
import com.dtone.lending.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserProfileRepository users;

    @PostMapping("/token")
    @CrossOrigin
    public ResponseEntity token(@RequestBody AuthenticationRequest data) {

        try {
            String username = data.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));

            UserProfile userProfile = this.users.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found"));
            String token = jwtTokenProvider.createToken(username, userProfile);

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);

            Map<Object, Object> userModel = new HashMap<>();
            userModel.put("role", userProfile.getRoles().get(0));
            userModel.put("username", username);

            model.put("user", userModel);

            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
}
