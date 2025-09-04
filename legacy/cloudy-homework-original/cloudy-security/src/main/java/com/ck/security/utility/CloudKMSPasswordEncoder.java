package com.ck.security.utility;

import com.ck.security.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CloudKMSPasswordEncoder implements PasswordEncoder {

    @Autowired
    CryptoService cryptoService;

    @Override
    public String encode(CharSequence rawPassword) {
        return cryptoService.encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
       String decodePassword = cryptoService.decrypt(encodedPassword);
       return rawPassword.equals(decodePassword);
    }
}
