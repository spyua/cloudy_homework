package com.ck.account.controller;

import com.ck.account.controller.bean.UserInfoDto;
import com.ck.account.controller.bean.UserInfoRelayDto;


import com.ck.security.service.impl.JwtUserDetailsServiceImpl;
import com.ck.security.utility.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserVerifyController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserInfoDto userInfoDto) throws Exception {

        authenticate(userInfoDto.getUserAccountID(), userInfoDto.getUserPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(userInfoDto.getUserAccountID());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new UserInfoRelayDto(token));

    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserInfoDto user) throws Exception {
        return ResponseEntity.ok(userDetailsService.createAccount(user.getUserAccountID(),user.getUserPassword()));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
